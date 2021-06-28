/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.event.ActionListener;
import java.util.stream.Stream;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Objeto que controla el menú superior de la aplicación
 * 
 * @since v1.0.0
 * 
 * @author Angel
 */
public class MenuBarManager {
    
    public static final String GENERAL = "General",
            SETTINGS = "Ajustes",
            OPEN_FILES = "Abrir imagenes",
            OPEN_FOLDER = "Abrir carpeta",
            ADD_FILES = "Añadir imagenes",
            ADD_FOLDER = "Añadir carpeta",
            SIZE_SETTING = "Tamaño",
            SEPARATION_SETTINGS = "Separación",
            SETTINGS_SIZE_AUTO = "Auto",
            SETTINGS_SIZE_ADJUST = "Ajustar ancho",
            SETTINGS_SEPARATION_NOT = "Sin separación",
            SETTINGS_SEPARATION_SMALL = "Separadas",
            SETTINGS_SEPARATION_BIG = "Mucha separación";
    
    public JMenuBar menuBar;
    
    public JMenu general, settings;
    
    /**
     * Inicializa el objeto que controla el menú superior
     * 
     * @since v1.0.0
     */
    public MenuBarManager(){
        this.menuBar = new JMenuBar();
        
        this.general = new JMenu(GENERAL);
        JMenuItem openFile = new JMenuItem(OPEN_FILES);
        JMenuItem openFolder = new JMenuItem(OPEN_FOLDER);
        JMenuItem addFile = new JMenuItem(ADD_FILES);
        JMenuItem addFolder = new JMenuItem(ADD_FOLDER);
        general.add(openFile);
        general.add(openFolder);
        general.addSeparator();
        general.add(addFile);
        general.add(addFolder);
        
        this.settings = new JMenu(SETTINGS);
        
        JMenu size = new JMenu(SIZE_SETTING);
        JMenuItem autoSize = new JMenuItem(SETTINGS_SIZE_AUTO);
        JMenuItem fullSize = new JMenuItem(SETTINGS_SIZE_ADJUST); 
        size.add(autoSize);
        size.add(fullSize);
        
        JMenu separation = new JMenu(SEPARATION_SETTINGS);
        JMenuItem separationCero = new JMenuItem(SETTINGS_SEPARATION_NOT);
        JMenuItem separationSmall = new JMenuItem(SETTINGS_SEPARATION_SMALL); 
        JMenuItem separationBig = new JMenuItem(SETTINGS_SEPARATION_BIG); 
        separation.add(separationCero);
        separation.add(separationSmall);
        separation.add(separationBig);
        
        this.settings.add(size);
        this.settings.add(separation);
        
        menuBar.add(general);
        menuBar.add(settings);
    }
    
    /**
     * Devuelve la JMenuBar controlada por este objeto
     * 
     * @return La barra de menú
     * 
     * @since v1.0.0
     */
    public JMenuBar getMainMenuBar(){
        return menuBar;
    }
    
    /**
     * Añade un escucha de evento para todos los items del menú general
     * 
     * @param al El action listener que escuchará los eventos en los
     * items de menú
     * 
     * @since v1.0.0
     */
    public void addGeneralAction(ActionListener al){
        Stream.of(general.getMenuComponents())
                .filter(item -> item instanceof JMenuItem)
                .map(item -> (JMenuItem)item)
                .forEach(menu -> menu.addActionListener(al));
    }
    
    /**
     * Añade un escucha de evento para todos los items del menú de ajustes
     * 
     * @param al El action listener que escuchará los eventos en los
     * items de menú
     * 
     * @since v1.0.0
     */
    public void addSettingsAction(ActionListener al){
        Stream.of(settings.getMenuComponents())
                .filter(item -> item instanceof JMenu)
                .map(item -> (JMenu)item)
                .flatMap(menu -> Stream.of(menu.getMenuComponents()))
                .filter(item -> item instanceof JMenuItem)
                .map(item -> (JMenuItem)item)
                .forEach(i -> i.addActionListener(al));
    }
    
}
