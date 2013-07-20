/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.iesapp.apps.administrador;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.modules.fitxes.FitxesGUI;

/**
 *
 * @author Josep
 */
public class Cfg {
    public static final String ADMININI = "config/administrador.ini";
    public String job1="Mai";
    public String job2="Mai";
    public String job3="Mai";
    public String job4="Mai";
    public String ireportPath;
    protected final CoreCfg coreCfg;
    protected final org.iesapp.modules.fitxescore.util.Cfg fitxesCfg;
   
    public Cfg(CoreCfg coreCfg)
    {
        this.coreCfg = coreCfg;
        readIniFile();
        
        //Inicia bases de dades de Fitxes (com a administrador)
        fitxesCfg = new org.iesapp.modules.fitxescore.util.Cfg(coreCfg);
        FitxesGUI.belongs = new ArrayList<Integer>();
 
        
//        FitxesGUI.anyAcademic = CoreCfg.anyAcademic;
//        FitxesGUI.userInfo = new User();
//        FitxesGUI.userInfo.setGrant(User.ADMIN);       
//        FitxesGUI.abrev2prof = Data.mapProf(CoreCfg.mysql, "sig_professorat");
// 
    }


    private void readIniFile() {

        File propfile = new File(CoreCfg.contextRoot+File.separator+Cfg.ADMININI);
        if(!propfile.exists()) {
            saveIni();
        }


        Properties props = new Properties();
        //try retrieve data from file
        try {
              FileInputStream filestream = new FileInputStream(CoreCfg.contextRoot+File.separator+Cfg.ADMININI);
              props.load(filestream);

              ireportPath = props.getProperty("admin.ireportPath");
       
              filestream.close();
            }
            catch(IOException e)
            {
                System.out.println("error"+e);
            }
    }

    public void saveIni() {


           Properties props = new Properties();

            try {
              props.setProperty("admin.ireportPath", "C:\\Program Files (x86)\\Jaspersoft\\iReport-4.7.0\\bin\\ireport.exe");
              FileOutputStream filestream = new FileOutputStream(CoreCfg.contextRoot+File.separator+Cfg.ADMININI);
              props.store(filestream, null);
              filestream.close();

            } catch (IOException ex) {
                Logger.getLogger(Cfg.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

       
  //this can launch both local and remote files (including spaces)
  public static void launchFile(String filePath)
  {
    if(filePath == null || filePath.trim().length() == 0) {
          return;
      }
    if(!Desktop.isDesktopSupported()) {
          return;
      }
    Desktop dt = Desktop.getDesktop();
    try
    {      
       dt.browse(getFileURI(filePath));
    } catch (Exception ex)
    {
       Logger.getLogger(Cfg.class.getName()).log(Level.SEVERE, null, ex);
     }
   }

  //generate uri according to the filePath
  private static URI getFileURI(String filePath)
  {
    
    URI uri = null;
    filePath = filePath.trim();
    if(filePath.indexOf("http") == 0 || filePath.indexOf('\\') == 0)
    {
      if(filePath.indexOf('\\') == 0) {
                filePath = "file:" + filePath;
            }
      try
      {
        filePath = filePath.replaceAll(" ", "%20");
        URL url = new URL(filePath);
        uri = url.toURI();
      } catch (MalformedURLException ex)
      {
         Logger.getLogger(Cfg.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (URISyntaxException ex)
      {
         Logger.getLogger(Cfg.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      File file = new File(filePath);
      uri = file.toURI();
    }
     
    return uri;
  }

    public org.iesapp.modules.fitxescore.util.Cfg getFitxesCfg() {
        return fitxesCfg;
    }

    public CoreCfg getCoreCfg() {
        return coreCfg;
    }
    
}
