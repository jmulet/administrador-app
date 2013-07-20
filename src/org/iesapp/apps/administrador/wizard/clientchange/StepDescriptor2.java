/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.clientchange;

import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;


public class StepDescriptor2 extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STEP2";
    private final Step2 panel2;
    
    public StepDescriptor2(final Cfg cfg) {
        panel2= new Step2(cfg);
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
        return StepDescriptor1.IDENTIFIER;
 
    }  

    @Override
     public Object getNextPanelDescriptor() {
          return null;
    }
    
    
}
