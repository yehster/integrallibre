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
        String filename;
        String path="";
        if(args.length > 0)
        {
            filename=args[0];
            if(args.length>1)
            {
                path=args[1];
                path=path.trim();
            }
        }
        else
        {
            filename="e:\\MedVocab/MSA01092012002\\MSA01092012001.DOC";
        }
        libretexthandler lthHandler=new libretexthandler(xDesktop,filename);
        docmetadata dmd = new XMLdocmetadata("");
        lthHandler.parse(dmd);
        try
        {
            //
            
            lthHandler.writeXML(path+lthHandler.getShortFilename()+".xml");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(path);
        System.out.println(lthHandler.getShortFilename());
//        xDesktop.terminate();
        System.exit(0);
        
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
                String[] arguments={"--headless","--nologo"};
                Object oDesktop = xMCF.createInstanceWithArgumentsAndContext(
                    "com.sun.star.frame.Desktop", arguments,xContext);
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
