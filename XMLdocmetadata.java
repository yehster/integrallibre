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

    java.util.HashMap mIdentifierHeaderMappings = new java.util.HashMap();
    java.util.HashMap mDocumentInfoHeaderMappings = new java.util.HashMap();
  
    public XMLdocmetadata(String filename)
    {
        mHeaderPatterns.add(java.util.regex.Pattern.compile("^[A-Z]+[/#-]?\\s??[A-Z]*\\s??[A-Z]*:"));
        initializeIdentifierHeaderMappings();

    }

    protected void initializeIdentifierHeaderMappings()
    {
        mIdentifierHeaderMappings.put("DOB","DateOfBirth");
        mIdentifierHeaderMappings.put("CHART#","PID");
        mIdentifierHeaderMappings.put("PATIENT","Patient");
        mIdentifierHeaderMappings.put("OFFICE VISIT","DateOfService");
        mIdentifierHeaderMappings.put("OFFICE VISIT","DateOfService");
        mIdentifierHeaderMappings.put("DOI","DateOfInjury");
        
        mDocumentInfoHeaderMappings.put("REFERRING PHYSICIAN","ReferringPhysician");
        mDocumentInfoHeaderMappings.put("EMP","Employer");
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
    
    public String LookupIdentiferHeaderMapping(String header)
    {
        return (String)mIdentifierHeaderMappings.get(header);
    }
    public String LookupDocumentInfoHeaderMapping(String header)
    {
        return (String)mDocumentInfoHeaderMappings.get(header);        
    }
    
}
