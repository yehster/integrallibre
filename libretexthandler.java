/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integrallibre;
import java.io.StringWriter;
import java.io.FileWriter;
import com.sun.star.uno.UnoRuntime;
import java.util.regex.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 *
 * @author yehster
 */
public class libretexthandler {
    
    protected com.sun.star.text.XTextDocument mXTextDocument;
    protected String sFilename;
    protected String sShortFilename;
    protected docmetadata dmdMetadata;
    protected String mHeaderText;
    protected java.util.ArrayList alHeaders = new java.util.ArrayList();
    protected Document mDOMDoc;
    protected Element mRoot;
    protected Element mCurrentSection;
    protected Element mCurrentHeader;
    protected Element mPatientIdentifiers;
    protected Element mDocumentInfo;

    public libretexthandler(com.sun.star.frame.XDesktop xDesktop, String filename)
    {
        mXTextDocument=OpenTextdocument(xDesktop,filename);
        sFilename=filename;
        initializeXML();
        parseFilename();
    }
    
    private void parseFilename()
    {
        Pattern slashes = Pattern.compile("\\\\");
        Matcher matcher = slashes.matcher(sFilename);
        String normalized =matcher.replaceAll("/");
        Pattern shortFileName = Pattern.compile("/[A-Z0-9]*_+[0-9]*.DOC$");
        Matcher mSFN = shortFileName.matcher(normalized);
        if(mSFN.find())
        {
            sShortFilename=normalized.substring(mSFN.start()+1,mSFN.end()-4);
            sShortFilename=sShortFilename.trim();
            Element mFileInfo=mDOMDoc.createElement("Filename");
            mFileInfo.setTextContent(sShortFilename);
            mDocumentInfo.appendChild(mFileInfo);
        }
    }


    
    private void initializeXML()
    {
            //We need a Document
            try
            {
                DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                mDOMDoc = docBuilder.newDocument();   
            ////////////////////////
            //Creating the XML tree

            //create the root element and add it to the document
             mRoot= mDOMDoc.createElement("transcription");
             mDOMDoc.appendChild(mRoot);
             
             mDocumentInfo=mDOMDoc.createElement("DocumentInfo");
             mRoot.appendChild(mDocumentInfo);
             
             mPatientIdentifiers=mDOMDoc.createElement("PatientIdentifiers");
             mRoot.appendChild(mPatientIdentifiers);
            
             mCurrentSection=null;
             mHeaderText="";             
            }
            catch(Exception e)
            {
                
            }

    }
    
    
    public boolean parse(docmetadata dmd)
    {
        dmdMetadata=dmd;
        com.sun.star.text.XText xText = (com.sun.star.text.XText)mXTextDocument.getText();
        

            com.sun.star.container.XEnumeration xParagraphEnumeration = null;
            com.sun.star.container.XEnumerationAccess xParaEnumerationAccess = null;
            com.sun.star.container.XEnumeration xPortionEnumeration = null;
            com.sun.star.container.XEnumeration xTextPortionEnum;
            com.sun.star.text.XTextContent xTextElement = null;

            /////////////////////////////
            //Creating an empty XML Document

            com.sun.star.container.XEnumerationAccess xEnumerationAccess =
                (com.sun.star.container.XEnumerationAccess)
                    UnoRuntime.queryInterface(
                        com.sun.star.container.XEnumerationAccess.class, xText);
            xParagraphEnumeration = xEnumerationAccess.createEnumeration();
            

        try
            {
            while ( xParagraphEnumeration.hasMoreElements() ) {
                xTextElement = (com.sun.star.text.XTextContent)
                    UnoRuntime.queryInterface(
                        com.sun.star.text.XTextContent.class,
                        xParagraphEnumeration.nextElement());
                    
                this.analyzeParagraph(xTextElement);

                }


            }
            
            // Loop through all paragraphs of the document
            catch( Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return true;
    }
    
    protected boolean analyzeParagraph(com.sun.star.text.XTextContent xText)
    {
        String sParagraph=xText.getAnchor().getString().trim();
        boolean headerMatch=false;
        java.util.regex.Matcher Header=null;
        for(Integer idx=0;(idx<dmdMetadata.getHeadersPatterns().size()) && !headerMatch;idx++)
        {
            java.util.regex.Pattern curPattern= (java.util.regex.Pattern)dmdMetadata.getHeadersPatterns().get(idx);
            Header = curPattern.matcher(sParagraph);
            headerMatch=Header.find();
        }
        if(headerMatch)
        {
            this.mHeaderText=sParagraph.substring(Header.start(), Header.end()-1);
            alHeaders.add(mHeaderText);
            mCurrentSection=mDOMDoc.createElement("section");
            mRoot.appendChild(mCurrentSection);
            mCurrentHeader=mDOMDoc.createElement("header");
            mCurrentHeader.setTextContent(mHeaderText);
            mCurrentSection.appendChild(mCurrentHeader);
            String sRestOfLine = null;
            if(sParagraph.length()>Header.end())
            {
                sRestOfLine=sParagraph.substring(Header.end()+1).trim();
                Element content=mDOMDoc.createElement("content");
                content.setTextContent(sRestOfLine);
                mCurrentSection.appendChild(content);
            }
            String headerMapping=dmdMetadata.LookupIdentiferHeaderMapping(mHeaderText);
            if(headerMapping!=null)
            {
                mCurrentSection.setAttribute("tag", headerMapping);
                Element eIdentifier=mDOMDoc.createElement(headerMapping);
                if(sRestOfLine!=null)
                {
                    eIdentifier.setTextContent(sRestOfLine);                
                }
                mPatientIdentifiers.appendChild(eIdentifier);
            }
         }
         else
        {
            if(sParagraph.length()==0)
            {
                this.mHeaderText="";
                mCurrentSection=null;
            }
            else
            {
                if(mCurrentSection!=null)
                {
                    Element content=mDOMDoc.createElement("content");
                    content.setTextContent(sParagraph);
                    mCurrentSection.appendChild(content);
                }
                else
                {
                    // Content not marked by a header
                    if(sParagraph.startsWith(dmdMetadata.getDictatorFullName()))
                    {
                        Element mDictator=mDOMDoc.createElement("dictator");
                        mDictator.setTextContent(sParagraph);
                        mDocumentInfo.appendChild(mDictator);
                    }
                    else
                    {
                        if(sParagraph.startsWith(dmdMetadata.getDictatorInitials()))
                        {
                            Element Workflow=mDOMDoc.createElement("workflow");
                            mDocumentInfo.appendChild(Workflow);

                            Element info=mDOMDoc.createElement("info");
                            info.setTextContent(sParagraph);
                            Workflow.appendChild(info);
                        }
                    }
                }
            }
        }
        

        return true;
    }

    protected static com.sun.star.lang.XComponent OpenDocument(
        com.sun.star.frame.XDesktop xDesktop,
        String sFilename )
    {
        String sURL="file:///"+sFilename;
        com.sun.star.lang.XComponent xComponent = null;
        com.sun.star.frame.XComponentLoader xComponentLoader = null;
        com.sun.star.beans.PropertyValue xValues[] =
            new com.sun.star.beans.PropertyValue[1];
        com.sun.star.beans.PropertyValue xEmptyArgs[] =
            new com.sun.star.beans.PropertyValue[0];
        
        try {
            xComponentLoader = (com.sun.star.frame.XComponentLoader)
                UnoRuntime.queryInterface(
                    com.sun.star.frame.XComponentLoader.class, xDesktop);
        
            xComponent  = xComponentLoader.loadComponentFromURL(
                sURL, "_blank", 0, xEmptyArgs);
        }
        catch( Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
        return xComponent ;
    }
    
    protected static com.sun.star.text.XTextDocument OpenTextdocument(
        com.sun.star.frame.XDesktop xDesktop, String sFilename )
    {
        com.sun.star.text.XTextDocument aTextDocument = null;

        try {
            com.sun.star.lang.XComponent xComponent = OpenDocument(xDesktop,
                                                                        sFilename);
            aTextDocument = (com.sun.star.text.XTextDocument)
                UnoRuntime.queryInterface(
                    com.sun.star.text.XTextDocument.class, xComponent);
        }
        catch( Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return aTextDocument;
    }    
    

    public Document getDOM()
    {
        return mDOMDoc;
    }
    public String getShortFilename()
    {
        return sShortFilename;
    }
    
    public void writeXML(String filename) throws Exception
    {
            FileWriter fw = new FileWriter(filename);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(fw);
            DOMSource source = new DOMSource(mDOMDoc);
            trans.transform(source, result);
            fw.close();
        
    }
    protected String genXMLString() throws Exception
    {
        try
        {
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(mDOMDoc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            return xmlString;           
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}
