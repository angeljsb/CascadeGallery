/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.io.File;

/**
 * Define un objeto que escucha el cambio de los archivos en colecciones
 * o arreglos de objetos tipo file.
 * 
 * @author Angel
 */
@FunctionalInterface
public interface FilesChangeListener {
    
    /**
     * Función que se activa al cambiar los archivos guardados por un objeto
     * 
     * @param files El nuevo arreglo de archivos
     */
    public void filesChange(File... files);
    
}
