package delta.updates.contents.io.xml;

import java.io.File;

import javax.xml.transform.sax.TransformerHandler;

import org.w3c.dom.Element;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.common.utils.text.EncodingNames;
import delta.common.utils.xml.DOMParsingTools;
import delta.updates.contents.ContentsManager;

/**
 * XML I/O facilities for contents descriptions.
 * @author DAM
 */
public class ContentsXmlIO
{
  /**
   * Write a contents manager to an XML file.
   * @param toFile File to write to.
   * @param contentsMgr Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeFile(File toFile, final ContentsManager contentsMgr)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        ContentsXMLWriter.writeContentsManager(hd,contentsMgr);
      }
    };
    boolean ret=helper.write(toFile,EncodingNames.UTF_8,writer);
    return ret;
  }

  /**
   * Parse a contents manager XML file.
   * @param source Source file.
   * @return Parsed contents manager.
   */
  public static ContentsManager parseFile(File source)
  {
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      return ContentsXMLParser.parseContentsManager(root);
    }
    return null;
  }
}
