/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Stream;

/**
 * Clase principal de la aplicación
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public final class App {
    
    public static final File ERRORS = new File("errors.txt"),
            RESOURCES = new File("resources");

    /**
     * Instancia única de la clase
     */
    private static App instance = null;
    
    /**
     * Redirecciona la salida de errores de la aplicación al archivo 
     * {@code errors.txt}
     * 
     * @since v1.0.1
     */
    public static void redirectOutput(){
        try{
            ERRORS.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(ERRORS)); 
            System.setErr(out);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    /**
     * Función que devuelve una sola instancia de la clase durante la ejecución
     * de la aplicación
     *
     * @param args Los argumentos de la linea de comandos
     * @return Una instancia de la clase
     */
    public static App getInstance(String[] args) {
        if (App.instance == null) {
            App.instance = new App(args);
            App.redirectOutput();
        }
        return App.instance;
    }
    
    /**
     * Controlador de la interfaz de la aplicación. Maneja a gran escala todo
     * lo relacionado a los componentes de la interfaz
     */
    public InterfazManager interfaz;

    /**
     * Controlador de los archivos que llega. Recibe y filtra la entrada de
     * archivos por parte del usuario
     */
    public ImageFilesControl filesControl;

    /**
     * Maneja los FileChooser que permiten al usuario ingresar archivos
     */
    public FileSelector fileSelector;

    /**
     * Constructor de la clase principal de la aplicación, que se encarga
     * de inicializar todos los componentes de la misma y los eventos que
     * la controlan
     * 
     * @since v1.0.0
     */
    private App() {

        this.filesControl = new ImageFilesControl();

        this.interfaz = new InterfazManager();

        this.fileSelector = new FileSelector(this.interfaz.getVentana());

        this.interfaz.getMenuManager().addGeneralAction(this::userSelectFiles);
        this.interfaz.addButtonsEvent(this::userSelectFiles);
        this.interfaz.getMenuManager().addViewAction(this::viewMenu);

        this.interfaz.getMenuManager().addSettingsAction(this::settingsButton);
        this.interfaz.initDnD(this.filesControl);
        
        this.interfaz.showNotImagesMessage();
        
        this.filesControl.setListener(this.interfaz);
    }
    
    /**
     * Constructor de la clase principal de la aplicación, inicializa
     * la aplicación y carga el archivo o archivos pasados por linea
     * de comandos
     * 
     * @since v1.0.4
     */
    private App(String[] paths) {
        this();
        
        if(paths.length==0){
            return;
        }
        
        try{
            File file = new File(paths[0]);
            if(file.isDirectory()){
                this.filesControl.addImages(file);
            }else{
                this.filesControl.addImages(
                        Stream.of(paths)
                            .map(File::new)
                            .filter(f->f.exists())
                            .toArray(File[]::new)
                );
            }
        }catch(IOException ex){
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Gestiona los eventos relacionados a los cambios en la selección de
     * archivos por parte del usuario
     * 
     * @param e El evento enviado por el botón que fue clickeado
     * 
     * @since v1.0.0
     */
    public void userSelectFiles(ActionEvent e) {

        try {

            switch (e.getActionCommand()) {
                case MenuBarManager.ADD_FILES:
                    this.filesControl.addImages(
                            this.fileSelector.selectImages()
                    );
                    break;
                case MenuBarManager.ADD_FOLDER:
                    this.filesControl.addImages(
                            this.fileSelector.selectDirectory()
                    );
                    break;
                case MenuBarManager.OPEN_FILES:
                    File[] open = this.fileSelector.selectImages();
                    this.filesControl.setImages(open);
                    break;
                case MenuBarManager.OPEN_FOLDER:
                    File dir = this.fileSelector.selectDirectory();
                    this.filesControl.setImages(dir);
                    break;
                default:
                    break;
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }

    /**
     * Gestiona los eventos relacionados a cambios en los ajustes de la vista
     * de las imagenes
     * 
     * @param e El evento enviado por el botón que fue clickeado
     * 
     * @since v1.0.0
     */
    public void settingsButton(ActionEvent e) {

        switch (e.getActionCommand()) {
            case MenuBarManager.SETTINGS_SIZE_ADJUST:
                this.interfaz.setAdjustWidth(true);
                break;
            case MenuBarManager.SETTINGS_SIZE_AUTO:
                this.interfaz.setAdjustWidth(false);
                break;
            case MenuBarManager.SETTINGS_SEPARATION_NOT:
                this.interfaz.getImagesPage().setSeparation(0);
                break;
            case MenuBarManager.SETTINGS_SEPARATION_SMALL:
                this.interfaz.getImagesPage().setSeparation(25);
                break;
            case MenuBarManager.SETTINGS_SEPARATION_BIG:
                this.interfaz.getImagesPage().setSeparation(50);
                break;
            default:
                break;
        }
    }
    
    public void viewMenu(ActionEvent e){
        switch (e.getActionCommand()) {
            case MenuBarManager.VIEW_BROWSER:
                BrowserViewManager bvm = new BrowserViewManager();
                bvm.show(this.filesControl.getImageFiles());
                break;
            default:
                break;
        }
    }

}
