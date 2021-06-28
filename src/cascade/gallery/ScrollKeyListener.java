/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author Angel
 */
public class ScrollKeyListener implements KeyListener {
    
    public JScrollPane scroll;
    
    public ScrollKeyListener(JScrollPane scroll){
        this.scroll = scroll;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JScrollBar bar = scroll.getVerticalScrollBar();
        int increment = 0;
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                increment -= 16;
                break;
            case KeyEvent.VK_DOWN:
                increment += 16;
                break;
            case KeyEvent.VK_SPACE:
                increment += 32;
            default:
                break;
        }
        
        int newValue = bar.getValue() + increment;
        newValue = Math.max(newValue, 0);
        newValue = Math.min(newValue, bar.getMaximum());
        
        bar.setValue(newValue);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
