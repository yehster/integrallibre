/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrallibre;

import com.sun.star.uno.UnoRuntime;
/**
 *
 * @author yehster
 */
public class Integrallibre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        com.sun.star.frame.XDesktop xDesktop = null;
        xDesktop = getDesktop();
        String filename="e:/MedVocab/MSA01092012002/MSA01092012004.DOC";
        libretexthandler lthHandler=new libretexthandler(xDesktop,filename);
        docmetadata dmd = new XMLdocmetadata("");
        lthHandler.parse(dmd);
        
    }

    public static com.sun.star.frame.XDesktop getDesktop() {
        com.sun.star.frame.XDesktop xDesktop = null;
        com.sun.star.lang.XMultiComponentFactory xMCF = null;

        try {
            com.sun.star.uno.XComponentContext xContext = null;

            // get the remote office component context
            xContext = com.sun.star.comp.helper.Bootstrap.bootstrap();

            // get the remote office service manager
            xMCF = xContext.getServiceManager();
            if( xMCF != null ) {
                System.out.println("Connected to a running office ...");

                Object oDesktop = xMCF.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);
                xDesktop = (com.sun.star.frame.XDesktop) UnoRuntime.queryInterface(
                    com.sun.star.frame.XDesktop.class, oDesktop);
            }
            else
                System.out.println( "Can't create a desktop. No connection, no remote office servicemanager available!" );
        }
        catch( Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }


        return xDesktop;
    }
}
