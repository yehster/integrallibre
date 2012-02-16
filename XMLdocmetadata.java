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

    java.util.HashMap mHeaderMappings = new java.util.HashMap();
    public XMLdocmetadata(String filename)
    {
        mHeaderPatterns.add(java.util.regex.Pattern.compile("^[A-Z]+[/#-]?\\s??[A-Z]*:"));
        initializeHeaderMappings();

    }

    protected void initializeHeaderMappings()
    {
        mHeaderMappings.put("DOB","Date of Birth");
        mHeaderMappings.put("CHART#","PID");
        mHeaderMappings.put("PATIENT","Patient");
        mHeaderMappings.put("OFFICE VISIT","Date of Service");
        mHeaderMappings.put("OFFICE VISIT","Date of Service");
        mHeaderMappings.put("DOI","Date of Injury");
        
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
    
    public String LookupHeaderMapping(String header)
    {
        return (String)mHeaderMappings.get(header);
    }
    
}
