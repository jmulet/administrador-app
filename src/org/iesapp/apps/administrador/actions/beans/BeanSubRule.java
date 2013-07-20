/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions.beans;

import java.util.ArrayList;
import org.iesapp.apps.administrador.Cfg;

/**
 *
 * @author Josep
 */
public class BeanSubRule {
    protected int id;
    protected int idRule;
    protected String ambits="*";
    protected String estudis="*";
    protected int minAge=0;
    protected int maxAge=100;
    protected String enviamentCarta="N";
    protected String enviamentSMS="S";
    protected String instancesPolicy="SINGLE";
    protected boolean registerInFitxaAlumne;
    protected String form="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                          "<root>\n\n</root>";
    protected ArrayList<BeanReport> reports = new ArrayList<BeanReport>();
    private final Cfg cfg;
    
    public BeanSubRule(Cfg cfg)
    {
        this.cfg = cfg;
    }
    
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRule() {
        return idRule;
    }

    public void setIdRule(int idRule) {
        this.idRule = idRule;
    }

    public String getAmbits() {
        return ambits;
    }

    public void setAmbits(String ambits) {
        this.ambits = ambits;
    }

    public String getEstudis() {
        return estudis;
    }

    public void setEstudis(String estudis) {
        this.estudis = estudis;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getEnviamentCarta() {
        return enviamentCarta;
    }

    public void setEnviamentCarta(String enviamentCarta) {
        this.enviamentCarta = enviamentCarta;
    }

    public String getEnviamentSMS() {
        return enviamentSMS;
    }

    public void setEnviamentSMS(String enviamentSMS) {
        this.enviamentSMS = enviamentSMS;
    }

    public String getInstancesPolicy() {
        return instancesPolicy;
    }

    public void setInstancesPolicy(String instancesPolicy) {
        this.instancesPolicy = instancesPolicy;
    }

    public boolean isRegisterInFitxaAlumne() {
        return registerInFitxaAlumne;
    }

    public void setRegisterInFitxaAlumne(boolean registerInFitxaAlumne) {
        this.registerInFitxaAlumne = registerInFitxaAlumne;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void save() {
        if(this.id<=0)
        {
            insert();
        }
        else
        {
            update();
        }
    }

    public void delete() {
        if(this.id>0)
        {
             String SQL1 = "DELETE FROM tuta_actuacions_fields WHERE id="+id+" LIMIT 1";      
             cfg.getCoreCfg().getMysql().executeUpdate(SQL1);
             this.id = -1;
        }
    }

    private void insert() {
       String SQL1 = "INSERT INTO tuta_actuacions_fields (idRule,ambits,estudis,idFieldSet,minAge,maxAge,"
                 + " enviamentCarta,enviamentSMS,instancesPolicy,registerInFitxaAlumne,xmlForm) "
                 + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
       Object[] obj = new Object[]{idRule,ambits,estudis,0,minAge,maxAge,enviamentCarta.trim(),
       enviamentSMS.trim(), instancesPolicy, registerInFitxaAlumne?"S":"N", form};
       this.id = cfg.getCoreCfg().getMysql().preparedUpdateID(SQL1, obj);
    }

    private void update() {
       String SQL1 = "UPDATE tuta_actuacions_fields SET idRule=?,ambits=?,estudis=?,idFieldSet=?,minAge=?,maxAge=?,"
                 + " enviamentCarta=?,enviamentSMS=?,instancesPolicy=?,registerInFitxaAlumne=?,xmlForm=? WHERE id="+this.id;
                 
       Object[] obj = new Object[]{idRule,ambits,estudis,0,minAge,maxAge,enviamentCarta.trim(),
       enviamentSMS.trim(), instancesPolicy, registerInFitxaAlumne?"S":"N", form};
       cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, obj);
    }

    public ArrayList<BeanReport> getReports() {
        return reports;
    }

    public void setReports(ArrayList<BeanReport> reports) {
        this.reports = reports;
    }
 }
