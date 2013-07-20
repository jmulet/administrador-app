/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class StepDescriptor3 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP3";
    private final Step3 panel2;
    
    public StepDescriptor3(final Cfg cfg) {
        panel2= new Step3(cfg);
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
        return StepDescriptor2.IDENTIFIER;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
         return StepDescriptor4.IDENTIFIER;
    }
    
    
}
