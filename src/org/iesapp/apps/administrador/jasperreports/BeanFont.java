/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.apps.administrador.jasperreports;

/**
 *
 * @author Josep
 */
public class BeanFont {
    protected String fontName = "Arial";
    protected int size = 12;
    protected boolean italic = false;
    protected boolean bold = false;
    protected boolean underline = false;

    public BeanFont()
    {
        //Default
    }
    
    public BeanFont(String fontName, int size)
    {
        this.fontName = fontName;
        this.size = size;
    }
    
    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }
    
    
}
