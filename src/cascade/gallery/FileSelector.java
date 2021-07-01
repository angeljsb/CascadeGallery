/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.Component;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * Clase que, de forma simple, se encarga de crear y controlar los dialogos de
 * escoger archivos y su respectiva entrada de datos
 * 
 * @since v1.0.0
 *
 * @author Angel
 */
public class FileSelector {
    
    /**
     * La clase FileSelector cuenta con dos JFileChooser instanciado.
     * chooseDir para elegir directorios y chooserImages para elegir conjuntos
     * de imagenes
     * @since v1.0.0
     */
    public JFileChooser chooserDir, chooserImages;
    
    private Component parentComponent;
    
    /**
     * Constructor básico que guarda
     * <code>null</code> como componente padre, lo cual indica que los
     * JDialog del JFileChooser no tendrán un componente padre<br><br>
     * 
     * Aún llamando a este constructor, se puede cambiar el componente
     * padre de los JDialog con <i>setParentComponent</i> en cualquier
     * momento de la ejecución
     * 
     * @since v1.0.0
     * @see setParentComponent(Component parent)
     */
    public FileSelector(){
        this(null);
    }
    
    /**
     * Crea una instancia de la clase con un padre especifico para los JDialog
     * en los que se mostrarán los JFileChooser
     * 
     * @param parent El componente padre de los JDialog
     * 
     * @since v1.0.0
     */
    public FileSelector(Component parent){
        
        this.parentComponent = parent;
        File home = FileSystemView.getFileSystemView().getHomeDirectory();
        
        this.chooserDir = new JFileChooser();
        this.chooserDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.chooserDir.setMultiSelectionEnabled(false);
        this.chooserDir.setCurrentDirectory(home);
        
        this.chooserImages = new JFileChooser();
        this.chooserImages.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.chooserImages.setMultiSelectionEnabled(true);
        FileFilter ff = new FileNameExtensionFilter("Images", "jpg", "gif", "png");
        this.chooserImages.setFileFilter(ff);
        this.chooserImages.setCurrentDirectory(home);
        
    }
    
    /**
     * Muestra un JDialog con un JFileChooser que permite seleccionar multiples
     * imagenes y detiene el hilo actual hasta que las imagenes han sido
     * seleccionadas
     * 
     * @return Un array con las imagenes seleccionadas si se presionó abrir
     * o <code>null</code> si canceló la operación
     * 
     * @since v1.0.0
     */
    public File[] selectImages(){
        
        int result = this.chooserImages.showOpenDialog(this.parentComponent);

        if (result != JFileChooser.CANCEL_OPTION) {

            File[] fileNames = this.chooserImages.getSelectedFiles();
            
            return fileNames;
        }
        
        return new File[0];
    }
    
    /**
     * Muestra un JDialog con un JFileChooser que permite seleccionar 
     * un directorio y detiene el hilo actual hasta un directorio ha sido
     * seleccionado
     * 
     * @return Un objeto File con el directorio seleccionado o <code>null</code>
     * si se canceló la operación
     * 
     * @since v1.0.0
     */
    public File selectDirectory(){
        int result = this.chooserDir.showOpenDialog(this.parentComponent);

        if (result != JFileChooser.CANCEL_OPTION) {

            File fileName = this.chooserDir.getSelectedFile();
            
            return fileName;
        }
        
        return null;
    }

    /**
     * Retorna el componente usado como padre para los JDialog que muestran
     * los JFileChooser de este objeto
     * 
     * @return El componente padre de los JDialog mostrados por los
     * metodos <i>selectDirectory()</i> y <i>selectImages()</i>
     * 
     * @since v1.0.0
     * @see selectDirectory()
     * @see selectImages()
     */
    public Component getParentComponent() {
        return parentComponent;
    }

    /**
     * Cambia el componente que será usado como padre al momento de
     * abrir los JDialog para los JFileChooser.<br><br>
     * 
     * A partir de la llamada a este metodo, los dialog que se abran estarán
     * añadidos al componente padre pasado
     * 
     * @param parent El nuevo componente padre de los JDialog
     * 
     * @since v1.0.0
     */
    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }
    
}
