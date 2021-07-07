/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Clase que se encarga de controlar un conjunto de archivos de imagenes
 * y de cambiar de manera dinamica y simple ese conjunto
 * 
 * @since v1.0.0
 * @author Angel
 */
public class ImageFilesControl {
    
    /**
     * Verifica si un archivo está en un formato reconocido de imagen
     * 
     * @param file El archivo cuyo formato se va a comprobar
     * 
     * @return Si el archivo está en uno de los formatos de imagenes
     * reconocidos. Los formatos reconocidos son: <code>png</code>,
     * <code>jpg</code> y <code>gif</code>
     * 
     * @since v1.0.0
     */
    public static boolean isImage(File file){
        String nameLower = file.getName().toLowerCase();
        return (nameLower.endsWith(".png") 
                || nameLower.endsWith(".jpg")
                || nameLower.endsWith(".gif"));
    }
    
    /**
     * Verifica que un archivo coincida con uno de los dos posibles requisitos
     * y, de no hacerlo, lanza IOException
     * 
     * @param file El archivo a verificar
     * 
     * @param dir <code>true</code> para verificar que el archivo sea un
     * directorio, <code>false</code> para verificar que sea una imagen
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si no cumplió la condición
     */
    private static void verifyFile(File file, boolean dir) throws IOException{
        boolean verify = dir ? file.isDirectory() : isImage(file);
        if(!verify){
            throw new IOException("El archivo pasado no es un" 
                    + (dir ? " directorio" : "a imagen"));
        }
    }
    
    /**
     * Verifica que el archivo pasado sea un directorio y, de no serlo, lanza
     * IOException
     * 
     * @param file El archivo a verificar
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si el archivo no es un directorio
     */
    private static void verifyDir(File file) throws IOException{
        verifyFile(file, true);
    }
    
    /**
     * Verifica que el archivo pasado sea una imagen y, de no serlo, lanza
     * IOException
     * 
     * @param file El archivo a verificar
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si el archivo no es una imagen
     */
    private static void verifyImage(File file) throws IOException{
        verifyFile(file, false);
    }
    
    /**
     * Función que compara individualmente dos archivos y retorna un valor
     * positivo si <code>a</code> tiene un número de mayor valor o
     * negativo de ser lo contrario.<br>
     * De ninguno tener número, devolverá 0.<br><br>
     * 
     * En una función sort sobre objetos File. Los que no tienen números
     * en su nombre estarán primeros, ordenados del mismo modo que estaban antes.
     * Luego los que tienen números estarán ordenados según sus números.
     * Los números iguales son colocados juntos en el orden que tenían
     * previamente.
     * 
     * @param a El primer archivo a comparar 
     * @param b El segundo archivo a comparar
     * 
     * @return Un número positivo si <code>a</code> tiene un número mayor y
     * un número negativo si <code>b</code> tiene un valor mayor
     * 
     * @since v1.0.0
     */
    public static int numberNameSort(File a, File b){
        String aName = a.getName(), 
                    bName = b.getName();
            
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher mA = p.matcher(aName),
                mB = p.matcher(bName);
        
        boolean aHas = mA.find(), bHas = mB.find();
        
        for(;aHas && bHas; aHas = mA.find(), bHas = mB.find()){
            String numberA = mA.group();
            String numberB = mB.group();
            
            if(numberA.equals(numberB)){
                continue;
            }
            
            int intA = Integer.parseInt(numberA);
            int intB = Integer.parseInt(numberB);
            return intA - intB;
        }
        
        if(aHas || bHas){
            return aHas ? 1 : -1;
        }
        
        return 0;
    }
    
    /**
     * El arreglo de archivos de imagenes del cual se lleva un control
     * 
     * @since v1.0.0
     */
    private File[] files;
    
    private FilesChangeListener listener = null;
    
    /**
     * Inicializa el objeto ImageFilesControl sin imagenes
     * 
     * @since v1.0.0
     */
    public ImageFilesControl(){
        this.files = new File[0];
    }
    
    /**
     * Añade un agente de escucha para los archivos de este objeto.
     * Al cambiar los archivos de este objeto, se activará la
     * función filesChange de listener.<br><br>
     * 
     * En su versión actual, este objeto solo admite un agente de escucha
     * 
     * @param listener El nuevo agente de escucha
     * 
     * @since v1.0.1
     */
    public void setListener(FilesChangeListener listener){
        this.listener = listener;
    }
    
