/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollBar;

/**
 * Clase encargada de la carga de las imagenes basado en el scroll
 * con el proposito de ahorrar memoria
 *
 * @author Angel
 */
public class ScrollLoader implements Runnable {
    
    private ImagesView imagesView;
    private JScrollBar scrollBar;
    
    private int chargeZone = 20;
    
    public ScrollLoader(ImagesView imagesView, JScrollBar scrollBar){
        this.imagesView = imagesView;
        this.scrollBar = scrollBar;
    }

    @Override
    public void run() {
        
        while(true){
            if(imagesView.getComponentCount() == 0){
                continue;
            }
            
            int amount = scrollBar.getValue();
            Component component = this.imagesView.getComponentAt(0, amount);
            int imageOver = this.imagesView.indexOf(component);
            
            this.deleteImagesOut(imageOver-chargeZone, imageOver + chargeZone);
            
            if(this.loadImageRange(imageOver, imageOver + chargeZone)){
                continue;
            }
            
            if(this.loadImageRange(imageOver, imageOver - chargeZone)){
                continue;
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ScrollLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int getChargeZone() {
        return chargeZone;
    }

    public void setChargeZone(int chargeZone) {
        this.chargeZone = chargeZone;
    }
    
    /**
     * Carga la primera imagen en un rango que no esté cargada todavía
     * 
     * @param start El inicio del rango a revisar
     * @param end El final del rango a revisar
     * @return Si se tuvo que cargar alguna de las imagenes en el rango
     */
    public boolean loadImageRange(int start, int end){
        return start>end 
                ? this.loadImageInverted(start,end)
                : this.loadImageNormal(start,end);
    }
    
    private boolean loadImageNormal(int start, int end){
        int s = Math.max(start, 0);
        for(int i=s; i<end && i<this.imagesView.getComponentCount(); i++){
            if(this.loadImage(i)){
                System.out.println("Cargando " + i);
                return true;
            }
        }
        return false;
    }
    
    private boolean loadImageInverted(int start, int end){
        int s = Math.min(start, this.imagesView.getComponentCount());
        for(int i=s; i>end && i>=0; i--){
            if(this.loadImage(i)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Si la imagen en el indice enviado no está cargada, la carga.
     * 
     * @param index El indice de la imagen a cargar
     * @return Si se cargo, false si ya estaba cargada
     */
    private boolean loadImage(int index){
        Component review = this.imagesView.getComponent(index);
        if(review instanceof ImageFileView){
            ImageFileView ifv = (ImageFileView)review;

            if(!ifv.isShowingImage()){
                ifv.showImage();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Borra todas las imagenes que estén cargadas fuera del rango
     * 
     * @param start El inicio del rango
     * @param end El final del rango
     */
    private void deleteImagesOut(int start, int end){
        for(int i = 0; i<this.imagesView.getComponentCount(); i++){
            if(i>=start && i<=end) continue;
            
            Component review = this.imagesView.getComponent(i);
            if(review instanceof ImageFileView){
                ImageFileView ifv = (ImageFileView)review;
                ifv.hideImage();
                System.out.println("Borrando " + i);
            }
        }
    }
    
}
