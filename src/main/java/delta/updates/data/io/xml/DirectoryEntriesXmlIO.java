package delta.updates.data.io.xml;

import java.io.File;

import javax.xml.transform.sax.TransformerHandler;

import org.w3c.dom.Element;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.common.utils.text.EncodingNames;
import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.DirectoryEntryDescription;

/**
 * XML I/O facilities for directory entries.
 * @author DAM
 */
public class DirectoryEntriesXmlIO
{
  /**
   * Write a directory entry to an XML file.
   * @param toFile File to write to.
   * @param entry Data to save.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public static boolean writeFile(File toFile, final DirectoryEntryDescription entry)
  {
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        DirectoryEntriesXMLWriter.writeEntry(hd,entry);
      }
    };
    boolean ret=helper.write(toFile,EncodingNames.UTF_8,writer);
    return ret;
  }

  /**
   * Parse a directory entry XML file.
   * @param source Source file.
   * @return Parsed directory entry.
   */
  public static DirectoryEntryDescription parseFile(File source)
  {
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      return DirectoryEntriesXMLParser.parseEntry(root);
    }
    return null;
  }
}
