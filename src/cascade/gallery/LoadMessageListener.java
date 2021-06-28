/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

/**
 *
 * @author Angel
 */
@FunctionalInterface
public interface LoadMessageListener extends LoadListener {
    
    @Override
    public default void load(float amount){
        this.load(amount, "");
    }
    
    public void load(float amount, String message);
    
}
