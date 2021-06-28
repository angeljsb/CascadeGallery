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
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;

/**
 * Clase que se encarga de controlar toda la interfaz de la aplicación
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public class InterfazManager implements FilesChangeListener, LoadMessageListener {
    
    public static File iconFile = new File("icon.png");
    
    /**
     * Inicializa el scroll por defecto
     * 
     * @param view El componente sobre el cual hacer scroll
     * 
     * @return El objeto JScrollPane ya configurado
     * 
     * @since v1.0.0
     */
    private static JScrollPane createDefaultScrollPane(Component view){
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
        
        this.ventana = this.initFrame();
        
        this.panelPrincipal = new JPanel(new FullLayout());
        
        this.menuManager = new MenuBarManager();
        
        this.ventana.add(this.menuManager.getMainMenuBar());
        this.ventana.add(this.panelPrincipal);
        
        BorderLayout layout = new BorderLayout();
        this.ventana.setLayout(layout);
        layout.addLayoutComponent(this.menuManager.getMainMenuBar(), BorderLayout.NORTH);
        layout.addLayoutComponent(this.panelPrincipal, BorderLayout.CENTER);
        
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
        
        BufferedImage icono = null;
        try{
            icono = ImageIO.read(iconFile);
        }catch(IOException ex){
            System.err.println(ex);
        }
        
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
    public void load(float amount, String message) {
        
    }
    
    @Override
    public void filesChange(File... files){
        if(files.length == 0){
            this.showNotImagesMessage();
            return;
        }
        
        this.showImagesPage();
        
        this.imagesPage.removeAll();
        
        ImageInfo[] images = new ImageInfo[files.length];
        
        for(int i=0; i< files.length; i++){
            images[i] = new ImageInfo(files[i]);
            ImageFixWidth view = new ImageFixWidth();
            images[i].onChange = view::setShowImage;
            this.imagesPage.add(view);
//            BaseFile baseFile = new BaseFile(file);
//            ImageFile imageFile = new ImageFile(baseFile);
//            ImageFileView view = new ImageFileView(imageFile);
//            this.imagesPage.add(view);
        }
        
        this.controller.setImages(images);
        
    }
    
}
