/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class StepDescriptor1 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP1";
    private final Step1 panel2;
    
    public StepDescriptor1(final Cfg cfg) {
       panel2= new Step1(cfg);
    
       panel2.registerListenerRadioButtons(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               panel2.updateComponents();
            }
        });
        
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
    }
    
     @Override
    public void displayingPanel() {
       panel2.onShow();
    }
        
   @Override
    public void aboutToHidePanel() {
       panel2.doSave();
       
    }
 
    @Override
    public Object getBackPanelDescriptor() {
       return null;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
          return StepDescriptor2.IDENTIFIER;
    }
    
    
}
