/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Guarda la información necesaria para mantener actualizada una imagen
 * proveniente desde un archivo y admite un efecto secundario para el cambio
 * de la imagen
 * 
 * @since v1.0.1
 * 
 * @author Angel
 */
public class ImageInfo {
    
    private BufferedImage image = null;
    private File file;
    private int realWidth;
    private int currentWidth;
    
    /**
     * Acción que se activa al cambiar/actualizar la imagen guardada y recibe como
     * parametro la nueva imagen
     * 
     * @since v1.0.1
     */
    public Consumer<BufferedImage> onChange = null;
    
    /**
     * Inicializa la clase a partir de un archivo el cual debe ser el que
     * contiene la imagen
     * 
     * @param file El archivo de la imagen
     * 
     * @since v1.0.1
     */
    public ImageInfo(File file){
        if( !ImageFilesControl.isImage(file) ){
            throw new IllegalArgumentException("file debe ser una imagen");
        }
        
        this.file = file;
    }

    /**
     * Devuelve la imagen guardada actualmente. Esta puede estar redimensionada
     * o cambiada de algún modo según las acciones previas.
     * 
     * @return La versión actual de la imagen en el archivo
     * 
     * @since v1.0.1
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Cambia la imagen guardada actualmente y activa los efectos secundarios
     * 
     * @param image La nueva versión de la imagen
     * 
     * @since v1.0.1
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        if(image!=null)
            this.currentWidth = image.getWidth();
        this.performChange();
    }
    
    /**
     * Dice si hay una versión de la imagen cargada actualmente.
     * 
     * @return Si la imagen ya fue cargada.
     * 
     * @since v1.0.1
     */
    public boolean isLoaded() {
        return this.image != null;
    }
    
    /**
     * Activa el efecto secundario,pasandole como parametro la versión actual
     * de la imagen
     * 
     * @since v1.0.1
     */
    public void performChange(){
        if(this.onChange != null){
            this.onChange.accept(this.image);
        }
    }
    
    /**
     * Copia la imagén y la información relevante de otro objeto ImageInfo
     * que corresponda al mismo archivo que este.
     * 
     * @param imageInfo El otro objeto del cual copiar sus datos
     * 
     * @since v1.0.1
     */
    public void clone(ImageInfo imageInfo){
        if (!this.equals(imageInfo)) return;
        
        this.realWidth = imageInfo.getRealWidth();
        this.currentWidth = imageInfo.getCurrentWidth();
        this.setImage(imageInfo.getImage());
    }

    /**
     * Devuelve el ancho guardado como el original de la imagen.
     * 
     * @return El ancho original de la imagen
     * 
     * @since v1.0.1
     */
    public int getRealWidth() {
        return realWidth;
    }

    /**
     * Permite ingresar el ancho real de la imagen. Es decir, el ancho de la
     * imagen guardada en el archivo.
     * 
     * @param realWidth El ancho de la imagen en su archivo de origen
     * 
     * @since v1.0.1
     */
    public void setRealWidth(int realWidth) {
        this.realWidth = realWidth;
    }

    /**
     * Devuelve el ancho de la versión actual de la imagen
     * 
     * @return El ancho de la imagen actual
     * 
     * @since v1.0.1
     */
    public int getCurrentWidth() {
        return currentWidth;
    }

    /**
     * Cambia el ancho guardado como el actual de la imagen.
     * 
     * @param currentWidth El nuevo ancho de la imagen actual.
     * 
     * @since v1.0.1
     */
    public void setCurrentWidth(int currentWidth) {
        this.currentWidth = currentWidth;
    }
    
    /**
     * Devuelve el archivo de origen del que se basa la imagen
     * 
     * @return El archivo de rigen de la imagen
     * 
     * @since v1.0.1
     */
    public File getFile() {
        return file;
    }

    /**
     * Cambia el archivo de origen en el que se basará este objeto
     * 
     * @param file El nuevo archivo usado como base para este objeto
     * 
     * @since v1.0.1
     */
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImageInfo other = (ImageInfo) obj;
        return Objects.equals(this.file, other.file);
    }
    
}
