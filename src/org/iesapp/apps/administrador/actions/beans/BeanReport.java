/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.apps.administrador.Cfg;

/**
 *
 * @author Josep
 */
public class BeanReport {
    protected int id;  
    protected int idSubRule; 
//    protected String ambit="*";
//    protected String estudis="*";
    protected String reportPath="";
    protected String reportDescription="";
    protected String includeSubReport="N"; //enum('N','F','A','R') NOT NULL
    protected int limitInc=0;
    protected boolean important=true;
    protected String popupInstructions;
    protected String visibilitat="*"; //enum("P","*") prefectura o prefectura&tutor
    protected String lang="CA";
    private final Cfg cfg;

    public BeanReport(Cfg cfg)
    {
        this.cfg = cfg;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getIncludeSubReport() {
        return includeSubReport;
    }

    public void setIncludeSubReport(String includeSubReport) {
        this.includeSubReport = includeSubReport;
    }

    public int getLimitInc() {
        return limitInc;
    }

    public void setLimitInc(int limitInc) {
        this.limitInc = limitInc;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getPopupInstructions() {
        return popupInstructions;
    }

    public void setPopupInstructions(String popupInstructions) {
        this.popupInstructions = popupInstructions;
    }

    public String getVisibilitat() {
        return visibilitat;
    }

    public void setVisibilitat(String visibilitat) {
        this.visibilitat = visibilitat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public static ArrayList<BeanReport> loadAll(int idSubRule, Cfg cfg)
    {
        ArrayList<BeanReport> list = new ArrayList<BeanReport>();
        String SQL1 = "SELECT * FROM tuta_actuacions_reports WHERE idSubRule="+idSubRule;
        ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1);
        try {
            while(rs1!=null && rs1.next())
            {
                 BeanReport bean = new BeanReport(cfg);
                 bean.id = rs1.getInt("id");
                 bean.idSubRule = rs1.getInt("idSubRule");
//                 bean.ambit = rs1.getString("ambit");
//                 bean.estudis = rs1.getString("estudis");
                 bean.important = rs1.getString("important").equalsIgnoreCase("S");
                 bean.includeSubReport = rs1.getString("includeSubreport");
                 bean.lang = rs1.getString("lang");
                 bean.limitInc = rs1.getInt("limitInc");
                 bean.popupInstructions = rs1.getString("popupInstructions");
                 bean.reportDescription = rs1.getString("reportDescription");
                 bean.reportPath = rs1.getString("reportPath");
                 bean.visibilitat = rs1.getString("visibilitat");
                 list.add(bean);
            }
            if(rs1!=null)
            {
                rs1.close();
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(BeanReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public void save()
    {
        if(this.id<=0)
        {
            insert();
        }
        else
        {
            update();
        }
    }

    public void delete()
    {
        if(this.id>0)
        {
            String SQL1 = "DELETE FROM tuta_actuacions_reports WHERE id="+id+" LIMIT 1";
            cfg.getCoreCfg().getMysql().executeUpdate(SQL1);
            this.id = -1;
        }
    }

    
   

    private void insert() {
       String SQL1 = "INSERT INTO tuta_actuacions_reports (idSubRule,reportPath,reportDescription,"
               + "lang,includeSubReport,limitInc,popupInstructions,important,visibilitat) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
       Object[] obj = new Object[]{idSubRule,reportPath,reportDescription,lang,includeSubReport,
            limitInc,popupInstructions,important?"S":"N",visibilitat};
       this.id = cfg.getCoreCfg().getMysql().preparedUpdateID(SQL1, obj);

    }

    private void update() {
       String SQL1 = "UPDATE tuta_actuacions_reports SET idSubRule=?,reportPath=?,reportDescription=?,"
               + "lang=?,includeSubReport=?,limitInc=?,popupInstructions=?,important=?,visibilitat=? "
               + " WHERE id="+this.id;
       Object[] obj = new Object[]{idSubRule,reportPath,reportDescription,lang,includeSubReport,
            limitInc,popupInstructions,important?"S":"N",visibilitat};
       cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, obj);
       System.out.println(cfg.getCoreCfg().getMysql().getLastPstm());
    }

    public int getidSubRule() {
        return idSubRule;
    }

    public void setidSubRule(int idSubRule) {
        this.idSubRule = idSubRule;
    }
 
}
