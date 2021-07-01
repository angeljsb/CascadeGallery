/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import interfaz.layout.MostradorVertical;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * Componente que muestra un conjunto de imagenes en cascada
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public class ImagesView extends JPanel{
    
    private final MostradorVertical layout = new MostradorVertical(0);
    
    private boolean adjustWidth = false;
    
    /**
     * Crea un componente que puede mostrar un conjunto de imagenes en cascada
     * 
     * @since v1.0.0
     */
    public ImagesView(){
        super();
        
        super.setLayout(layout);
    }
    
    /**
     * Cambia si las imagenes se van a ajustar al ancho del componente
     * 
     * @param adjust Si las imagenes se vana  ajustar
     * 
     * @since v1.0.0
     */
    public void setAdjustWidth(boolean adjust){
        if(this.adjustWidth == adjust){
            return;
        }
        this.adjustWidth = adjust;
        
        for(int i= 0; i<this.getComponentCount(); i++){
            Component comp = this.getComponent(i);
            if(comp instanceof ImageFixWidth){
                ImageFixWidth imageFixWidth = (ImageFixWidth) comp;
                imageFixWidth.setAjustWidth(adjust);
            }
        }
    }
    
    /**
     * Dice si el componente está ajustando el ancho de todas las magenes
     * automaticamente
     * 
     * @return Si se está ajustando el ancho
     * 
     * @since v1.0.1
     */
    public boolean isAdjustWidth(){
        return this.adjustWidth;
    }
    
    /**
     * Cambia la separación entre las imagenes
     * 
     * @param separation La nueva separación entre imagenes en pixeles
     * 
     * @since v1.0.0
     */
    public void setSeparation(int separation){
        this.layout.setSeparacion(separation);
        
        this.updateUI();
    }
    
    /**
     * Devuelve la separación actual entre las imagenes
     * 
     * @return La separación entre las imagenes en pixeles
     * 
     * @since v1.0.0
     */
    public int getSeparation(){
        return this.layout.getSeparacion();
    }
    
    public int indexOf(Component child){
        for(int i = 0; i<this.getComponentCount(); i++){
            if(this.getComponent(i)==child){
                return i;
            }
        }
        return -1;
    }
    
}
