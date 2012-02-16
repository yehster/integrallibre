/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrallibre;

import java.util.List;

/**
 *
 * @author yehster
 */
public interface docmetadata {
    public java.util.List getHeadersPatterns();
    public String getDictatorFullName();
    public String getDictatorInitials();
    public String LookupHeaderMapping(String header);
    
}
