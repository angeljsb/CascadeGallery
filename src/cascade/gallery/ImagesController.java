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
    
    private int start = 0;
    private int end = 0;
    
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
        System.out.println("Setting range " + this.start + ", " + this.end);
        if(!this.isLoading()){
            System.out.println("Proccessing");
            this.proccessChange();
        }
        this.deleteAllImages();
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
        
        Runnable run = this::updateAllImages;
        this.loadThread = new Thread(run);
        this.loadThread.setDaemon(true);
        this.loadThread.start();
    }
    
    public void updateAllImages(){
        int initS = this.getStart();
        for(int i = start; i<end; i++){
            ImageInfo image = this.images[i];
            
            this.updateImage(image);
            
            if(this.stop){
                return;
            }
        }
        if(initS!=this.getStart()){
            this.updateAllImages();
        }
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
    
    public void updateImage(ImageInfo image) {
        if(!image.isLoaded()){
            System.out.println(image.getFile().getName());
            this.loadImage(image);
        }
        
        this.validateSize(image);
    }
    
    public void loadImage(ImageInfo image){
        BufferedImage loaded = ImageLoader.loadImage(image.getFile());
        image.setImage(loaded);
        image.setRealWidth(image.getCurrentWidth());
    }
    
    public void resizeImage(ImageInfo image, int width){
        if(image.getCurrentWidth() == width) return;
        BufferedImage resized = ImageLoader.resizeImage(image.getImage(), width, -1);
        image.setImage(resized);
    }
    
    public void validateSize(ImageInfo image){
        if(maxWidth<image.getRealWidth()){
            if(image.getCurrentWidth() != image.getRealWidth()
                    && image.getCurrentWidth() != maxWidth){
                this.loadImage(image);
            }
            this.resizeImage(image, maxWidth);
        }else if(minWidth>image.getRealWidth()){
            if(image.getRealWidth() > image.getCurrentWidth()){
                this.loadImage(image);
            }
            this.resizeImage(image, minWidth);
        }else if(image.getCurrentWidth() != image.getRealWidth()){
            this.loadImage(image);
        }
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
