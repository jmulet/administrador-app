/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.CoreCfg;

/**
 *
 * @author Josep
 */
public class Step4 extends javax.swing.JPanel {
    private DefaultListModel model1, model2;
    private DefaultComboBoxModel combomodel1;
    private HashMap<String, String> taules;
    private final Cfg cfg;

    /**
     * Creates new form StartupStep3
     */
    public Step4(final Cfg cfg) {
        this.cfg = cfg;
        initComponents();
        relatedTables1.startUp(cfg);
     
    }
    
    public void updateComponents()
    {
        dBEditorPanel1.setTable(cfg.getCoreCfg().getMysql(), CoreCfg.core_mysqlDB, "sig_guardies");
        relatedTables1.setTables(new String[]{"sig_guardies_zones","sig_professorat","sig_hores_clase"},CoreCfg.core_mysqlDB);
      }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        dBEditorPanel1 = new org.iesapp.apps.mysqlbrowser.DBEditorPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        relatedTables1 = new org.iesapp.apps.administrador.wizard.noucurs.RelatedTables();

        jLabel3.setText("Prepareu un document .xls amb aquesta estructura de dades. Després, feis click en 'Importa xls' i en 'desa'");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("?");
        jButton1.setToolTipText("Ajuda");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dBEditorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(relatedTables1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1))
                .addGap(3, 3, 3)
                .addComponent(dBEditorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relatedTables1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NouCursWiz.showQuickHelpDialog(null, "Abrev: identificador unic per a cada professor\n"+
                "Lloc: codig del lloc de la guardia així com estableix sig_guardies_zones\n"
                + "Dia: 1=Dilluns, 2=Dimarts, ...\n"+
                "Hora: 1, 2, 3, ... segons la taula sig_hores_clase");
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private org.iesapp.apps.mysqlbrowser.DBEditorPanel dBEditorPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private org.iesapp.apps.administrador.wizard.noucurs.RelatedTables relatedTables1;
    // End of variables declaration//GEN-END:variables

   
}