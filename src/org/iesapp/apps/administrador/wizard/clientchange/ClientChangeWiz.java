/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.clientchange;

import org.iesapp.apps.administrador.wizard.noucurs.*;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.start.*;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.wizard.WizardPanelDescriptor;

/**
 *
 * @author Josep
 */
public class ClientChangeWiz extends org.iesapp.framework.util.wizard.Wizard {
     
    public ClientChangeWiz(final Cfg cfg)       
    {
        this.getDialog().setTitle("Clients updater manager");
        this.getDialog().setModal(false);
       // this.getDialog().setLocationRelativeTo(null);
        
        WizardPanelDescriptor descriptor1 = new StepDescriptor1(cfg);
        this.registerWizardPanel(StepDescriptor1.IDENTIFIER, "Select client type", descriptor1);
        
        WizardPanelDescriptor descriptor2 = new StepDescriptor2(cfg);
        this.registerWizardPanel(StepDescriptor2.IDENTIFIER, "Choose client", descriptor2);
        
        WizardPanelDescriptor descriptor3= new StepDescriptor3(cfg);
        this.registerWizardPanel(StepDescriptor3.IDENTIFIER, "Check result", descriptor3);
         
        WizardPanelDescriptor descriptor4= new StepDescriptor4(cfg);
        this.registerWizardPanel(StepDescriptor4.IDENTIFIER, "Fix result", descriptor4);
        
         WizardPanelDescriptor descriptor5= new StepDescriptor5(cfg);
        this.registerWizardPanel(StepDescriptor5.IDENTIFIER, "Done", descriptor5);
        
        this.setCurrentPanel(StartupDescriptor1.IDENTIFIER);
 
    }
    
    
}
