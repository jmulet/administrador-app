/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.actions.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.util.StringUtils;

/**
 *
 * @author Josep
 */
public class BeanRule {
    protected ArrayList<BeanSubRule> subRules = null;
    protected int id;
    protected String actuacio="New Rule";
    protected String descripcio="";
    protected String tipus="ASSISTENCIA";
    protected String forRoles="*";
    protected boolean nomesAdmin;
    protected String simbol="";
    protected int threshold = 0;
    protected int vrepeticio= 0;
    protected int vmax= 0;
    protected boolean alertActuacionsPendents = true;
    protected String requireCreated = "";
    protected boolean repetir;
    protected boolean autoTancar;
    protected String equivalencies="";
    protected String requireClosed="";
    protected String instruccions;
    protected String reglament;
    protected String alert="";
    protected String classRepresentator;
    protected String extensionsTxt = "";
    private final Cfg cfg;

    public BeanRule(Cfg cfg)
    {
        this.cfg = cfg;
        subRules.add(new BeanSubRule(cfg));
    }
    
    public BeanRule(int id, boolean lazy, Cfg cfg)
    {
        this.cfg = cfg;
        if(!lazy)
        {
            fullLoad(id);
        }
        else
        {
            lazyLoad(id);
        }
    }
    
    
    public BeanRule(int id, Cfg cfg)
    {
        this.cfg = cfg;
        fullLoad(id);
    }
    
    private void fullLoad(int id)
    {
        //Loads the rule with this id
        subRules = new ArrayList<BeanSubRule>();
        
        String SQL1  = "SELECT * FROM tuta_actuacions WHERE id="+id;
         try {
            Statement st = cfg.getCoreCfg().getMysql().createStatement();
            ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs1!=null && rs1.next())
            {
                this.actuacio = rs1.getString("actuacio");
                this.alert = rs1.getString("alert");
                this.autoTancar = rs1.getString("autoTancar").equalsIgnoreCase("S");
                this.classRepresentator = rs1.getString("class");
                if(this.classRepresentator!=null && this.classRepresentator.equals("(NULL)"))
                {
                    this.classRepresentator = null;
                }
                this.descripcio = rs1.getString("descripcio");
                this.equivalencies = rs1.getString("equivalencies");
                this.id = rs1.getInt("id");
                this.instruccions = rs1.getString("instruccions");
                if( this.instruccions==null)
                {
                    this.instruccions = "";
                }
                this.nomesAdmin = rs1.getInt("nomesAdmin")>0;
                this.forRoles = rs1.getString("roles");
                this.reglament = rs1.getString("reglament");
                if(this.reglament==null)
                {
                    this.reglament="";
                }
                this.repetir = rs1.getString("repetir").equalsIgnoreCase("S");
                this.requireClosed = rs1.getString("requireClosed");
                this.requireCreated = rs1.getString("requireCreated");
                this.simbol = rs1.getString("simbol");
                this.threshold = rs1.getInt("threshold");
                this.vmax= rs1.getInt("vmax");
                this.vrepeticio = rs1.getInt("vrepeticio");
                        
                this.alertActuacionsPendents = rs1.getString("alertActuacionsPendents").equalsIgnoreCase("S");
                this.tipus = rs1.getString("tipus");
                
                this.extensionsTxt = StringUtils.noNull(rs1.getString("form"));
            }
            if(rs1!=null) {
                rs1.close();
                st.close();
            }
     
         
            SQL1 = "SELECT * FROM tuta_actuacions_fields WHERE idRule="+id;
               Statement st2 = cfg.getCoreCfg().getMysql().createStatement();
             rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs1!=null && rs1.next())
            {
                BeanSubRule bean = new BeanSubRule(cfg);
                bean.ambits = rs1.getString("ambits");
                bean.enviamentCarta = rs1.getString("enviamentCarta");
                bean.enviamentSMS = rs1.getString("enviamentSMS");
                bean.estudis = rs1.getString("estudis");
                bean.form = rs1.getString("xmlForm");
                bean.id = rs1.getInt("id");
                bean.idRule = rs1.getInt("idRule");
                bean.instancesPolicy = rs1.getString("instancesPolicy");
                bean.maxAge = rs1.getInt("maxAge");
                bean.minAge = rs1.getInt("minAge");
                bean.registerInFitxaAlumne = rs1.getString("registerInFitxaAlumne").equalsIgnoreCase("S");
                           
                this.subRules.add(bean);
            }
            if(rs1!=null)
            {
                rs1.close();
                st2.close();
            }
            
