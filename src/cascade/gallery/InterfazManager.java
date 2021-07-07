/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import interfaz.fabrica.FabricaJFrame;
import interfaz.layout.FullLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import simple.file.image.ImageLoader;

/**
 * Clase que se encarga de controlar toda la interfaz de la aplicación
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public class InterfazManager implements FilesChangeListener {
    
    public static File iconFile = new File("icon.png");
    
    /**
     * Cambia el look and feel de los componentes swing al por defecto del
     * sistema operativo
     * 
     * @since v1.0.4
     */
    public static void setLookAndFeelToSystem(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    /**
     * Crea el panel principal con la funcionalidad de drag and drop
     * 
     * @return El panel principal con su layout y funcionalidad
     * 
     * @since v1.0.4
     */
    private JPanel createMainPanel(){
        JPanel panel = new JPanel(new FullLayout());
        
        return panel;
    }
    
    private void addMenuAndPanel(JMenuBar menuBar, JPanel panel){
        this.ventana.add(menuBar);
        this.ventana.add(panel);
        
        BorderLayout layout = new BorderLayout();
        this.ventana.setLayout(layout);
        layout.addLayoutComponent(menuBar, BorderLayout.NORTH);
        layout.addLayoutComponent(panel, BorderLayout.CENTER);
        
    }
    
    /**
     * Inicializa el scroll por defecto
     * 
     * @param view El componente sobre el cual hacer scroll
     * 
     * @return El objeto JScrollPane ya configurado
     * 
     * @since v1.0.0
     */
    private JScrollPane createDefaultScrollPane(Component view){
        JScrollPane scroll = new JScrollPane(view);
        
        scroll.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scroll;
    }
    
    private JFrame ventana;
    private JPanel panelPrincipal;
    
    private JLabel errorPage;
    
    private Component loadingPage;
    
    private ImagesView imagesPage;
    private ImagesController controller;
    private JScrollPane scrollImages;
    
    private JPanel notImagesPage;
    
    private MenuBarManager menuManager;
    
    private JButton[] buttonsSelectFile;
    
    /**
     * Crea el controlador de interfaz
     */
    public InterfazManager(){
        
        setLookAndFeelToSystem();
        
        this.ventana = this.initFrame();
        
        this.panelPrincipal = createMainPanel();
        
        this.menuManager = new MenuBarManager();
        
        this.addMenuAndPanel(this.menuManager.getMainMenuBar(), this.panelPrincipal);
        
        //inciar pagina error
        this.errorPage = new JLabel();
        errorPage.setForeground(Color.RED);
        
        //iniciar pagina de carga
        this.loadingPage = new JLabel("Cargando xD (Soy muy flojo para hacer un spinner)");
        
        //iniciar pagina imagenes
        this.imagesPage = new ImagesView();
        this.scrollImages = createDefaultScrollPane(this.imagesPage);
        this.controller = new ImagesController();
        
        //iniciar pagina por defecto
        this.notImagesPage = new JPanel();
        JPanel container = new JPanel(new GridLayout(0, 1));
        
        JLabel labelNotImages = new JLabel("No hay imagenes mostradas");
        
        JPanel containerButtons = new JPanel();
        JButton botonArchivos = new JButton(MenuBarManager.OPEN_FILES);
        JButton botonDirectory = new JButton(MenuBarManager.OPEN_FOLDER);
        containerButtons.add(botonArchivos);
        containerButtons.add(botonDirectory);
        
        this.buttonsSelectFile = new JButton[]{ botonArchivos,botonDirectory};
        
        container.add(labelNotImages);
        container.add(containerButtons);
        this.notImagesPage.add(container);
        
        controller.setMaxWidth(this.ventana.getWidth());
        
        SizeListener l = new SizeListener((size) -> {
            if(size.width>0){
                controller.setMaxWidth(size.width);
                
                if(this.imagesPage.isAdjustWidth()){
                    controller.setMinWidth(size.width);
                }
            }
        });
        this.imagesPage.addComponentListener(l);
        
        this.ventana.addKeyListener(new ScrollKeyListener(this.scrollImages));
        
        this.scrollImages.getVerticalScrollBar().getModel().addChangeListener((ChangeEvent e) -> {
            int scroll = this.scrollImages.getVerticalScrollBar().getValue();
            Component comp = this.imagesPage.getComponentAt(this.imagesPage.getWidth()/2,scroll);
            int index = this.imagesPage.indexOf(comp);
            if(index < 0) return;
            int min = Math.max(index-10, 0);
            int max = Math.min(index+10, this.imagesPage.getComponentCount());
            this.controller.setRange(min, max);
        });
    }

    /**
     * Devuelve el objeto JFrame que representa la ventana principal de esta
     * aplicación
     * 
     * @return La ventana principal
     * 
     * @since v1.0.0
     */
    public JFrame getVentana() {
        return this.ventana;
    }
    
    /**
     * Devuelve el objeto que controla el menú superior de la aplicación
     * 
     * @return E controlador del menú superior
     * 
     * @since v1.0.0
     */
    public MenuBarManager getMenuManager(){
        return this.menuManager;
    }
    
    /**
     * Devuelve el componente que muetra las imagenes
     * 
     * @return El componente que muestra las imagenes
     * 
     * @since v1.0.0
     */
    public ImagesView getImagesPage(){
        return imagesPage;
    }

    public ImagesController getController() {
        return controller;
    }
    
    /**
     * Inicializa la ventana principal de la aplicación con su respectiva
     * configuración
     * 
     * @return La ventana creada
     * 
     * @since v1.0.0
     */
    private JFrame initFrame(){
        FabricaJFrame fabricaVentana = new FabricaJFrame();
        
        BufferedImage icono = ImageLoader.loadImage(iconFile);
        
        JFrame frame = fabricaVentana.crear("Cascade Gallery", icono);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        return frame;
    }
    
    /**
     * Añade un action listener en común a los botones de la pantalla
     * inicial
     * 
     * @param al El escucha que se activará al presionar uno de los botones
     * 
     * @since v1.0.0
     */
    public void addButtonsEvent(ActionListener al){
        for(JButton button : buttonsSelectFile){
            button.addActionListener(al);
        }
    }
    
    public void setAdjustWidth(boolean adjust){
        int min = adjust ? this.imagesPage.getWidth() : 0;
        
        this.getImagesPage().setAdjustWidth(adjust);
        this.getController().setMinWidth(min);
    }
    
    /**
     * Añade un drop target para el panel principal que envíe los archivos
     * droppeados al controlador de imagenes pasado
     * 
     * @param filesControl Un controlador que controle las imagenes que
     * caigan sobre la ventana
     */
    public void initDnD(ImageFilesControl filesControl){
        
        DragAndDropHandler dndh = new DragAndDropHandler(filesControl);
        DropTarget dropTarget = new DropTarget(this.panelPrincipal, dndh);
        
        this.panelPrincipal.setDropTarget(dropTarget);
        
    }
    
    /**
     * Muestra la pagina por defecto para cuando no hay imagenes añadidas
     * 
     * @since v1.0.0
     */
    public void showNotImagesMessage(){
        this.deleteAndShow(this.notImagesPage);
    }
    
    /**
     * Muestra un error en la interfaz
     * 
     * @param error El error
     * 
     * @since v1.0.0
     */
    public void showErrorMessage(Exception error){
        System.err.println(error.getStackTrace()[0]);
        this.errorPage.setText(error.toString());
        
        this.deleteAndShow(errorPage);
        
    }
    
    public void showLoadPage(){
        this.imagesPage.removeAll();
        System.gc();
        
        this.deleteAndShow(this.loadingPage);
    }
    
    public void setLoadingPage(String text){
        ((JLabel)this.loadingPage).setText(text);
    }
    
    public void showImagesPage(){
        if (this.panelPrincipal.getComponent(0)==this.scrollImages) return;
        this.deleteAndShow(this.scrollImages);
    }
    
    /**
     * Borra toda la información en el panel principal y muestra los componentes
     * que se le añaden
     * 
     * @param components Los nuevos componentes a mostrar
     * 
     * @since v1.0.0
     */
    private void deleteAndShow(Component ...components){
        this.panelPrincipal.removeAll();
        
        for(Component component : components){
            this.panelPrincipal.add(component);
        }
        
        this.panelPrincipal.updateUI();
    }
    
    @Override
    public void filesChange(File... files){
        if(files.length == 0){
            this.ventana.setTitle("Cascade Gallery");
            this.showNotImagesMessage();
            return;
        }
        
        this.ventana.setTitle(createTitle(files));
        
        this.showImagesPage();
        
        this.imagesPage.removeAll();
        
        ImageInfo[] images = new ImageInfo[files.length];
        
        for(int i=0; i< files.length; i++){
            images[i] = new ImageInfo(files[i]);
            ImageFixWidth view = new ImageFixWidth();
            images[i].onChange = view::setShowImage;
            this.imagesPage.add(view);
        }
        
        this.controller.setImages(images);
        this.controller.setRange(0, 10);
        
    }
    
    private String createTitle(File[] files){
        return "Cascade Gallery - " + files[0].getParentFile().getName()
                + " | " + files.length + " imagenes";
    }
    
}
