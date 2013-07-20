/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class StepDescriptor5 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP5";
    private final Step5 panel2;
    
    public StepDescriptor5(Cfg cfg) {
        panel2= new Step5(cfg);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
    }
    
     @Override
    public void displayingPanel() {
         panel2.updateComponents();
    }
        
   @Override
    public void aboutToHidePanel() {
        
    }
 
    @Override
    public Object getBackPanelDescriptor() {
        return StepDescriptor4.IDENTIFIER;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
         return null;
    }
    
    
}
