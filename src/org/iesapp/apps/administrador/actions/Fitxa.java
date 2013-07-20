/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.modules.fitxes.dialogs.FitxaAlumne;

/**
 *
 * @author Josep
 */
public class Fitxa extends JDesktopPane {
    private final Cfg cfg;
 
    public Fitxa(int exp2, Cfg cfg)
    {
        this.cfg = cfg;
        addFitxa(exp2);
       
    }

    public void addFitxa(int exp2) {
        if(!org.iesapp.modules.fitxes.FitxesGUI.belongs.contains(exp2)) {
            org.iesapp.modules.fitxes.FitxesGUI.belongs.add(exp2);
        }
        
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(exp2);
        FitxaAlumne dlg = new FitxaAlumne(null,true,exp2,list,cfg.getFitxesCfg());  
        dlg.setVisible(true);       
        dlg.setClosable(true);
        dlg.setMaximizable(true);
         
        try {
            dlg.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Fitxa.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.add(dlg);
    }
    
}
