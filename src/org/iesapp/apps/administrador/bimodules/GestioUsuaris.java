/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.bimodules;

import com.l2fprod.common.swing.StatusBar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.admin.AssignaRoles;
import org.iesapp.framework.admin.cfg.DlgCreaUsuari;
import org.iesapp.framework.pluggable.TopModuleWindow;

/**
 *
 * @author Josep
 */
public class GestioUsuaris extends TopModuleWindow {
    private DefaultTableModel modelTable1;
    private ArrayList<String> abrevModified;
    private boolean isListening = false;
    private final DefaultComboBoxModel comboTorn;
    private final Cfg cfg;
  
    /**
     * Creates new form GestioUsuaris
     */
    public GestioUsuaris(Cfg cfg) {
         this.cfg = cfg;
         this.moduleDescription = "Gestió dels usaris";
         this.moduleDisplayName = "Gestió usuaris";
         this.moduleName = "gestioUsuaris";
          initComponents();
        
         modelTable1.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE)
                {
                    int row = jTable1.getSelectedRow();
                    String id = ((String) jTable1.getValueAt(row, 0));
                    if(!abrevModified.contains(id))
                    {
                        abrevModified.add(id);
                    }
                }
            }
        });
         
         abrevModified = new ArrayList<String>();
         DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();
         DefaultComboBoxModel comboDepart = new DefaultComboBoxModel();
         comboTorn = new DefaultComboBoxModel();
         comboTorn.addElement("No signable");
         comboTorn.addElement("Matí");
         comboTorn.addElement("Tarda");
         comboTorn.addElement("Matí i tarda");


        //Aquesta taula ja no s'empra cal agafar el roles de sig_grant_roles
        String SQL1 = "SELECT DISTINCT role FROM sig_grant_roles order by role";
        ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        modelCombo.addElement(""); //undefined
        try {
            while (rs1 != null && rs1.next()) {
                String grup = rs1.getString(1);
                modelCombo.addElement(grup);
            }
            if(rs1!=null) {
                rs1.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(AssignaRoles.class.getName()).log(Level.SEVERE, null, e);
        }

        SQL1 = "SELECT DISTINCT depart FROM sig_professorat order by depart";
        rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        
        try {
            while (rs1 != null && rs1.next()) {
                String grup = rs1.getString("depart");
                comboDepart.addElement(grup);
            }
            if(rs1!=null) {
                rs1.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(AssignaRoles.class.getName()).log(Level.SEVERE, null, e);
        }

        jTable1.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(
             new JComboBox(modelCombo)
             ));

        jTable1.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(
             new JComboBox(comboDepart)
             ));

        jTable1.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(
             new JComboBox(comboTorn)
             ));

      


        modelTable1.addTableModelListener( new TableModelListener()
              {

              public void tableChanged(TableModelEvent e)
               {

                    int mcol = e.getColumn();
                    if(isListening && mcol >1)
                    {
                       int mrow = e.getFirstRow();                        
                        String abrev = (String) jTable1.getValueAt(mrow,0);
                    
                       if(!abrevModified.contains(abrev)) {
                            abrevModified.add(abrev);
                        }
                       ////System.out.println("Canvi per abrev"+abrev);
                   }
                                  
                      
              }
      });

        fillTable();
        
        checkErrors();
        isListening = true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                if(colIndex <1)
                return false;   //Disallow the editing of any cell
                else
                return true;
            }
        }
        ;
        jLabel3 = new javax.swing.JLabel();

        jToolBar1.setFloatable(false);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/administrador/icons/configIcon.gif"))); // NOI18N
        jButton3.setText("Auto");
        jButton3.setToolTipText("Assignació automàtica");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/administrador/icons/insert.gif"))); // NOI18N
        jButton4.setText("Nou usuari");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/administrador/icons/save.gif"))); // NOI18N
        jButton1.setText("Desa");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        modelTable1 = new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Abrev", "idSgd", "Nom", "Contrasenya", "Role", "Departament","Torn"
            }
        );
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(modelTable1);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(250);
        jTable1.setRowHeight(32);
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/apps/administrador/icons/atention.png"))); // NOI18N
        jLabel3.setText("  ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentContainer());
        getContentContainer().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String SQL1 = "SELECT DISTINCT prof FROM sig_horaris WHERE asig='TUTA'";
        ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        ArrayList<String> tutors = new ArrayList<String>();
        try {
            while (rs1 != null && rs1.next()) {
                String prof = rs1.getString("prof");
                tutors.add(prof);
            }
            if(rs1!=null) {
                rs1.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(AssignaRoles.class.getName()).log(Level.SEVERE, null, e);
        }

        for(int i=0; i<jTable1.getRowCount(); i++)
        {
            String abrev = (String) jTable1.getValueAt(i, 0);
            if(tutors.contains(abrev))
            {
                jTable1.setValueAt("TUTOR", i, 4);
            }
            else
            {
                jTable1.setValueAt("NOTUTOR", i, 4);
            }
            if(!abrevModified.contains(abrev)) {
                abrevModified.add(abrev);
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        DlgCreaUsuari dlg = new DlgCreaUsuari(javar.JRDialog.getActiveFrame(), true, cfg.getCoreCfg());
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);

        fillTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveModifications();        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fillTable() {
       
       isListening = false;
       
       while(jTable1.getRowCount()>0) {
            modelTable1.removeRow(0);
        }
       
       String SQL1 = "SELECT abrev, idSgd, nombre, Contrasenya, GrupFitxes, depart, torn FROM usu_usuari AS usu INNER JOIN sig_professorat AS prof ON usu.Nom=prof.abrev ORDER BY nombre";
       ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        try {
            while (rs1 != null && rs1.next()) {
                String abrev = rs1.getString(1);
                String idSgd = rs1.getString(2);
                String nombre = rs1.getString(3);
                String pwd = rs1.getString(4);
                String gf = rs1.getString(5);
                String dept = rs1.getString(6);
                int torn = rs1.getInt(7);
                String storn = "No signable";
                if(torn==0)
                {
                    storn="Matí";
                }
                else if(torn==1)
                {
                    storn ="Tarda";
                }
                else if(torn==2)
                {
                    storn="Matí i tarda";
                }
                
                modelTable1.addRow(new Object[]{abrev, idSgd, nombre, pwd, gf, dept, storn});
            }
              if(rs1!=null) {
                rs1.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(AssignaRoles.class.getName()).log(Level.SEVERE, null, e);
        }
         isListening = true;
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setMenus(JMenuBar jMenuBar1, JToolBar jToolbar1, StatusBar jStatusBar1) {
       jToolbar1.add(jToolBar1);
    }

     
    @Override
    public void beforeDispose() {
        if(!abrevModified.isEmpty())
        {
            int confirm = JOptionPane.showConfirmDialog(javar.JRDialog.getActiveFrame(),"Voleu desar els canvis abans de tancar?", "Confirmació", JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION)
            {
                saveModifications();
            }
        }
        abrevModified.clear();
    }

    private void saveModifications() {
        jTable1.editCellAt(0,0);
        for(String id: abrevModified)
        {
            int i = findRow(id);
            String abrev = (String) jTable1.getValueAt(i, 0);
            if(abrevModified.contains(abrev))
            {
                String idSgd = (String) jTable1.getValueAt(i, 1);
                String nom = (String) jTable1.getValueAt(i, 2);
                String contrasenya = (String) jTable1.getValueAt(i, 3);
                String grupfitxes = (String) jTable1.getValueAt(i, 4);
                String depart = (String) jTable1.getValueAt(i, 5);
                String storn = (String) jTable1.getValueAt(i, 6);
                int torn = comboTorn.getIndexOf(storn)-1;

                String SQL1 = "UPDATE usu_usuari SET Contrasenya='"+contrasenya+
                "', usuari='"+idSgd+"', GrupFitxes='"+grupfitxes+"' WHERE Nom='"+abrev+"'";
                int nup = cfg.getCoreCfg().getMysql().executeUpdate(SQL1);

                SQL1 = "UPDATE sig_professorat SET nombre='"+nom+"', depart='"+depart+"', torn="+torn+", idSGD="+idSgd+" WHERE abrev='"+abrev+"'";
                nup = cfg.getCoreCfg().getMysql().executeUpdate(SQL1);
            }
        }
        abrevModified.clear();
        checkErrors();

    }
   
      /**
     * Find the row for a given scheme id
     * @param id
     * @return 
     */
     private int findRow(String id) {
        int row = -1;
        for(int i=0; i<jTable1.getRowCount(); i++)
        {
            if(((String) jTable1.getValueAt(i, 0)).equals(id))
            {
                row = i;
                break;
            }
        }
        return row;
    }
     


    private void checkErrors()
    {
         //Check possible Errors
        //Users which have undefined roles
        String SQL1 = "SELECT Nom, GrupFitxes FROM usu_usuari WHERE "
                + "usu_usuari.GrupFitxes NOT IN (SELECT role AS GrupFitxes FROM sig_grant_roles)";
       
        String txt = "";
        try {
            Statement st = cfg.getCoreCfg().getMysql().createStatement();
            ResultSet rs = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs!=null && rs.next())
            {
                txt += rs.getString(2)+" ["+rs.getString(1)+"]; ";
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(GrantManager.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        jLabel3.setVisible(!txt.isEmpty());
        jLabel3.setText("Undefined roles: "+txt);
    }
}
