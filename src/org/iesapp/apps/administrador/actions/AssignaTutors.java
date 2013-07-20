/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions;

import org.iesapp.apps.administrador.AdministradorGUI;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.apps.administrador.MainFrame;
import org.iesapp.framework.util.HtmlLog;

/**
 *
 * @author Josep
 */
public class AssignaTutors extends Thread
{

    int mode;
    private final Cfg cfg;
    public AssignaTutors(int mode, Cfg cfg) {
        this.cfg = cfg;
        this.mode = mode;
    }
    
    @Override
    public void run()
    {
        if(mode==1 && !MainFrame.confirmacio()) {
            return;
        }
        //String host, String db, String user, String pass
        AdministradorGUI.htmlLog1.clear();
        
        //AdministradorGUI.jProgressBar1.setIndeterminate(true);
        
        if(mode == 0) {
            AdministradorGUI.htmlLog1.append("***** MODE SIMULACIO *****", HtmlLog.COMMENTTYPE);
        }
        AdministradorGUI.htmlLog1.append("ASSIGNADOR DE TUTORS", HtmlLog.TITLETYPE);
        
        org.iesapp.framework.admin.AssignaTutors at = new org.iesapp.framework.admin.AssignaTutors(AdministradorGUI.sgdAny, AdministradorGUI.htmlLog1, cfg.getCoreCfg());
        at.assignaTutors(mode);
        //AdministradorGUI.jProgressBar1.setIndeterminate(false);
    }
    
}