            for(BeanSubRule bean: subRules)
            {
                bean.reports = BeanReport.loadAll(bean.id, cfg);  //Carrega tots els reports de la subregla
               
            }
            
            
            
        
        } catch (SQLException ex) {
            Logger.getLogger(BeanRule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void lazyLoad(int id)
    {
        //Loads the rule with this id
        
        String SQL1  = "SELECT * FROM tuta_actuacions WHERE id="+id;
     
        try {
            Statement st = cfg.getCoreCfg().getMysql().createStatement();
            ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs1!=null && rs1.next())
            {
                this.actuacio = rs1.getString("actuacio");
                this.alert = rs1.getString("alert");
                this.autoTancar = rs1.getString("autoTancar").equalsIgnoreCase("S");
                this.classRepresentator = rs1.getString("class");
                if(this.classRepresentator!=null && this.classRepresentator.equals("(NULL)"))
                {
                    this.classRepresentator = null;
                }
                this.descripcio = rs1.getString("descripcio");
                this.equivalencies = rs1.getString("equivalencies");
                this.id = rs1.getInt("id");
                this.instruccions = rs1.getString("instruccions");
                if( this.instruccions==null)
                {
                    this.instruccions = "";
                }
                this.nomesAdmin = rs1.getInt("nomesAdmin")>0;
                this.forRoles = rs1.getString("roles");
                this.reglament = rs1.getString("reglament");
                if(this.reglament==null)
                {
                    this.reglament="";
                }
                this.repetir = rs1.getString("repetir").equalsIgnoreCase("S");
                this.requireClosed = rs1.getString("requireClosed");
                this.requireCreated = rs1.getString("requireCreated");
                this.simbol = rs1.getString("simbol");
                this.threshold = rs1.getInt("threshold");
                this.vmax = rs1.getInt("vmax");
                this.vrepeticio = rs1.getInt("vrepeticio");
                this.alertActuacionsPendents = rs1.getString("alertActuacionsPendents").equalsIgnoreCase("S");
                this.tipus = rs1.getString("tipus");
                this.extensionsTxt = StringUtils.noNull(rs1.getString("form"));
            }
            if(rs1!=null) {
                rs1.close();
                st.close();
            }
            
        
        } catch (SQLException ex) {
            Logger.getLogger(BeanRule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    
     public void forceLoading()
     {
       if(this.subRules!=null) 
       {
           return; //already loaded
       }
       subRules = new ArrayList<BeanSubRule>();
       
       try {
            String SQL1 = "SELECT * FROM tuta_actuacions_fields WHERE idRule="+id;
              Statement st = cfg.getCoreCfg().getMysql().createStatement();
             ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            
            while(rs1!=null && rs1.next())
             {
                 BeanSubRule bean = new BeanSubRule(cfg);
                 bean.ambits = rs1.getString("ambits");
                 bean.enviamentCarta = rs1.getString("enviamentCarta");
                 bean.enviamentSMS = rs1.getString("enviamentSMS");
                 bean.estudis = rs1.getString("estudis");
                 bean.form = rs1.getString("xmlForm");
                 bean.id = rs1.getInt("id");
                 bean.idRule = rs1.getInt("idRule");
                 bean.instancesPolicy = rs1.getString("instancesPolicy");
                 bean.maxAge = rs1.getInt("maxAge");
                 bean.minAge = rs1.getInt("minAge");
                 bean.registerInFitxaAlumne = rs1.getString("registerInFitxaAlumne").equalsIgnoreCase("S");
                            
                 this.subRules.add(bean);
             }
             if(rs1!=null)
             {
                 rs1.close();
                 st.close();
             }
             
             for(BeanSubRule bean: subRules)
             {
                 bean.reports = BeanReport.loadAll(bean.id, cfg);  //Carrega tots els reports de la subregla         
             }
        } catch (SQLException ex) {
            Logger.getLogger(BeanRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }
     
     
    public ArrayList<BeanSubRule> getSubRules() {
        return subRules;
    }

    public void setSubRules(ArrayList<BeanSubRule> subRules) {
        this.subRules = subRules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActuacio() {
        return actuacio;
    }

    public void setActuacio(String actuacio) {
        this.actuacio = actuacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public boolean isNomesAdmin() {
        return nomesAdmin;
    }

    public void setNomesAdmin(boolean nomesAdmin) {
        this.nomesAdmin = nomesAdmin;
    }

    public String getSimbol() {
        return simbol;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isRepetir() {
        return repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public boolean isAutoTancar() {
        return autoTancar;
    }

    public void setAutoTancar(boolean autoTancar) {
        this.autoTancar = autoTancar;
    }

    public String getEquivalencies() {
        return equivalencies;
    }

    public void setEquivalencies(String equivalencies) {
        this.equivalencies = equivalencies;
    }

    public String getRequireClosed() {
        return requireClosed;
    }

    public void setRequireClosed(String requireClosed) {
        this.requireClosed = requireClosed;
    }

    public String getInstruccions() {
        return instruccions;
    }

    public void setInstruccions(String instruccions) {
        this.instruccions = instruccions;
    }

    public String getReglament() {
        return reglament;
    }

    public void setReglament(String reglament) {
        this.reglament = reglament;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getClassRepresentator() {
        return classRepresentator;
    }

    public void setClassRepresentator(String classRepresentator) {
        this.classRepresentator = classRepresentator;
    }
   
    public static ArrayList<BeanRule> getAllRulesLazyLoading(Cfg cfg)
    {
         ArrayList<BeanRule> list = new  ArrayList<BeanRule>();
         String SQL1  = "SELECT * FROM tuta_actuacions ORDER BY id";
          
         try {
                Statement st = cfg.getCoreCfg().getMysql().createStatement();
                ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs1!=null && rs1.next())
            {              
                int id = rs1.getInt("id");
                list.add( new BeanRule(id,true,cfg) );                
            }
            if(rs1!=null)
            {
                rs1.close();
                st.close();
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(BeanRule.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         return list;
    }

    public static ArrayList<BeanRule> getAllRules(Cfg cfg)
    {
         ArrayList<BeanRule> list = new  ArrayList<BeanRule>();
         String SQL1  = "SELECT * FROM tuta_actuacions ORDER BY id";
          try {
             Statement st = cfg.getCoreCfg().getMysql().createStatement();
            ResultSet rs1 = cfg.getCoreCfg().getMysql().getResultSet(SQL1,st);
            while(rs1!=null && rs1.next())
            {              
                int id = rs1.getInt("id");
                list.add( new BeanRule(id,cfg) );                
            }
            if(rs1!=null)
            {
                rs1.close();
                st.close();
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(BeanRule.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         return list;
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
        
       for(BeanSubRule subr: subRules)
       {
           subr.save();
       }
    }
    
    public void delete()
    {
        if(this.id>0)
        {
             String SQL1 = "DELETE FROM tuta_actuacions WHERE id="+id+" LIMIT 1";      
             cfg.getCoreCfg().getMysql().executeUpdate(SQL1);
             this.id = -1;
        }
           
       for(BeanSubRule subr: subRules)
       {
           subr.delete();
       }
    }
    
   private void insert()
   {
       String SQL1 = "INSERT INTO tuta_actuacions (actuacio,descripcio,tipus,nomesAdmin,roles,simbol,"
               + "threshold,vrepeticio,vmax,alertActuacionsPendents,repetir,autoTancar,equivalencies,requireClosed,requireCreated,instruccions,reglament,"
               + "alert,class,form) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       Object[] obj = new Object[]{actuacio,descripcio,tipus,nomesAdmin?1:0,forRoles,simbol,
                threshold,vrepeticio,vmax,alertActuacionsPendents?"S":"N",repetir?"S":"N",autoTancar?"S":"N",
                equivalencies,requireClosed,requireCreated,instruccions,
                reglament,alert,classRepresentator,extensionsTxt};
       this.id = cfg.getCoreCfg().getMysql().preparedUpdateID(SQL1, obj);
       
       for(BeanSubRule bsr: subRules)
       {
           bsr.idRule = id;
       }
   }
   
   private void update()
   {
       String SQL1 = "UPDATE tuta_actuacions SET actuacio=?,descripcio=?,tipus=?,nomesAdmin=?,roles=?,simbol=?,"
               + "threshold=?,vrepeticio=?,vmax=?,alertActuacionsPendents=?,repetir=?,autoTancar=?,equivalencies=?,requireClosed=?,"
               + "requireCreated=?,instruccions=?,reglament=?,"
               + "alert=?,class=?,form=? WHERE id="+id;
       Object[] obj = new Object[]{actuacio,descripcio,tipus,nomesAdmin?1:0,forRoles,simbol,
                threshold,vrepeticio,vmax,alertActuacionsPendents?"S":"N",repetir?"S":"N",autoTancar?"S":"N",equivalencies,
                requireClosed,requireCreated,instruccions,
                reglament,alert,classRepresentator,extensionsTxt};
       cfg.getCoreCfg().getMysql().preparedUpdate(SQL1, obj);
       
   }

    public String getForRoles() {
        return forRoles;
    }

    public void setForRoles(String forRoles) {
        this.forRoles = forRoles;
    }

    public boolean isAlertActuacionsPendents() {
        return alertActuacionsPendents;
    }

    public void setAlertActuacionsPendents(boolean alertActuacionsPendents) {
        this.alertActuacionsPendents = alertActuacionsPendents;
    }

    public String getRequireCreated() {
        return requireCreated;
    }

    public void setRequireCreated(String requireCreated) {
        this.requireCreated = requireCreated;
    }

    public int getVrepeticio() {
        return vrepeticio;
    }

    public void setVrepeticio(int vrepeticio) {
        this.vrepeticio = vrepeticio;
    }

    public int getVmax() {
        return vmax;
    }

    public void setVmax(int vmax) {
        this.vmax = vmax;
    }

    public String getExtensionsTxt() {
        return extensionsTxt;
    }

    public void setExtensionsTxt(String extensionsTxt) {
        this.extensionsTxt = extensionsTxt;
    }

  
}
