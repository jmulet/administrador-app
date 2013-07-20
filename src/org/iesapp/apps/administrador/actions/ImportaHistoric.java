/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.apps.administrador.AdministradorGUI;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.apps.administrador.MainFrame;
import org.iesapp.clients.iesdigital.alumnat.Historic;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.HtmlLog;

/**
 *
 * @author Josep
 */
public class ImportaHistoric extends Thread {
    
    private int mode;
    private final Cfg cfg;
    
    public ImportaHistoric(int mode, Cfg cfg)
    {
        this.cfg = cfg;
        this.mode = mode;
    }
        
    @Override
    public void run()
    {       
       AdministradorGUI.htmlLog1.clear();
        
       if(mode ==1 && !MainFrame.confirmacio()) {
            return;
        }
       if(mode == 0) 
       {
           AdministradorGUI.htmlLog1.append("***** MODE SIMULACIO *****",HtmlLog.COMMENTTYPE);
       }
       
               
     
       AdministradorGUI.htmlLog1.append("Important dades d'alumnes històriques...", HtmlLog.TITLETYPE);
       AdministradorGUI.htmlLog1.beginTable("iht1", new String[]{"Expd.", "Alumne/a", "anyAcademic", 
                                "Ensenyament", "Estudis", "Grup", "Tutor/a", "Resultat"}, "body");
    
      
       int nInsert = 0;
       int nUpdate = 0;
       int nerr = 0;
       
        org.iesapp.framework.admin.AssignaTutors at = new org.iesapp.framework.admin.AssignaTutors(AdministradorGUI.sgdAny, AdministradorGUI.htmlLog1, cfg.getCoreCfg());
       AdministradorGUI.jProgressBar1.setValue(50);
       
       HashMap<Integer, Historic> list = at.getHistoricMap();
        //Refina la cerca (tutors sense tutoria)
        list = at.getHistoricMap_refine(list);

       //cerca el mapa entre id-sgd i abrev-apps
        HashMap<Integer, String> mapa = at.getMapaIdAbrev();

        //Elimina les possibles condicions de pertanença
        int aquestAny = cfg.getCoreCfg().anyAcademic;
        
        //en primer lloc borrar tot lo que hi pugi haver d'altres anys
        String SQL3 = "UPDATE `"+CoreCfg.core_mysqlDBPrefix+"`.xes_alumne SET permisos=''";
        AdministradorGUI.htmlLog1.append("Delete pertanença: "+SQL3+"");
        int nup3 = 0;
        if(mode==1) {
            nup3 = cfg.getCoreCfg().getMysql().executeUpdate(SQL3);
        }
        else {
            nup3 = 1;
        }
            
        AdministradorGUI.jProgressBar1.setValue(70);  
          
        //Fa els updates pertinents en la nostra base de dades
        AdministradorGUI.htmlLog1.append("Updates @ xes_alumne_historic: "+SQL3+"");
        for(int i: list.keySet())
        {
            Historic hist = list.get(i);

            String proftutor2 = hist.profTutor.get(0);
            String abrevsProf = "";
            int index = hist.codigoTutor.get(0);
            if( mapa.containsKey(index)) {
                abrevsProf += mapa.get(index);
            }

            for(int k=1; k<hist.profTutor.size();k++)
            {
                proftutor2 += "; "+hist.profTutor.get(k);
                int index2 = hist.codigoTutor.get(k);
                if( mapa.containsKey(index2)) {
                    abrevsProf += ", "+mapa.get(index2);
                }
            }

            String SQL1 = "Select * from `"+CoreCfg.core_mysqlDBPrefix+"`.xes_alumne_historic where Exp2="+hist.exp2 + " AND AnyAcademic='"+hist.anyAcademic+"'";
            String SQL2 ="";
            ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
            Object[] values = null;
            int tipus = 0;
            try {
                if (rs1 != null && rs1.next()) {
                    SQL2 = "UPDATE `"+CoreCfg.core_mysqlDBPrefix+"`.xes_alumne_historic SET ProfTutor=? WHERE Exp2=? AND AnyAcademic=?";
                    values = new Object[]{proftutor2,hist.exp2,hist.anyAcademic};
                    tipus = 1;
                }
                else {
                    SQL2 = "INSERT INTO `"+CoreCfg.core_mysqlDBPrefix+"`.xes_alumne_historic (Exp2, AnyAcademic, Ensenyament, Estudis, Grup, ProfTutor)"
                        + " VALUES (?,?,?,?,?,?)";
                    values = new Object[]{hist.exp2,hist.anyAcademic, hist.ensenyament, hist.estudis, hist.grup, proftutor2};
                    tipus = 2;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ImportaHistoric.class.getName()).log(Level.SEVERE, null, ex);
            }


            int nup = 0;
            if (mode==1) { 
                nup = cfg.getCoreCfg().getMysql().preparedUpdate(SQL2,values);
            }
            else {
                nup  = 0;
            }
            
            String resultat = "";
           
        
            if(nup>0) 
            {
               if(tipus==1)
               {
                   resultat += "UPDATE OK ";
                   nUpdate +=1;
               }
               else if(tipus==2)
               {
                  resultat += "INSERT OK ";
                  nInsert +=1;
               }
           }
           else
           {
               if(tipus==1)
               {
                   resultat += "UPDATE FAILED ";
                
               }
               else if(tipus==2)
               {
                  resultat += "INSERT FAILED ";
               
               }
           }
            


            //ara fa els updates sobre els permisos de pertanença alumne fitxa - professors
            //Atenció això només ho ha de fer si està en el curs actual
                        
            if((aquestAny+"").equals(AdministradorGUI.anyAcademic))
            {
                SQL3 = "UPDATE `"+CoreCfg.core_mysqlDBPrefix+"`.xes_alumne SET permisos='"+abrevsProf+"' WHERE Exp2='"+hist.exp2+"'";
                if(mode==1) { 
                    nup3 = cfg.getCoreCfg().getMysql().executeUpdate(SQL3);
                }
                else {
                    nup3=1;
                }
                
                resultat += "; SET PERMISOS: "+abrevsProf;
            }
            
            AdministradorGUI.htmlLog1.addRowTable(new Object[]{hist.exp2, hist.nomAlumne,
                hist.anyAcademic, hist.ensenyament, hist.estudis, hist.grup, proftutor2, resultat},"iht1");

         }
   
         AdministradorGUI.jProgressBar1.setValue(100);  
          
         AdministradorGUI.htmlLog1.append("S'han actualitzat "+ nUpdate + " entrades.",HtmlLog.SUMMARYTYPE);
         AdministradorGUI.htmlLog1.append("S'han insertat "+ nInsert + " entrades.",HtmlLog.SUMMARYTYPE);
         AdministradorGUI.htmlLog1.append("El procés ha acabat amb "+ nerr + " errors.",HtmlLog.SUMMARYTYPE);
         AdministradorGUI.htmlLog1.append("Done.",HtmlLog.SUMMARYTYPE);
       }
    }
    
 
