/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions;

 
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.database.MyDatabase;

/**
 *
 * @author Josep
 */
public class DlgAvaluacions extends javar.JRDialog {
    private final Cfg cfg;

    /**
     * Creates new form DlgAvaluacions
     */
    public DlgAvaluacions(java.awt.Frame parent, boolean modal, Cfg cfg) {
        super(parent, modal);
        this.cfg = cfg;
        initComponents();
        
        HashMap<String,Date> map = getDateMap(cfg.getCoreCfg().getMysql());
        
        jDateChooser1.setDate(map.get("1a_ini"));
        jDateChooser2.setDate(map.get("1a_fi"));
        
        jDateChooser3.setDate(map.get("2a_ini"));
        jDateChooser4.setDate(map.get("2a_fi"));
        
        jDateChooser5.setDate(map.get("3a_ini"));
        jDateChooser6.setDate(map.get("3a_fi"));
        
        if(cfg.getCoreCfg().getSgd()==null){
            jButton2.setEnabled(false);
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

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editor d'Avaluacions");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Atenció: Per un correcte funcionament, els períodes no han de tenir cap dia en comú.");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel3.setText("fins");

        jLabel2.setText("des de");

        jLabel1.setText("1a Avaluació");

        jLabel6.setText("2a Avaluació");

        jLabel7.setText("3a Avaluació");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel6))
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7))
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 100, 5));

        jButton2.setText("Importa del SGD");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton1.setText("Tancar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      //Primer comprova si els periodes son correctes
      java.util.Date ini1a = jDateChooser1.getDate();
      java.util.Date fi1a = jDateChooser2.getDate();
      
      java.util.Date ini2a = jDateChooser3.getDate();
      java.util.Date fi2a = jDateChooser4.getDate();
      
      java.util.Date ini3a = jDateChooser5.getDate();
      java.util.Date fi3a = jDateChooser6.getDate();
      
      String str = null;
      jLabel5.setText(str);
      if(ini2a.compareTo(fi1a)<=0)
      {
         str = "Alerta: Ini 2a <= Fi 1a";
      }
      else if(ini3a.compareTo(fi2a)<=0)
      {       
          str = "Alerta: Ini 3a <= Fi 2a";
      }
      else if(ini1a.compareTo(fi1a)>=0)
      {
          str = "Alerta: Dates d'inici i fi 1a";
      }
      else if(ini2a.compareTo(fi2a)>=0)
      {
          str = "Alerta: Dates d'inici i fi 2a";
      }
      else if(ini3a.compareTo(fi3a)>=0)
      {
          str = "Alerta: Dates d'inici i fi 3a";
      }
      else if(ini1a==null || ini2a==null || ini3a==null || fi1a==null ||fi2a==null || fi3a==null)
      {
          str = "Alerta: Cal donar un valor a tots els camps";
      } 
      if(str!=null)
      {
          jLabel5.setText(str);
          return;
      }
      
      //Desa les noves dates a la base de dades
      String SQL1="UPDATE evaluacionesdetalle SET fechaInicio=?, fechaFin=?, fechaInicioReal=?, fechaFinReal=? WHERE valorExportable like '1a%'";
      int nup = cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, new Object[]{ini1a,fi1a,ini1a,fi1a});
      
      SQL1="UPDATE evaluacionesdetalle SET fechaInicio=?, fechaFin=?, fechaInicioReal=?, fechaFinReal=? WHERE valorExportable like '2a%'";
      nup = cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, new Object[]{ini2a,fi2a,ini2a,fi2a});
      
      SQL1="UPDATE evaluacionesdetalle SET fechaInicio=?, fechaFin=?, fechaInicioReal=?, fechaFinReal=? WHERE valorExportable like '3a%' OR valorExportable like 'Ord%'";
      nup = cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, new Object[]{ini3a,fi3a,ini3a,fi3a});
       
      this.dispose();
      
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(cfg.getCoreCfg().getSgd()!=null && !cfg.getCoreCfg().getSgd().isClosed())
        {
             HashMap<String,Date> map = getDateMap(cfg.getCoreCfg().getSgd());
             jDateChooser1.setDate(map.get("1a_ini"));
             jDateChooser2.setDate(map.get("1a_fi"));
        
            jDateChooser3.setDate(map.get("2a_ini"));
            jDateChooser4.setDate(map.get("2a_fi"));
        
            jDateChooser5.setDate(map.get("3a_ini"));
            jDateChooser6.setDate(map.get("3a_fi"));
        }
    }//GEN-LAST:event_jButton2ActionPerformed
 
     private HashMap<String, Date> getDateMap(MyDatabase db) {
                        
        HashMap<String,Date> map = new HashMap<String,Date>();
        //Read values
        String SQL1 = "SELECT * FROM evaluacionesdetalle";
        ResultSet rs1 = db.getResultSet(SQL1);
        try {
            while(rs1!=null && rs1.next())
            {
                Date date1 = rs1.getDate("fechaInicio");
                Date date2 = rs1.getDate("fechaFin");
                String str = rs1.getString("valorExportable");
                if(str.startsWith("1a"))
                {
                    map.put("1a_ini", date1);
                    map.put("1a_fi", date2);
                }
                else if(str.startsWith("2a"))
                {
                    map.put("2a_ini", date1);
                    map.put("2a_fi", date2);
                }
                else if(str.startsWith("3a") || str.contains("Ord"))
                {
                    map.put("3a_ini", date1);
                    map.put("3a_fi", date2);
                }
            }
            if(rs1!=null) {
                rs1.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DlgAvaluacions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }
    
     @Override
    protected JRootPane createRootPane() {
    JRootPane rootPane2 = new JRootPane();
    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
    AbstractAction actionListener = new AbstractAction() {
      public void actionPerformed(ActionEvent actionEvent) {
        setVisible(false);
      }
    } ;
    InputMap inputMap = rootPane2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(stroke, "ESCAPE");
    rootPane2.getActionMap().put("ESCAPE", actionListener);

    return rootPane2;
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

   
}