/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.io.File;
import java.io.IOException;
import simple.file.BaseFile;
import simple.file.text.TextFile;
import simple.file.text.TextLoader;

/**
 *
 * @author Angel
 */
public class BrowserViewManager {
    
    public static final BaseFile PLANTILLA = new BaseFile(App.RESOURCES, "plantilla.html"),
            TEMPORAL = new BaseFile(App.RESOURCES, "tmp.html");
    
    /**
     * Muestra en el navegador las imagenes de los archivos pasados
     * 
     * @param images Los archivos con las imagenes que se quiere mostrar
     */
    public void show(File[] images){
        try {
            TEMPORAL.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        TEMPORAL.deleteOnExit();
        
        TextFile textFile = new TextFile(TEMPORAL);
        
        textFile.setContent(createTmpText(images));
        textFile.write();
        
        TEMPORAL.open();
    }
    
    /**
     * Crea el html del archivo que será mostrado en el navegador
     * 
     * @param images Los archivos que contienen las imagenes a mostrar
     * @return Un string con el html que muestra todas las imagenes
     */
    public String createTmpText(File[] images){
        String plantilla = TextLoader.read(PLANTILLA);
        
        String tmp = plantilla
                .replace("%title%", images[0].getParentFile().getName());
        
        tmp = tmp.replace("%images%", createAllImagesTag(images));
        
        return tmp;
    }
    
    /**
     * Crea un string con todas las etiquetas de imagenes para un
     * arreglo de archivos
     * 
     * @param images Los archivos de imagen
     * @return Un string con todas las etiquetas sin separación entre ellas
     */
    public String createAllImagesTag(File[] images){
        String html = "";
        for(File image : images){
            if(!ImageFilesControl.isImage(image)) continue;
            html += createImageTag(image);
        }
        return html;
    }
    
    /**
     * Crea una etiqueta html de imagen para un archivo
     * 
     * @param imagen El archivo que guarda la imagen
     * @return El outerHTML de la etiqueta
     */
    public String createImageTag(File imagen){
        return "<img "
                + "id=\"" + imagen.getName() + "\" "
                + "src=\"" + imagen.toURI() + "\" "
                + "alt=\"" + imagen.getName() + "\" >";
    }
    
}
