/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Componente que muestra una imagen centrada o ajustada al ancho
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public class ImageFixWidth extends JPanel {
    
    private Image showImage;
    
    private boolean ajustWidth = false;
    
    private Dimension lastSize = new Dimension(0, 0);
    
    /**
     * Crea un componente sin una imagen, el cual tendrá un tamaño de 0, 0
     * hasta que se le agregue una imagen
     * 
     * @since v1.0.1
     */
    public ImageFixWidth(){
        this.showImage = null;
    }
    
    /**
     * Crea un componente que muestra la imagen pasada y adapta su tamaño según
     * el ancho del padre y la altura de la imagen
     * 
     * @param image La imagen a mostrar
     * 
     * @since v1.0.0
     */
    public ImageFixWidth(Image image){
        this.showImage = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(this.showImage == null){
            return;
        }
        
        super.paintComponent(g); 
        
        if(this.isAjustWidth()){
            this.paintImageAjustWidth(g);
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
    public void paintImageAjustWidth(Graphics g){
        int imgWidth = showImage.getWidth(this);
        int imgHeight = showImage.getHeight(this);
        
        g.drawImage(showImage, 0, 0, this.getWidth(), this.getHeight(), 0, 0, imgWidth, imgHeight, this);
    }
    
    /**
     * Pinta la imagen n su tamaño real y la muestra centrada e el componente
     * 
     * @param g El objeto Graphics del componente en el que se va a pintar
     * 
     * @since v1.0.0
     */
    public void paintImageRealSize(Graphics g){
        int imgWidth = showImage.getWidth(this);
        
        if(imgWidth > this.getWidth()){
            this.paintImageAjustWidth(g);
            return;
        } 
        
        int imgX = (this.getWidth()/2) - (imgWidth/2);
        
        g.drawImage(showImage, imgX, 0, this);
    }
    
    @Override
    public Dimension getPreferredSize(){
        int imgWidth = this.lastSize.width,
                imgHeight = this.lastSize.height;
        
        if(imgHeight==0 || imgWidth==0){
            return this.lastSize;
        }
        
        float relation = (float)imgWidth/(float)imgHeight;
        
        if(this.isAjustWidth()){
            return new Dimension(this.getWidth(), (int)(this.getWidth()/relation));
        }else{
            return new Dimension(imgWidth, imgHeight);
        }
    }

    /**
     * Devuelve la imagen mostrada por este componente
     * 
     * @return La imagen que muestra este componente
     * 
     * @since v1.0.0
     */
    public Image getShowImage() {
        return showImage;
    }

    /**
     * Cambia la imagen que se muestra en este componente
     * 
     * @param showImage La nueva imagen a mostrar
     * 
     * @since v1.0.0
     */
    public void setShowImage(BufferedImage showImage) {
        this.showImage = showImage;
        
        if(showImage!=null){
            this.lastSize = new Dimension(showImage.getWidth(), showImage.getHeight());
        }
        
        this.updateUI();
    }

    /**
     * Evalua si el componente está ajustando el ancho de la imagen
     * 
     * @return Si el componente está ajustando el ancho de la imagen
     * 
     * @since v1.0.0
     */
    public boolean isAjustWidth() {
        return ajustWidth || lastSize.width > this.getWidth();
    }

    /**
     * De ser <code>true</code> fuerza a la imagen a adaptarse al ancho del
     * componente
     * 
     * @param ajustWidth Si se va a forzar el ajuste de ancho
     * 
     * @since v1.0.0
     */
    public void setAjustWidth(boolean ajustWidth) {
        if (this.ajustWidth == ajustWidth) return;
        
        this.ajustWidth = ajustWidth;
        
        this.updateUI();
    }
    
}
