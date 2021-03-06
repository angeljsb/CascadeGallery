/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.image.BufferedImage;
import simple.file.image.ImageLoader;

/**
 * Clase encargada de controlar un conjunto de objetos ImageInfo y procesar
 * los cambios que deben haber en ellos según los eventos activados y los
 * cambios en la interfaz.
 * 
 * @author Angel
 * 
 * @since v1.0.1
 */
public class ImagesController {
    
    private ImageInfo[] images = new ImageInfo[0];
    
    private int start = 0, end = 0, current = -1;
    
    private int maxWidth = Integer.MAX_VALUE;
    private int minWidth = 0;
    
    private Thread loadThread = null;
    private boolean stop = false;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setStart(int start){
        this.start = start;
    }
    
    public void setEnd(int end){
        this.end = end;
    }
    
    public void setRange(int start, int end){
        if(start==this.start && end == this.end) return;
        this.setStart(start);
        this.setEnd(end);
        if(!this.isLoading()){
            this.proccessChange();
        }
        this.deleteAllImages();
    }
    
    public void setCurrent(int current) {
        if (this.current == current) return;
        this.current = current;
        int min = Math.max(0, current - 10);
        int max = Math.min(this.images.length, current + 10);
        this.setRange(min, max);
    }

    /**
     * Devueve el ancho maximo de las imagenes controladas.
     * 
     * @return El ancho maximo
     * 
     * @since v1.0.1
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Cambia el ancho maximo de las imagenes de este controlador. Aquellas
     * cuyo ancho sea mayor que este serán redimensionadas para coincidir con
     * el maximo.
     * 
     * @param maxWidth El nuevo ancho maximo
     * 
     * @since v1.0.1
     */
    public void setMaxWidth(int maxWidth) {
        if(this.maxWidth == maxWidth) return;
        
        this.maxWidth = maxWidth;
        
        this.proccessChange();
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        if(this.minWidth == minWidth) return;
        
        this.minWidth = minWidth;
        
        this.proccessChange();
    }
    
    public void setImages(ImageInfo[] images){
        if (images == this.images) return;
        
        this.stopLoading();
        
        for(ImageInfo next : images){
            this.checkExistance(next);
        }
        this.images = images;
        
        this.proccessChange();
    }
    
    public void proccessChange(){
        this.stopLoading();
        if(images.length == 0) return;
        
        Runnable run = this::updateAllImages;
        this.loadThread = new Thread(run);
        this.loadThread.setDaemon(true);
        this.loadThread.start();
    }
    
    public void updateAllImages(){
        while(updateNextImage() && !this.stop){}
    }
    
    private boolean updateNextImage(){
        for(int i=current; i<end; i++){
            if(updateImage(images[i])){
                return true;
            }
        }
        for(int i=current-1; i>=start; i--){
            if(updateImage(images[i])){
                return true;
            }
        }
        return false;
    }
    
    public boolean updateImage(ImageInfo image) {
        boolean load = !image.isLoaded();
        if(load){
            this.loadImage(image);
        }
        
        return this.validateSize(image) || load;
    }
    
    public void loadImage(ImageInfo image){
        BufferedImage loaded = ImageLoader.loadImage(image.getFile());
        image.setImage(loaded);
        image.setRealWidth(image.getCurrentWidth());
    }
    
    public boolean validateSize(ImageInfo image){
        if(maxWidth<image.getRealWidth()){
            return this.resizeImage(image, maxWidth);
        }else if(minWidth>image.getRealWidth()){
            return this.resizeImage(image, minWidth);
        }else if(this.shouldLoad(image)){
            this.loadImage(image);
            return true;
        }
        return false;
    }
    
    public boolean resizeImage(ImageInfo image, int width){
        if(image.getCurrentWidth() == width) return false;
        if(this.shouldLoad(image)){
            this.loadImage(image);
        }
        BufferedImage resized = ImageLoader.resizeImage(image.getImage(), width, -1);
        image.setImage(resized);
        return true;
    }
    
    public void deleteAllImages(){
        for(int i=0; i<this.images.length; i++){
            if((i<this.getStart() || i>this.getEnd()) 
                    && this.images[i].isLoaded()){
                this.images[i].setImage(null);
            }
        }
    }
    
    public void deleteImage(ImageInfo image) {
        image.setImage(null);
    }
    
    private boolean shouldLoad(ImageInfo image){
        return image.getCurrentWidth() != image.getRealWidth();
    }
    
    public void stopLoading(){
        if (!this.isLoading()) return;
        
        this.stop = true;
        
        try{
            this.loadThread.join();
        }catch(InterruptedException ex){
            System.err.println(ex);
        }
        
        this.stop = false;
    }
    
    public boolean isLoading(){
        return (this.loadThread != null && this.loadThread.isAlive());
    }
    
    public void checkExistance(ImageInfo image) {
        for(ImageInfo prev : this.images){
            if(image.equals(prev)){
                image.clone(prev);
            }
        }
    }
    
}
