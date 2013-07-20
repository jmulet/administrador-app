/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

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
public class NouCursWiz extends org.iesapp.framework.util.wizard.Wizard {
     
    public NouCursWiz(final Cfg cfg)       
    {
        this.getDialog().setTitle("Creacio d'un nou curs - iesDigital "+CoreCfg.VERSION);
        this.getDialog().setModal(false);
       // this.getDialog().setLocationRelativeTo(null);
        
        WizardPanelDescriptor descriptor1 = new StepDescriptor1(cfg);
        this.registerWizardPanel(StepDescriptor1.IDENTIFIER, "Curs acadèmic", descriptor1);
        
        WizardPanelDescriptor descriptor2 = new StepDescriptor2(cfg);
        this.registerWizardPanel(StepDescriptor2.IDENTIFIER, "Importar professorat", descriptor2);
        
        WizardPanelDescriptor descriptor3= new StepDescriptor3(cfg);
        this.registerWizardPanel(StepDescriptor3.IDENTIFIER, "Importar horaris", descriptor3);
         
        WizardPanelDescriptor descriptor4= new StepDescriptor4(cfg);
        this.registerWizardPanel(StepDescriptor4.IDENTIFIER, "Importar guàrdies", descriptor4);
        
         WizardPanelDescriptor descriptor5= new StepDescriptor5(cfg);
        this.registerWizardPanel(StepDescriptor5.IDENTIFIER, "Importar alumnat", descriptor5);
        
        this.setCurrentPanel(StartupDescriptor1.IDENTIFIER);
 
    }
    
    public static void showQuickHelpDialog(JFrame parent, String txt) {
		// create and configure a text area - fill it with exception text.
		final JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		textArea.setEditable(false);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
		textArea.setText(txt);
		
		// stuff it in a scrollpane with a controlled size.
		JScrollPane scrollPane = new JScrollPane(textArea);		
		scrollPane.setPreferredSize(new Dimension(350, 150));
		
		// pass the scrollpane to the joptionpane.				
		JOptionPane.showMessageDialog(parent, scrollPane, "Ajuda", JOptionPane.INFORMATION_MESSAGE);
	}
}
