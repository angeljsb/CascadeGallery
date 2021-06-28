/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import simple.file.image.ImageFile;

/**
 *
 * @author Angel
 */
public class ImageFileView extends JPanel {
    
    private ImageFile imageFile;
    
    private boolean fitWidth = false;
    private int height = 1;
    
    /**
     * Crea un componente sin una imagen, el cual tendrá un tamaño de 0
     * hasta que se le agregue una imagen
     * 
     * @since v1.0.1
     */
    public ImageFileView(){
        this.imageFile = null;
    }
    
    /**
     * Crea un componente que muestra la imagen pasada y adapta su tamaño según
     * el ancho del padre y la altura de la imagen
     * 
     * @param image La imagen a mostrar
     * 
     * @since v1.0.0
     */
    public ImageFileView(ImageFile image){
        this.imageFile = image;
        this.imageFile.getCapsule().setOnChange((i) -> this.updateUI());
    }
    
    /**
     * Obtiene la imagen mostrada actualmente
     * 
     * @return La imagen mostrada actualmente
     */
    public BufferedImage getImage(){
        return this.imageFile.getContent();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!this.imageFile.isLoaded()){
            return;
        }
        
        super.paintComponent(g); 
        
        if(this.isFitWidth()){
            this.paintImageFitWidth(g);
        }else{
            this.paintImageRealSize(g);
        }
        
    }
    
    /**
     * Pinta la imagen del componente ajustando el tamaño de la imagen según el
     * ancho del componente y redimensionando para no perder la escala
     * 
     * @param g El objeto Graphics del componente en el que se va a pintar
     * 
     * @since v1.0.0
     */
    public void paintImageFitWidth(Graphics g){
        BufferedImage image = this.getImage();
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, imgWidth, imgHeight, this);
    }
    
    /**
     * Pinta la imagen n su tamaño real y la muestra centrada e el componente
     * 
     * @param g El objeto Graphics del componente en el que se va a pintar
     * 
     * @since v1.0.0
     */
    public void paintImageRealSize(Graphics g){
        BufferedImage image = this.getImage();
        int imgWidth = image.getWidth();
        
        if(imgWidth > this.getWidth()){
            this.paintImageFitWidth(g);
            return;
        } 
        
        int imgX = (this.getWidth()/2) - (imgWidth/2);
        
        g.drawImage(image, imgX, 0, this);
    }
    
    /**
     * Indica si el tamaño de la imagen se está adaptando al ancho del
     * contenedor
     * 
     * @return Si se está forzando la adaptación al ancho
     */
    public boolean isFitWidth(){
        return this.fitWidth;
    }
    
    /**
     * Cambia si este componente va a ajustar automaticamente la imagen
     * para encajar con el ancho del componente. Incluso si este es false,
     * la imagen será redimensionada cuando no quepa en el componente. Pero
     * si es más pequeña, solo se redimensionará si el valor de esta propiedad
     * es true
     * 
     * @param fitWidth Si se quiere forzar la adaptación del ancho
     */
    public void setFitWidth(boolean fitWidth){
        this.fitWidth = fitWidth;
    }
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(this.getWidth(), this.height);
    }
    
    public void fitImageWidth(){
        if(this.isFitWidth()){
            this.imageFile.resizeToWidth(this.getWidth());
        }else{
            this.imageFile.resizeToWidth(0, this.getWidth());
        }
        this.height = this.imageFile.getContent().getHeight();
    }
    
    public boolean isShowingImage(){
        return this.imageFile.isLoaded() &&
                this.height == this.imageFile.getContent().getHeight();
    }
    
    public void showImage(){
        this.imageFile.read();
        this.fitImageWidth();
    }
    
    public void hideImage(){
        this.imageFile.nulliffy();
    }
    
}
