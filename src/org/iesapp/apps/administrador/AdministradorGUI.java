/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Sync_sgdGUI.java
 *
 * Created on 29-jun-2011, 15:19:47
 */

package org.iesapp.apps.administrador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.iesapp.apps.administrador.actions.AssignaTutors;
import org.iesapp.apps.administrador.actions.CanviAnyAcademic;
import org.iesapp.apps.administrador.actions.DlgFestius;
import org.iesapp.apps.administrador.actions.ImportaHistoric;
import org.iesapp.apps.administrador.actions.SGDImportationIdentifiers;
import org.iesapp.apps.administrador.bimodules.AvaluacionsManager;
import org.iesapp.apps.administrador.bimodules.GestioEspais;
import org.iesapp.apps.administrador.bimodules.GestioUsuaris;
import org.iesapp.apps.administrador.bimodules.GestionaAlumnes;
import org.iesapp.apps.administrador.bimodules.GrantManager;
import org.iesapp.apps.administrador.bimodules.LogModule;
import org.iesapp.apps.administrador.bimodules.MysqlConModule;
import org.iesapp.apps.administrador.bimodules.RulesManager;
import org.iesapp.apps.administrador.bimodules.ScriptsManager;
import org.iesapp.apps.administrador.bimodules.UsersControlModule;
import org.iesapp.apps.administrador.jasperreports.JasperCompileModule;
import org.iesapp.apps.administrador.wizard.clientchange.ClientChangeWiz;
import org.iesapp.apps.administrador.wizard.noucurs.NouCursWiz;
import org.iesapp.database.MyConnectionBean;
import org.iesapp.database.MyDatabase;
import org.iesapp.framework.admin.cfg.SgdConfig;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.framework.util.JarClassLoader;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class AdministradorGUI extends org.iesapp.framework.pluggable.DockingFrameworkApp {
    public static String anyAcademic;
    public static String sgdAny;
    private DefaultComboBoxModel modelCombo1;
    //public final JRTabbedPane jrTabbedPane1;
    public static  JProgressBar jProgressBar1;
    private String logWindowId;
    private Object comp;
    private final Cfg cfg;
    
 

    /** Creates new form Sync_sgdGUI */
    public AdministradorGUI(String[] args) {
       super(args);
       this.administrativeApp = true;
       this.appDescription = "Administrador de l'entorn iesDigital";
       this.appDisplayName = "Administrador";
       this.appNameId = "administrador";
       initComponents();
       
       this.appClass = getClass();
       //This app has no required module
       this.requiredJar = null;
       this.requiredModuleName = null;
       initializeFramework();
       
       //we must manually include help for administrador
       includeAdminHelpSet();
      
       
       this.enableLaunchAdministrador(false);
       beforeMenu.add(jMenuConfig);
       beforeMenu.add(jMenuSim);
       beforeMenu.add(jMenuGestio);
       beforeMenu.add(jMenuTools);
       beforeMenu.add(jMenuWizards);
       
       //Initialize core
       cfg = new Cfg(coreCfg);
       mainFrame1.startUp(cfg);
       
        
       JMenuItem menuItem = new JMenuItem("Identificadors d'importació SGD");
       menuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                SGDImportationIdentifiers dlg = new SGDImportationIdentifiers(javar.JRDialog.getActiveFrame(), true, coreCfg);
                dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
            }
        }
               );
       jMenu7.add(menuItem);
    }

    
    
    @Override
    public void onSwitchUserValid() {
    
    SwingUtilities.updateComponentTreeUI(xjMenuBar1);
        
    jProgressBar1 = new JProgressBar();
    LogModule logmodule = new LogModule(jPanel3, jProgressBar1, jToolBarAny, cfg);
    logWindowId = uiFramework.addTopModuleWindow(logmodule, false, false);
    
    this.setIconImage(new ImageIcon(getClass().getResource("/org/iesapp/apps/administrador/icons/iconapp.gif")).getImage());
    this.setTitle("Administrador de l'iesDigital "+CoreCfg.VERSION);   
    jLinkButton1.setText("Sou al curs "+coreCfg.anyAcademic+"-"+(coreCfg.anyAcademic+1));
    boolean q = MyDatabase.tryConnection(CoreCfg.coreDB_sgdHost, "",
                CoreCfg.coreDB_sgdUser, CoreCfg.coreDB_sgdPasswd, CoreCfg.coreDB_sgdParam, true);


     if(q)
     {
         this.DoConnect();
         //System.out.println("Connected");
     }
     else
     {
              SgdConfig dlg = new SgdConfig(this, true, coreCfg);
              dlg.setLocationRelativeTo(null);
              dlg.setVisible(true);
              if(dlg.Ok)
              {
                  this.DoConnect();
              }
              else
              {
                //System.out.println("Not Connected");
                  coreCfg.close(); 
                  System.exit(0);
              }

     }

      
     modelCombo1 = new DefaultComboBoxModel();

     String SQL1 = "Show databases";
     int n = 0;
     String sgdprefix = StringUtils.noNull( (String) CoreCfg.configTableMap.get("sgdDBPrefix")  );
     
     try {
         ResultSet rs1 =  coreCfg.getSgd().getResultSet(SQL1);
            while (rs1 != null && rs1.next()) {
                String db = rs1.getString(1);
                if (db.contains(sgdprefix) && !db.contains("tmp")) {
                    modelCombo1.addElement(db);
                    n +=1;
                }
            }
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

     jComboBox1.setModel(modelCombo1);
     
     if(n>0) 
     {
         jComboBox1.setSelectedItem((String) CoreCfg.configTableMap.get("sgdDBPrefix")+coreCfg.anyAcademic);
         sgdAny = StringUtils.AfterLast( (String) jComboBox1.getSelectedItem(),(String) CoreCfg.configTableMap.get("sgdDBPrefix"));
     }
     DoConnect();
   
     //This application has no required modules
     loadModules();
     
     //SwingUtilities.updateComponentTreeUI(this);
     Component pointer = null;
     for(Component compt: jToolBar1.getComponents())
     {
         //System.out.println("Component's name->"+compt.getName());
         if(compt.getName()!=null && compt.getName().equals("jToolBarModules"))
         {
             pointer = compt;
             break;
         }
     }
     if(pointer!=null)
     {
         //Creo els elements aqui perque tinguin el look and feel que toca      
         JButton jButtonRules = new javax.swing.JButton();
         jButtonRules.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/rules.gif")));  
         jButtonRules.setToolTipText("Rules");
         jButtonRules.setFocusable(false);
         jButtonRules.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                uiFramework.addTopModuleWindow(new RulesManager(cfg), true, false);  
             }
          });

          //Creo els elements aqui perque tinguin el look and feel que toca      
         JButton jButtonCheckDB = new javax.swing.JButton();
         jButtonCheckDB.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/checkDB.gif")));  
         jButtonCheckDB.setToolTipText("Comprova noves incorporacions");
         jButtonCheckDB.setFocusable(false);
         jButtonCheckDB.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
               uiFramework.setSelectedTopWindow(logWindowId);
               mainFrame1.checkCorrespondencia();  
             }
          });
    
         JButton jButtonUsers = new javax.swing.JButton();
         jButtonUsers.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/professor.gif")));  
         jButtonUsers.setToolTipText("Users");
         jButtonUsers.setFocusable(false);
         jButtonUsers.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
               GestioUsuaris win = new GestioUsuaris(cfg);
               uiFramework.addTopModuleWindow(win, true, false);  
             }
          });
       
         
         JButton jButtonRoles = new javax.swing.JButton();
         jButtonRoles.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/roles.gif")));  
         jButtonRoles.setToolTipText("Roles");
         jButtonRoles.setFocusable(false);
         jButtonRoles.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 GrantManager grantManager = new GrantManager(cfg);
                 uiFramework.addTopModuleWindow(grantManager, true, false);
             }
          });
      
         JButton jButtonAlumnes = new javax.swing.JButton();
         jButtonAlumnes.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/estudiant.gif")));  
         jButtonAlumnes.setToolTipText("Students");
         jButtonAlumnes.setFocusable(false);
         jButtonAlumnes.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 GestionaAlumnes panel = new GestionaAlumnes(AdministradorGUI.this, cfg);       
                 uiFramework.addTopModuleWindow(panel, true, false);
             }
          });
         
         JButton jButtonAval = new javax.swing.JButton();
         jButtonAval.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/avaluacions.gif")));  
         jButtonAval.setToolTipText("Avaluacions");
         jButtonAval.setFocusable(false);
         jButtonAval.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 AvaluacionsManager module = new AvaluacionsManager();
                 module.setInitializationObject(cfg);
                 module.initialize(stamper, coreCfg, uiFramework);
                 uiFramework.addTopModuleWindow(module, true, false);
             }
          });
  
         JButton jButtonJasper = new javax.swing.JButton();
         jButtonJasper.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/jasperreports.gif")));  
         jButtonJasper.setToolTipText("Compilador Jasper");
         jButtonJasper.setFocusable(false);
         jButtonJasper.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                JasperCompileModule win = new JasperCompileModule();
                uiFramework.addTopModuleWindow(win, true, false);
             }
          });
         
         JButton jButtonJScript = new javax.swing.JButton();
         jButtonJScript.setIcon(new javax.swing.ImageIcon(
                 getClass().getResource("/org/iesapp/apps/administrador/icons/js.gif")));  
         jButtonJScript.setToolTipText("JS Script Manager");
         jButtonJScript.setFocusable(false);
         jButtonJScript.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 
                uiFramework.addTopModuleWindow(new ScriptsManager(), true, false);
             }
          });
         
          ((JToolBar) pointer).add(jButtonRules);
          ((JToolBar) pointer).add(jButtonCheckDB);
          ((JToolBar) pointer).add(jButtonUsers);
          ((JToolBar) pointer).add(jButtonRoles);
          ((JToolBar) pointer).add(jButtonAlumnes);
          ((JToolBar) pointer).add(jButtonAval);
          ((JToolBar) pointer).add(jButtonJasper);
          ((JToolBar) pointer).add(jButtonJScript);
          
         ((JToolBar) pointer).revalidate();
     }
              
      //jToolBar1.add(jToolBarBI);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        mainFrame1 = new org.iesapp.apps.administrador.MainFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        htmlLog1 = new org.iesapp.framework.util.HtmlLog();
        jPanel2 = new javax.swing.JPanel();
        jToolBarAny = new javax.swing.JToolBar();
        jLinkButton1 = new com.l2fprod.common.swing.JLinkButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        xjMenuBar1 = new javax.swing.JMenuBar();
        jMenuConfig = new javax.swing.JMenu();
        jMenuItem34 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuSim = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuGestio = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuWizards = new javax.swing.JMenu();
        jMenuItemNouCurs = new javax.swing.JMenuItem();
        jMenuItemChangeClients = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();

        jButton1.setText("Esborra Log");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(htmlLog1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(mainFrame1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        jToolBarAny.setFloatable(false);
        jToolBarAny.setRollover(true);
        jToolBarAny.setOpaque(false);

        jLinkButton1.setText("Sou al curs");
        jLinkButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLinkButton1ActionPerformed(evt);
            }
        });
        jToolBarAny.add(jLinkButton1);
        jToolBarAny.add(filler1);

        jLabel1.setText("Base SGD d'on importar");
        jToolBarAny.add(jLabel1);

        jComboBox1.setOpaque(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBarAny.add(jComboBox1);

        jMenuConfig.setText("Configuracio");
        jMenuConfig.setName("jMenuConfig"); // NOI18N

        jMenuItem34.setText("Canvi d'any academic");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenuConfig.add(jMenuItem34);
        jMenuConfig.add(jSeparator9);

        jMenuItem25.setText("Conexió a iesDigital");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenuConfig.add(jMenuItem25);

        jMenuItem14.setText("Conexió a SGD");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenuConfig.add(jMenuItem14);

        jMenuItem8.setText("Global configuration parameters");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenuConfig.add(jMenuItem8);

        xjMenuBar1.add(jMenuConfig);

        jMenuSim.setText("Simulacions");
        jMenuSim.setName("jMenuSim"); // NOI18N
        jMenuSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSimActionPerformed(evt);
            }
        });

        jMenu4.setText("Importa des de SGD7");

        jMenuItem2.setText("Importa id's del professorat sistema SGD");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseExited(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setText("Importa contrasenyes del professorat");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseExited(evt);
            }
        });
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);
        jMenu4.add(jSeparator1);

        jMenuItem4.setText("Importa dies festius");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem4MouseExited(evt);
            }
        });
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);
        jMenu4.add(jSeparator2);

        jMenuItem7.setText("Assigna tutors");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem7MouseExited(evt);
            }
        });
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem5.setText("Importa curs, grup, tutor, alumne, permisos");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem5MouseExited(evt);
            }
        });
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuSim.add(jMenu4);

        jMenuItem6.setText("Canvis de grup i noves incorporacions");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem6MouseExited(evt);
            }
        });
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenuSim.add(jMenuItem6);

        xjMenuBar1.add(jMenuSim);

        jMenuGestio.setText("Gestió");
        jMenuGestio.setName("jMenuGestio"); // NOI18N
        jMenuGestio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuGestioActionPerformed(evt);
            }
        });

        jMenu7.setText("Importa des de SGD7");

        jMenuItem9.setText("Importa id's del professorat sistema SGD");
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem9MouseExited(evt);
            }
        });
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem9);

        jMenuItem10.setText("Importa contrasenyes del professorat");
        jMenuItem10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem10MouseExited(evt);
            }
        });
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem10);
        jMenu7.add(jSeparator3);

        jMenuItem11.setText("Importa dies festius");
        jMenuItem11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem11MouseExited(evt);
            }
        });
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem11);
        jMenu7.add(jSeparator4);

        jMenuItem12.setText("Assignació Alumne - Tutor");
        jMenuItem12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem12MouseExited(evt);
            }
        });
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem12);

        jMenuItem13.setText("Importa Històric");
        jMenuItem13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuItem13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuItem13MouseExited(evt);
            }
        });
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem13);

        jMenuGestio.add(jMenu7);

        jMenu10.setText("Alumnes");

        jMenuItem26.setText("Gestiona alumnes");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem26);
        jMenu10.add(jSeparator6);

        jMenuItem17.setText("Importa alumnes de XESTIB2.0");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem17);

        jMenuItem18.setText("Importa fotos d'alumnes");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem18);

        jMenuItem33.setText("Genera contrasenyes d'alumnes");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem33);

        jMenuGestio.add(jMenu10);

        jMenu11.setText("Professors / Usuaris");

        jMenuItem19.setText("Crea un nou usuari");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem19);

        jMenuItem20.setText("Edita usuaris");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem20);
        jMenu11.add(jSeparator5);

        jMenuItem21.setText("Edita roles");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem21);

        jMenuGestio.add(jMenu11);

        jMenu12.setText("Espais");

        jMenuItem22.setText("Gestiona");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem22);

        jMenuGestio.add(jMenu12);

        jMenu14.setText("Dates");

        jMenuItem28.setText("Festius");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem28);

        jMenuItem29.setText("Avaluacions");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem29);

        jMenuGestio.add(jMenu14);

        jMenuItem36.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem36.setText("Rules");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenuGestio.add(jMenuItem36);

        jMenu5.setText("Informes JASPER");

        jMenuItem27.setText("Compilador");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuGestio.add(jMenu5);
        jMenuGestio.add(jSeparator7);

        jMenu13.setText("Contrasenyes");

        jMenuItem23.setText("Administrador");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem23);

        jMenuItem24.setText("Usuaris interns");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem24);

        jMenuGestio.add(jMenu13);

        xjMenuBar1.add(jMenuGestio);

        jMenuWizards.setText("Assistents");
        jMenuWizards.setName("jMenuWizards"); // NOI18N

        jMenuItemNouCurs.setText("Creació d'un nou curs");
        jMenuItemNouCurs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNouCursActionPerformed(evt);
            }
        });
        jMenuWizards.add(jMenuItemNouCurs);

        jMenuItemChangeClients.setText("Canvia clients");
        jMenuItemChangeClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChangeClientsActionPerformed(evt);
            }
        });
        jMenuWizards.add(jMenuItemChangeClients);

        xjMenuBar1.add(jMenuWizards);

        jMenuTools.setText("Eines");
        jMenuTools.setName("jMenuTools"); // NOI18N

        jMenuItem15.setText("MySQL browser - Fitxes -");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItem15);

        jMenuItem32.setText("MySQL browser - SGD -");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItem32);
        jMenuTools.add(jSeparator10);

        jMenuItem35.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem35.setText("Users Control Center");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItem35);

        jMenuItem1.setText("Scripts manager");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItem1);

        xjMenuBar1.add(jMenuTools);

        jPanel1.setLayout(new java.awt.BorderLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administrador de l'iesDigital");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        sgdAny = StringUtils.AfterLast( (String) jComboBox1.getSelectedItem(),"curso");
        CoreCfg.coreDB_sgdDB = (String) jComboBox1.getSelectedItem();
        DoConnect();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    
    //Comprova canvies de grup i noves incorporacions
    private void jMenuSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSimActionPerformed
       
    }//GEN-LAST:event_jMenuSimActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.checkCorrespondencia();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaIDs(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaPwds(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaFestius(0);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        (new ImportaHistoric(0, cfg)).start();
        
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        (new AssignaTutors(0, cfg)).start(); //0=simulacio
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
       uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaIDs(1);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaPwds(1);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
       uiFramework.setSelectedTopWindow(logWindowId);
        mainFrame1.importaFestius(1);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        (new AssignaTutors(1, cfg)).start(); 
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        uiFramework.setSelectedTopWindow(logWindowId);
        (new ImportaHistoric(1, cfg)).start();      
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MouseEntered
        mainFrame1.setDescription(MainFrame.IMPORTAIDS);
    }//GEN-LAST:event_jMenuItem9MouseEntered

    private void jMenuItem9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MouseExited
        mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem9MouseExited

    private void jMenuItem9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MouseClicked
 
    }//GEN-LAST:event_jMenuItem9MouseClicked

    private void jMenuItem10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem10MouseEntered
       mainFrame1.setDescription(MainFrame.IMPORTAPWDS);
    }//GEN-LAST:event_jMenuItem10MouseEntered

    private void jMenuItem10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem10MouseExited
       mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem10MouseExited

    private void jMenuItem11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MouseEntered
        mainFrame1.setDescription(MainFrame.IMPORTAFESTIUS);
    }//GEN-LAST:event_jMenuItem11MouseEntered

    private void jMenuItem11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MouseExited
        mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem11MouseExited

    private void jMenuItem12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem12MouseEntered
        mainFrame1.setDescription(MainFrame.ASSIGNATUTORS);
    }//GEN-LAST:event_jMenuItem12MouseEntered

    private void jMenuItem12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem12MouseExited
       mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem12MouseExited

    private void jMenuItem13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem13MouseEntered
        mainFrame1.setDescription(MainFrame.IMPORTAHISTORIC);
    }//GEN-LAST:event_jMenuItem13MouseEntered

    private void jMenuItem13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem13MouseExited
        mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem13MouseExited

    private void jMenuItem6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MouseEntered
         mainFrame1.setDescription(MainFrame.CANVIGRUP);
    }//GEN-LAST:event_jMenuItem6MouseEntered

    private void jMenuItem6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MouseExited
        mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem6MouseExited

    private void jMenuItem2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseEntered
       mainFrame1.setDescription(MainFrame.IMPORTAIDS);
    }//GEN-LAST:event_jMenuItem2MouseEntered

    private void jMenuItem2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseExited
       mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem2MouseExited

    private void jMenuItem3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseEntered
         mainFrame1.setDescription(MainFrame.IMPORTAPWDS);
    }//GEN-LAST:event_jMenuItem3MouseEntered

    private void jMenuItem3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseExited
        mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem3MouseExited

    private void jMenuItem4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MouseEntered
         mainFrame1.setDescription(MainFrame.IMPORTAFESTIUS);
    }//GEN-LAST:event_jMenuItem4MouseEntered

    private void jMenuItem4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MouseExited
         mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem4MouseExited

    private void jMenuItem7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MouseEntered
         mainFrame1.setDescription(MainFrame.ASSIGNATUTORS);
    }//GEN-LAST:event_jMenuItem7MouseEntered

    private void jMenuItem7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MouseExited
         mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem7MouseExited

    private void jMenuItem5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MouseEntered
         mainFrame1.setDescription(MainFrame.IMPORTAHISTORIC);
    }//GEN-LAST:event_jMenuItem5MouseEntered

    private void jMenuItem5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MouseExited
         mainFrame1.setDescription(MainFrame.LASTDESCRIPTION);
    }//GEN-LAST:event_jMenuItem5MouseExited

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        org.iesapp.framework.admin.cfg.SgdConfig dlg = new org.iesapp.framework.admin.cfg.SgdConfig(this, true, coreCfg);
        dlg.setPassword(CoreCfg.coreDB_sgdPasswd);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
           MysqlConModule module = new MysqlConModule();
           module.setConBean(new MyConnectionBean(CoreCfg.core_mysqlHost, CoreCfg.core_mysqlDB, CoreCfg.core_mysqlUser, CoreCfg.core_mysqlPasswd, CoreCfg.coreDB_sgdParam));
           uiFramework.addTopModuleWindow(module, true, false);     
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItemNouCursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNouCursActionPerformed
        NouCursWiz wizard = new NouCursWiz(cfg);
        wizard.getDialog().pack();
        wizard.getDialog().setLocationRelativeTo(null);
        wizard.getDialog().setVisible(true);
        jLinkButton1.setText("Sou al curs "+coreCfg.anyAcademic+"-"+(coreCfg.anyAcademic+1));
    }//GEN-LAST:event_jMenuItemNouCursActionPerformed

    private void jMenuGestioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuGestioActionPerformed
 
    }//GEN-LAST:event_jMenuGestioActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
     
        org.iesapp.modules.fitxes.dialogs.admin.ImportarAlumnes dlg = new org.iesapp.modules.fitxes.dialogs.admin.ImportarAlumnes(this,true,cfg.getCoreCfg().anyAcademic, cfg.getFitxesCfg());
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        org.iesapp.framework.admin.ImportarFotos dlg = new org.iesapp.framework.admin.ImportarFotos(this,true, coreCfg);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
         org.iesapp.framework.admin.cfg.ChangePwd dlg = new org.iesapp.framework.admin.cfg.ChangePwd(this,true, coreCfg);
         dlg.setLocationRelativeTo(null);
         dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        org.iesapp.framework.admin.cfg.DlgConfiguration dlg = new org.iesapp.framework.admin.cfg.DlgConfiguration(this, true);
        dlg.setPassword(CoreCfg.coreDB_sgdPasswd);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        org.iesapp.framework.admin.cfg.ChangePwdIU dlg = new org.iesapp.framework.admin.cfg.ChangePwdIU(this, true, coreCfg);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        org.iesapp.framework.admin.cfg.DlgCreaUsuari dlg = new org.iesapp.framework.admin.cfg.DlgCreaUsuari(this, true, coreCfg);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        GestioUsuaris win = new GestioUsuaris(cfg);
        uiFramework.addTopModuleWindow(win, true, false);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    //Edicio de grups de roles
    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
     
