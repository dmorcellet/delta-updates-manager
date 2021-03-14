package delta.updates.data.io.xml;

import java.io.File;

import javax.xml.transform.sax.TransformerHandler;

import org.w3c.dom.Element;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.common.utils.text.EncodingNames;
import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;

/**
 * XML I/O facilities for software descriptions.
 * @author DAM
 */
public class SoftwareDescriptionXmlIO
{
  /**
   * Write a software description to an XML file.
   * @param toFile File to write to.
   * @param data Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeFile(File toFile, final SoftwareDescription data)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        SoftwareDescriptionXMLWriter.writeSoftware(hd,data);
      }
    };
    boolean ret=helper.write(toFile,EncodingNames.UTF_8,writer);
    return ret;
  }

  /**
   * Parse a software description from an XML file.
   * @param source Source file.
   * @return Parsed software description.
   */
  public static SoftwareDescription parseSoftwareDescriptionFile(File source)
  {
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      return SoftwareDescriptionXMLParser.parseSoftwareDescription(root);
    }
    return null;
  }
  /**
   * Write a software package to an XML file.
   * @param toFile File to write to.
   * @param data Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeFile(File toFile, final SoftwarePackageDescription data)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        SoftwareDescriptionXMLWriter.writePackageDescription(hd,data);
      }
    };
    boolean ret=helper.write(toFile,EncodingNames.UTF_8,writer);
    return ret;
  }

  /**
   * Parse a software package from an XML file.
   * @param source Source file.
   * @return Parsed software package.
   */
  public static SoftwarePackageDescription parsePackageFile(File source)
  {
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      return SoftwareDescriptionXMLParser.parseSoftwarePackage(root);
    }
    return null;
  }
}
