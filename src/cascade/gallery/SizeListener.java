/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.util.function.Consumer;

/**
 *
 * @author Angel
 */
public class SizeListener extends ComponentAdapter {
    
    private Consumer<Dimension> action;
    private Thread await = null;
    
    public SizeListener(Consumer<Dimension> action){
        this.action = action;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if(await!=null && await.isAlive()){
            await.interrupt();
        }
        
        await = new Thread(()->{ this.await(e.getComponent().getSize()); });
        await.start();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        this.action.accept(e.getComponent().getSize());
    }
    
    public void await(Dimension size){
        try{
            Thread.sleep(1000);
        }catch(InterruptedException ex){
            return;
        }
        
        this.action.accept(size);
    }
    
}
