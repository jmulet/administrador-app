/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.wizard.noucurs;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.framework.util.CoreCfg;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class Step1 extends javax.swing.JPanel {
    private DefaultListModel model1, model2;
    private DefaultComboBoxModel combomodel1;
    private HashMap<String, String> taules;
    private final Cfg cfg;

    /**
     * Creates new form StartupStep3
     */
    public Step1(final Cfg cfg) {
        this.cfg = cfg;
        initComponents();
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);  
        
    }
    
    public void registerListenerRadioButtons(ActionListener listener)
    {
        jRadioButton1.addActionListener(listener);
        jRadioButton2.addActionListener(listener);
        jComboBox1.addActionListener(listener);
    }
            
    
    
    public boolean doSave()
    {
        boolean q = false;
        String value = "";
        if(jRadioButton1.isSelected())
        {
            //Create a new curs
                  
            int selyear = (Integer) jComboBox1.getSelectedItem();
            value = selyear +"";
            
            
            //Crea la base en blanc per curs seleccionat
            cfg.getCoreCfg().createEmptyxxxxDB( CoreCfg.core_mysqlDBPrefix+value ); 
            //System.out.println("created new db="+CoreCfg.core_mysqlDBPrefix+value );
            
            //Copia les bases de dades que interesen
            if(jCheckBox1.isSelected())
            {
                Object[] selectedValues = jList2.getSelectedValues();
                
                for(int i=0; i<selectedValues.length; i++)
                {
                    String str = (String) selectedValues[i];
                    String tableName = StringUtils.AfterLast(str,"{");
                    tableName = StringUtils.BeforeLast(tableName, "}");
                     
                    String from =CoreCfg.core_mysqlDBPrefix+(selyear-1);
                    String to = CoreCfg.core_mysqlDBPrefix+selyear;
                    if(tableName!=null)
                    {
                    cfg.getCoreCfg().getMysql().copyDataTableBetweenDBs(tableName, from, to);
                    }
                    //System.out.println("copying "+tableName+" from  "+from+" to "+ to );
                }
            }
            
        }
        else
        {
            
              value = (String) jList1.getSelectedValue();
        }
        
        //Carrega la configuracio per defecte
        cfg.getCoreCfg().readDatabaseCfg();
        //Desa la configuracio a la taula.
        CoreCfg.configTableMap.put("anyIniciCurs", value);
        cfg.getCoreCfg().updateDatabaseCfg("anyIniciCurs", value);
        cfg.getCoreCfg().anyAcademic = Integer.parseInt(value);
        CoreCfg.core_mysqlDB = CoreCfg.core_mysqlDBPrefix+value;
        cfg.getCoreCfg().getMysql().setCatalog(CoreCfg.core_mysqlDB);
    
        return q;        
    }
    
    
    public void onShow()
    {
        
        model1 = new DefaultListModel();
        jList1.setModel(model1);
                
        model2 = new DefaultListModel();
        jList2.setModel(model2);

        //Llista de taules a mantenir dades
        taules = new HashMap<String,String>();
        
        //Taules susceptibles d'esser reciclades per un any al següent
        taules.put("avaluacions", "Avaluacions");
        taules.put("avaluacionsdetall", "Avaluacions detall");
        taules.put("sig_espais", "Aules");
        taules.put("tuta_actuacions", "Actuacions de tutoria");
        taules.put("tuta_actuacions_fields", "Actuacions de tutoria - camps");
        taules.put("tuta_actuacions_reports", "Actuacions de tutoria - reports");
        taules.put("sig_hores_classe", "Hores de classe");
        taules.put("sig_guardies_zones", "Zones de guàrdia");
        taules.put("sig_senseguardia", "Camps sense guàrdia");
        taules.put("sig_reserves_material", "Materials reservables");
        taules.put("sig_missatgeria_items", "Items d'info tutors");
        taules.put("sig_progtasques", "Tasques programades");
        taules.put("fitxa_programes", "Programes PIE");
        taules.put("fitxa_permisos", "Permisos grups d'usuaris (deprecated)");
        taules.put("sig_grant", "Sistema de permisos");
        taules.put("sig_grant_roles", "Sistema de permisos - roles");
        taules.put("sig_grant_values", "Sistema de permisos - valors");
        
        
        for(String keyTaula: taules.keySet()){
            model2.addElement(taules.get(keyTaula)+" {"+ keyTaula +"}");
        }
                
        
        jList2.setSelectionInterval(0, (model2.getSize()-1) );
        
        combomodel1 = new DefaultComboBoxModel();
        jComboBox1.setModel(combomodel1);
        
         //determina els cursos que existeixen
        cfg.getCoreCfg().getMysql().setCatalog(CoreCfg.core_mysqlDBPrefix);
        String SQL1 = "Show databases";
        ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        try {
            while(rs1!=null && rs1.next())   
            {
                String txt = rs1.getString(1);
                if(txt.startsWith(CoreCfg.core_mysqlDBPrefix))
                {
                    model1.addElement(txt.replaceAll(CoreCfg.core_mysqlDBPrefix, ""));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Step1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int thisyear = StringUtils.anyAcademic_primer_int();
        for(int i=thisyear-3; i<thisyear+4; i++)
        {
            if(!model1.contains(i+"")) {
                combomodel1.addElement(i);
            }
        }
        if(model1.contains( (thisyear+"") ) )
        {
            jRadioButton2.setSelected(true);
            jList1.setSelectedValue((thisyear+""), true);
        }
        else
        {
             jComboBox1.setSelectedItem(thisyear);
        }
               
      
        updateComponents();
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
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jRadioButton2 = new javax.swing.JRadioButton();

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Manté algunes dades del curs anterior");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Aules", "Actuacions de tutoria", "Hores de classe", "Zones de guàrdia", "Camps sense guàrdia", "Materials reservables", "Items d'info tutors", "Tasques programades", "Programes PIE", "Grups de permisos" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Crea un nou curs");

        jLabel2.setText("Any inici del curs");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jRadioButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jRadioButton2.setText("Selecciona un curs existent");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1))
                    .addComponent(jRadioButton2))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        jList2.setEnabled(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    public void updateComponents() {
        if(jRadioButton1.isSelected())
        {
            jComboBox1.setEnabled(true);
            jList2.setEnabled(true);
            jCheckBox1.setEnabled(true);
            jList1.setEnabled(false);
        }
        else
        {
            jComboBox1.setEnabled(false);
            jList2.setEnabled(false);
            jCheckBox1.setEnabled(false);
            jList1.setEnabled(true);
        }
        
          //Existeix un curs anterior?
        boolean q = model1.contains(( (Integer) jComboBox1.getSelectedItem()-1)+"");
        jCheckBox1.setEnabled(q);
        jList2.setEnabled(q);
 
    }
}
