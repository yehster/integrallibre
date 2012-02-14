/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrallibre;

/**
 *
 * @author yehster
 */
public class XMLdocmetadata implements docmetadata {
    public XMLdocmetadata(String filename)
    {
        mHeaderPatterns.add(java.util.regex.Pattern.compile("^[A-Z]+[/#-]?\\s??[A-Z]*:"));
    }
    protected java.util.ArrayList mHeaderPatterns = new java.util.ArrayList();
    public java.util.List getHeadersPatterns()
    {
        
        return mHeaderPatterns;
    }
    public String getDictatorFullName()
    {
        return "Miguel J. Saldana, M.D., F.A.C.S.";
    }
    public String getDictatorInitials()
    {
        return "MJS";
    }
    
    
}
