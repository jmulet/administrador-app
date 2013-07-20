/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import com.l2fprod.common.swing.JLinkButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.iesapp.apps.administrador.Cfg;

/**
 *
 * @author Josep
 */
public class RelatedTables extends javax.swing.JPanel {
    private String[] tables;
    private String catalog;
    private Cfg cfg;
     
    /**
     * Creates new form RelatedTables
     */
    public RelatedTables() {
        initComponents(); 
    }
    
    public void startUp(final Cfg cfg)
    {
        this.cfg = cfg;
    }
    
    public void setTables(String[] tables, final String catalog)
    {
       jPanel1.removeAll();
       jPanel1.revalidate();
        
        this.tables = tables;
        this.catalog = catalog;
        for(String tname: tables)
        {
            JLinkButton button = new JLinkButton(tname);
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                     JLinkButton source = (JLinkButton) e.getSource();
                     String tname = source.getText();
                     org.iesapp.apps.mysqlbrowser.DBEditorDlg dlg = new org.iesapp.apps.mysqlbrowser.DBEditorDlg(javar.JRDialog.getActiveFrame(),false, tname, cfg.getCoreCfg().getMysql(),
                             catalog, tname);
                     dlg.setAlwaysOnTop(true);
                     dlg.setVisible(true);
                }
            });
            jPanel1.add(button);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 4);
        flowLayout1.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout1);
        jScrollPane1.setViewportView(jPanel1);

        jLabel1.setText("Taules Relacionades");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}