//        DBEditorModule tableEditor = new DBEditorModule("Roles editor");
//        tableEditor.setTable(CoreCfg.mysql, CoreCfg.core_mysqlDB, "fitxa_permisos");
        GrantManager grantManager = new GrantManager(cfg);
        uiFramework.addTopModuleWindow(grantManager, true, false);
    
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        htmlLog1.clear();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
       
        GestionaAlumnes panel = new GestionaAlumnes(this,cfg);       
        uiFramework.addTopModuleWindow(panel, true, false);
       
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
       JasperCompileModule win = new JasperCompileModule();
       uiFramework.addTopModuleWindow(win, true, false);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
       GestioEspais win = new GestioEspais(cfg);
       uiFramework.addTopModuleWindow(win, true, false);        
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
       DlgFestius dlg = new DlgFestius(this,true,cfg);
       dlg.setLocationRelativeTo(null);
       dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
       
        AvaluacionsManager module = new AvaluacionsManager();
        module.setInitializationObject(cfg);
        module.initialize(stamper, coreCfg, uiFramework);
        uiFramework.addTopModuleWindow(module, true, false);
        
//       DlgAvaluacions dlg = new DlgAvaluacions(this,true);
//       dlg.setLocationRelativeTo(null);
//       dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    //Browser sgd
    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
           MysqlConModule module = new MysqlConModule();
           module.setConBean(new MyConnectionBean(CoreCfg.coreDB_sgdHost, CoreCfg.coreDB_sgdDB, CoreCfg.coreDB_sgdUser, CoreCfg.coreDB_sgdPasswd, CoreCfg.coreDB_sgdParam));
           uiFramework.addTopModuleWindow(module, true, false);            
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
       org.iesapp.framework.admin.GenContrassenyes dlg = new  org.iesapp.framework.admin.GenContrassenyes(this,true,""+coreCfg.anyAcademic, coreCfg);
       dlg.setLocationRelativeTo(null);
       dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        CanviAnyAcademic dlg = new CanviAnyAcademic(this,true,cfg);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
        jLinkButton1.setText("Sou al curs "+coreCfg.anyAcademic+"-"+(coreCfg.anyAcademic+1));
        jComboBox1.setSelectedItem(CoreCfg.coreDB_sgdDB);
        
        //Should close all
        uiFramework.closeAll();
        LogModule logmodule = new LogModule(jPanel3, jProgressBar1, jToolBarAny, cfg);
        logWindowId = uiFramework.addTopModuleWindow(logmodule, false, false);
    
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jLinkButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLinkButton1ActionPerformed
        int oldAny = coreCfg.anyAcademic;
        CanviAnyAcademic dlg = new CanviAnyAcademic(this,true,cfg);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
        if(dlg.getExitCode()==CanviAnyAcademic.ACCEPT && coreCfg.anyAcademic!=oldAny)
        {
            jLinkButton1.setText("Sou al curs "+coreCfg.anyAcademic+"-"+(coreCfg.anyAcademic+1));
            jComboBox1.setSelectedItem(CoreCfg.coreDB_sgdDB);
        
            //Should close all
             uiFramework.closeAll();
             LogModule logmodule = new LogModule(jPanel3, jProgressBar1, jToolBarAny,cfg);
             logWindowId = uiFramework.addTopModuleWindow(logmodule, false, false); 
        }
        dlg.dispose();
    }//GEN-LAST:event_jLinkButton1ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
         UsersControlModule module = new UsersControlModule(cfg);
         uiFramework.addTopModuleWindow(module, true, false);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        uiFramework.addTopModuleWindow(new RulesManager(cfg), true, false);         
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
          uiFramework.addTopModuleWindow(new ScriptsManager(), true, false); 
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    //Permet editar configTableDB
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        
        org.iesapp.apps.mysqlbrowser.DBEditorDlg dlg = new org.iesapp.apps.mysqlbrowser.DBEditorDlg(javar.JRDialog.getActiveFrame(), true, "Global configuration parameters", cfg.getCoreCfg().getMysql(), CoreCfg.core_mysqlDBPrefix, "configuracio");
        dlg.getDBEditorPanel().enableAddRow(true);
        dlg.getDBEditorPanel().enableDeleteRow(true);
        dlg.getDBEditorPanel().enableMoreOperations(false);
        dlg.getDBEditorPanel().enableImportXls(false);
        dlg.getDBEditorPanel().setEditability(new boolean[]{false,false,false,true,false,false});
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItemChangeClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChangeClientsActionPerformed
        ClientChangeWiz wiz = new ClientChangeWiz(cfg);
        wiz.getDialog().setLocationRelativeTo(null);
        wiz.showModalDialog();  
    }//GEN-LAST:event_jMenuItemChangeClientsActionPerformed

    
    private void DoConnect()
    {
       int id = jComboBox1.getSelectedIndex();
       String dbName = (String)jComboBox1.getSelectedItem();
       
       anyAcademic = StringUtils.AfterLast(dbName, "o");
      // //System.out.println("intentat"+anyAcademic+";"+dbName);

       if(id>=0)
       {
          int setCatalog = coreCfg.getSgd().setCatalog(dbName);
          if(setCatalog<=0) 
          {
              SgdConfig dlg = new SgdConfig(this,true, coreCfg);
              dlg.setLocationRelativeTo(null);
              dlg.setVisible(true);
          }
       }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    public static org.iesapp.framework.util.HtmlLog htmlLog1;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private com.l2fprod.common.swing.JLinkButton jLinkButton1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenuConfig;
    private javax.swing.JMenu jMenuGestio;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemChangeClients;
    private javax.swing.JMenuItem jMenuItemNouCurs;
    private javax.swing.JMenu jMenuSim;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JMenu jMenuWizards;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JToolBar jToolBarAny;
    private org.iesapp.apps.administrador.MainFrame mainFrame1;
    private javax.swing.JMenuBar xjMenuBar1;
    // End of variables declaration//GEN-END:variables

   
    public static void main(String[] args) {
           SwingUtilities.invokeLater(new DisplayApp(args));     
    }

    private void includeAdminHelpSet() {
         JarClassLoader systemClassLoader = JarClassLoader.getInstance();
         URL url = systemClassLoader.getResource("org/iesapp/apps/administrador/help/module.hs");
        // URL url = mainHelpSet.findmainHelpSet(systemClassLoader, "/org/iesapp/modules/reserves/help/module.hs");
         if (url != null) {
            try {
                HelpSet helpSet = new HelpSet(systemClassLoader, url);
                coreCfg.getMainHelpSet().add(helpSet);
            } catch (HelpSetException ex) {
                Logger.getLogger(AdministradorGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
    }
     
    private static class DisplayApp implements Runnable
    {
            private final String[] args;
            public DisplayApp(String[] args)
            {
                this.args = args;
            }
            
            @Override
            public void run() {
                 AdministradorGUI app = new AdministradorGUI(args);
                 app.setVisible(true);
            }
        
    }
}