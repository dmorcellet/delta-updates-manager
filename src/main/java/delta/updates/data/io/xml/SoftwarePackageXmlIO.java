package delta.updates.data.io.xml;

import java.io.File;

import javax.xml.transform.sax.TransformerHandler;

import org.w3c.dom.Element;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.common.utils.text.EncodingNames;
import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.SoftwarePackage;

/**
 * XML I/O facilities for software packages.
 * @author DAM
 */
public class SoftwarePackageXmlIO
{
  /**
   * Write a software package to an XML file.
   * @param toFile File to write to.
   * @param data Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeFile(File toFile, final SoftwarePackage data)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        SoftwarePackageXMLWriter.writeSoftwarePackage(hd,data);
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
  public static SoftwarePackage parseFile(File source)
  {
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      return SoftwarePackageXMLParser.parseSoftwarePackage(root);
    }
    return null;
  }
}
