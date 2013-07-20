/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.jasperreports;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.iesapp.apps.administrador.Cfg;
import org.iesapp.apps.administrador.actions.beans.BeanReport;
import org.iesapp.clients.iesdigital.actuacions.BeanFieldSet;
import org.iesapp.clients.iesdigital.actuacions.BeanRules;
import org.iesapp.framework.pluggable.modulesAPI.GenericFactory;
import org.iesapp.framework.util.CoreCfg;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Josep
 */
public class JasperReportParser {
    private Document doc = null;
    private final File file;
    private final ArrayList<BeanFieldSet> fields;
    private DocumentBuilder b;
    private Element jasperReport;
    private final BeanRules ruleXml;
    private final BeanReport beanReport;
    public JasperReportParser(File file, int idRule, String ambit, String estudis, BeanReport br, Cfg cfg)
    {
        this.file = file;
        //Parse the current form
        this.beanReport = br;
        ruleXml = cfg.getCoreCfg().getIesClient().getFitxesClient().getFactoryRules().getRuleXml(idRule, ambit, estudis);
        fields = ruleXml.getFields();
        
        
        try { 
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();         
            b = f.newDocumentBuilder();
        
            if(file.exists())
            {
                doc = b.parse(file);
            }
            
            } catch (Exception ex) {
                Logger.getLogger(JasperReportParser.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
    }
    
    public boolean checkExistence()
    {
        return file.exists();
    }
    
    public HashMap<String,String> checkParameters()
    {
        HashMap<String,String> result = new HashMap<String,String>();
        if(doc==null)
        {
            System.out.println("Can't find doc");
            return result;
        }
        
        NodeList root = doc.getElementsByTagName("jasperReport");
        NodeList nodes = ((Element) root.item(0)).getElementsByTagName("parameter");
        HashMap<String,String> paramInReport = new HashMap<String,String>();
        for(int i=0; i<nodes.getLength(); i++)
        {
            Node item = nodes.item(i);
            NamedNodeMap attributes = item.getAttributes();
            Node namedItem = attributes.getNamedItem("name");
            Node namedItem2 = attributes.getNamedItem("class");
            if(namedItem!=null && namedItem2!=null)
            {
                String parameterName = namedItem.getNodeValue();
                String parameterClass = namedItem2.getNodeValue();
                paramInReport.put(parameterName, parameterClass);
            }
        }
        
        //Check if all parameters in form are in report
        for(BeanFieldSet bfs: fields)
        {
            String fieldName = bfs.getFieldName();
            if(!paramInReport.containsKey(fieldName))
            {
                result.put(fieldName, "ERROR: Report does not have '"+fieldName+"' parameter");
            }
        }
        
        //Check viceversa
        for(String key: paramInReport.keySet())
        {
            boolean found = false;
            for(BeanFieldSet bfs: fields)
            {
                if(bfs.getFieldName().equals(key))
                {
                    found = true;
                    break;
                }
            }  
                if(!found)
                {
                     result.put(key, "WARNING: Report contains a redundant parameter named '"+key+"'");
                }
            
        }
       
        paramInReport.clear();
        
        return result;
    }

    public void createNewDoc() {
        if(b==null)
        {
            return;
        }
        
        doc = b.newDocument();
            
        jasperReport = doc.createElement("jasperReport");
        jasperReport.setAttribute("xmlns", "http://jasperreports.sourceforge.net/jasperreports");
        jasperReport.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        jasperReport.setAttribute("xsi:schemaLocation", "http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd");
        jasperReport.setAttribute("name", file.getName());
        jasperReport.setAttribute("pageWidth", "595");
        jasperReport.setAttribute("pageHeight", "842");
        jasperReport.setAttribute("whenNoDataType", "AllSectionsNoDetail");
        jasperReport.setAttribute("columnWidth", "555");
        jasperReport.setAttribute("leftMargin", "20" );
        jasperReport.setAttribute("rightMargin","20" );
        jasperReport.setAttribute("topMargin", "20");        
        jasperReport.setAttribute("bottomMargin", "20");
        jasperReport.setAttribute("whenResourceMissingType","Empty");
        jasperReport.setAttribute("uuid", UUID.randomUUID()+"");
        doc.appendChild(jasperReport);
 
        Element prop1 = doc.createElement("property");
        prop1.setAttribute("name", "ireport.zoom");
        prop1.setAttribute("value", "1.5");
        jasperReport.appendChild(prop1);
        
        Element prop2 = doc.createElement("property");
        prop2.setAttribute("name", "ireport.x");
        prop2.setAttribute("value", "33");
        jasperReport.appendChild(prop2);
        
        Element prop3 = doc.createElement("property");
        prop3.setAttribute("name", "ireport.y");
        prop3.setAttribute("value", "147");
        jasperReport.appendChild(prop3);

        for (BeanFieldSet bfs : fields) {
            if(bfs.isAddToMap())
            {
                Element parameter = doc.createElement("parameter");
                parameter.setAttribute("name", bfs.getFieldName());
                parameter.setAttribute("class", "java.lang.String");
                jasperReport.appendChild(parameter);
            }
        }
         
        Element parameter = doc.createElement("parameter");
        parameter.setAttribute("name", "SUBREPORT_DIR");
        parameter.setAttribute("class", "java.lang.String");
        parameter.setAttribute("isForPrompting", "false");
            Element subparameter = doc.createElement("defaultValueExpression");
            parameter.appendChild(subparameter);
            String path = CoreCfg.contextRoot+File.separator+"reports"+File.separator;
            path = path.replaceAll("\\\\", "\\\\\\\\");
            CDATASection createCDATASection = doc.createCDATASection("\""+path+"\"");
            subparameter.appendChild(createCDATASection);
             
        jasperReport.appendChild(parameter);
        
        //Add fields if bean injects lists
        if(beanReport.getIncludeSubReport().equalsIgnoreCase("F"))
        {
           // addField(jasperReport,"fielName","java.lang.String");
        }
        else if(beanReport.getIncludeSubReport().equalsIgnoreCase("A"))
        {
            
        }
        else if(beanReport.getIncludeSubReport().equalsIgnoreCase("R"))
        {
            
        }
       
            
        //Crea les bandes
        
        Element bandTitle = addBand(doc, "title", 50);
        Element bandHeader = addBand(doc, "pageHeader", 450);
        Element bandDetail = null; 
        if(!beanReport.getIncludeSubReport().equalsIgnoreCase("N")){
            bandDetail = addBand(doc, "detail", 50);
        }
        Element bandFooter = addBand(doc, "pageFooter", 50);
        
        //Afegeix un title
        addTextField(bandTitle, 39, 14, 467, 29, "Center", new BeanFont("Arial",20), "\""+ruleXml.getDescripcioLlarga()+"\"");
   
        //Afegeix els parametres al bandHeader
         int y0 = 55;
         int dy = 20;
         for (BeanFieldSet bfs : fields) {
            if(bfs.isAddToMap())
            {
                addTextField(bandHeader, 39, y0, 467, dy, "Left", new BeanFont(), 
                       "\""+bfs.getFieldDescription()+" : \"+  $P{"+bfs.getFieldName()+"}");
                y0 += dy;
            }
        }
        saveXml();
        
    }

    private void saveXml() {
        try {
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            FileWriter outputwriter = new FileWriter(file);
            XMLSerializer serializer = new XMLSerializer(outputwriter, format);
            serializer.serialize(doc);
            outputwriter.close();
        } catch (IOException ex) {
            Logger.getLogger(GenericFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Element addBand(Document doc, String bandType, int height) {
        Element element = doc.createElement(bandType);
        Element band_element = doc.createElement("band");
        band_element.setAttribute("height", height+"");
        element.appendChild(band_element);
        jasperReport.appendChild(element);
        return band_element;
    }

    private void addField(Element root, String fieldName, String className) {
        Element createElement = doc.createElement("field");
        createElement.setAttribute("name", fieldName);
        createElement.setAttribute("class", className);
        root.appendChild(createElement);
    }

    private void addTextField(Element root, int x, int y, int width, int height, String align, BeanFont font, String text) {
        //        <textField>
//				<reportElement x="39" y="84" width="467" height="20"/>
//				<textElement textAlignment="Center">
//					<font isBold="true" isItalic="true" isUnderline="true"/>
//				</textElement>
//				<textFieldExpression><![CDATA["INICIACIÓ DE L’ EXPEDIENT PER ACUMULACIÓ D'AMONESTACIONS GREUS"]]></textFieldExpression>
//			</textField>
        
        Element textField = doc.createElement("textField");
            Element re = doc.createElement("reportElement");
            re.setAttribute("uuid", UUID.randomUUID()+"");
            re.setAttribute("x", x+"");
            re.setAttribute("y", y+"");
            re.setAttribute("width", width+"");
            re.setAttribute("height", height+"");
            textField.appendChild(re);
        
            Element te = doc.createElement("textElement");
            te.setAttribute("textAlignment", align);
                Element efont = doc.createElement("font");
                    efont.setAttribute("size", font.getSize()+"");
                    efont.setAttribute("isBold", font.isBold()?"true":"false");
                    efont.setAttribute("isItalic", font.isItalic()?"true":"false"); 
                    efont.setAttribute("isUnderline", font.isUnderline()?"true":"false");
                te.appendChild(efont);
            textField.appendChild(te);
            
            Element tfe = doc.createElement("textFieldExpression");
            CDATASection createCDATASection = doc.createCDATASection(text);
            tfe.appendChild(createCDATASection);
            textField.appendChild(tfe);
        root.appendChild(textField);
    }

    //When adding new parameter, it must be added after the last one, not at the
    //end of file
    public void fix() {
         HashMap<String,String> result = checkParameters();
         NodeList root = doc.getElementsByTagName("jasperReport");
         NodeList background = doc.getElementsByTagName("background");
         
         Node jr = root.item(0); 
         for(String key: result.keySet())
         {
            String get = result.get(key);
            if(get.contains("ERROR"))
            {
                 Element createElement = doc.createElement("parameter");
                 createElement.setAttribute("name", key);
                 createElement.setAttribute("class", "java.lang.String");
                 if(background.getLength()>0)
                 {
                    jr.insertBefore(createElement, background.item(0));
                 }
            }
         }
         saveXml();
    }

    
}