    /**
     * Devuelve el actual agente de escucha de los archivos de este objeto
     * 
     * @return El agente de escucha que está reaccionando a los cambios en
     * loa archivos
     * 
     * @since v1.0.1
     */
    public FilesChangeListener getListener(){
        return listener;
    }
    
    /**
     * Obtiene y manipula las imagenes de un directorio
     * 
     * @param directory El directorio a Manipular
     * 
     * @since v1.0.0
     * 
     * @throws java.io.IOException Si el archivo pasado no es un directorio
     */
    public ImageFilesControl(File directory) throws IOException{
        
        ImageFilesControl.verifyDir(directory);
        
        this.files = directory.listFiles(ImageFilesControl::isImage);
    }
    
    /**
     * Inicializa el objeto con el array de archivos pasados
     * 
     * @param files Un arreglo de archivos a controlar con este objeto
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si alguno de los archivos no está en formato
     * de imagen
     */
    public ImageFilesControl(File[] files) throws IOException{
        
        for(File file : files){
            ImageFilesControl.verifyImage(file);
        }
        
        this.files = files;
    }
    
    private File[] filterAndOrder(File[] input){
        return Stream.of(input)
                .filter(ImageFilesControl::isImage)
                .sorted(ImageFilesControl::numberNameSort)
                .toArray(File[]::new);
    }
    
    /**
     * Añade todas las imagenes en un directorio al arreglo actual de imagenes
     * 
     * @param directory El directorio cuyas imagenes se van a añadir
     * 
     * @return El arreglo con todos los archivos controlados por el objeto
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si el archivo pasado no es un directorio
     */
    public File[] addImages(File directory) throws IOException{
        if(directory == null) return new File[0];
        
        ImageFilesControl.verifyDir(directory);
        
        File[] dirFiles = directory.listFiles(ImageFilesControl::isImage);
        return this.addImages(dirFiles);
    }
    
    /**
     * Añade un conjunto de archivos de imagenes al arreglo de archivos
     * 
     * @param images Las imagenes a añadir
     * 
     * @return Todos los archivos que están siendo controlados actualmente
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si alguno de los archivos no es una imagen
     */
    public File[] addImages(File[] images) throws IOException{
        if(images == null) return setImages(this.files);
        
        File[] concat = Arrays.copyOf(this.files, this.files.length + images.length);
        
        System.arraycopy(images, 0, concat, this.files.length, images.length);
        
        return setImages(concat);
    }
    
    /**
     * Cambia el arreglo actual de archivos de imagenes por las imagenes
     * dentro de un directorio
     * 
     * @param directory El directorio cuyos archivos se desea utilizar
     * 
     * @return Todos los archivos de imagenes en ese directorio
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si el archivo pasado no es un directorio
     */
    public File[] setImages(File directory) throws IOException{
        if (directory == null) return new File[0];
        
        ImageFilesControl.verifyDir(directory);
        
        File[] dirFiles = directory.listFiles(ImageFilesControl::isImage);
        return this.setImages(dirFiles);
    }
    
    /**
     * Cambia el arreglo actual de archivos por el arreglo pasado
     * 
     * @param images El nuevo arreglo de archivos
     * 
     * @return El arreglo de archivos
     * 
     * @since v1.0.0
     * 
     * @throws IOException Si alguno de los archivos no es una imagen
     */
    public File[] setImages(File[] images) throws IOException{
        if(images == null){ 
            this.setFiles(new File[0]);
            return new File[0];
        }
        
        File[] filtered = filterAndOrder(images);
        
        this.setFiles(filtered);
        
        return this.files;
    }
    
    /**
     * Devuelve todos los archivos que actualmente están siendo manipulados por
     * este objeto
     * 
     * @return El arreglo actual de archivos manipulados
     * 
     * @since v1.0.0
     */
    public File[] getImageFiles(){
        return this.files;
    }
    
    /**
     * Función que cambia el arreglo de archivos y activa el listener de
     * ser necesario
     * 
     * @param files El nuevo arreglo de archivos
     * 
     * @since v1.0.1
     */
    private void setFiles(File[] files){
        if(files == this.files) return;
        
        this.files = files;
        if(listener!=null){
            listener.filesChange(files);
        }
    }
    
    /**
     * Comprueba si no hay archivos siendo controlados actualmente
     * 
     * @return Si el arreglo de archivos está vacio
     * 
     * @since v1.0.0
     */
    public boolean isEmpty(){
        return this.files.length == 0;
    }
    
}
