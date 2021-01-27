package delta.updates.data.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;

/**
 * Writes directory entries to XML files.
 * @author DAM
 */
public class DirectoryEntriesXMLWriter
{
  /**
   * Write an entry to the given output.
   * @param hd Output.
   * @param entry Entry to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeEntry(TransformerHandler hd, DirectoryEntryDescription entry) throws SAXException
  {
    if (entry instanceof FileDescription)
    {
      writeFile(hd,(FileDescription)entry);
    }
    else if (entry instanceof DirectoryDescription)
    {
      writeDirectory(hd,(DirectoryDescription)entry);
    }
  }

  private static void writeFile(TransformerHandler hd, FileDescription file) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // Name
    String name=file.getName();
    attrs.addAttribute("","",DirectoryEntriesXMLConstants.ENTRY_NAME_ATTR,XmlWriter.CDATA,name);
    // Size
    long size=file.getSize();
    attrs.addAttribute("","",DirectoryEntriesXMLConstants.FILE_SIZE_ATTR,XmlWriter.CDATA,String.valueOf(size));
    // CRC
    long crc=file.getCRC();
    attrs.addAttribute("","",DirectoryEntriesXMLConstants.FILE_CRC_ATTR,XmlWriter.CDATA,String.valueOf(crc));
    hd.startElement("","",DirectoryEntriesXMLConstants.FILE_TAG,attrs);
    hd.endElement("","",DirectoryEntriesXMLConstants.FILE_TAG);
  }

  private static void writeDirectory(TransformerHandler hd, DirectoryDescription directory) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // Name
    String name=directory.getName();
    attrs.addAttribute("","",DirectoryEntriesXMLConstants.ENTRY_NAME_ATTR,XmlWriter.CDATA,name);
    hd.startElement("","",DirectoryEntriesXMLConstants.DIRECTORY_TAG,attrs);
    // Child entries
    for(DirectoryEntryDescription child : directory.getEntries())
    {
      writeEntry(hd,child);
    }
    hd.endElement("","",DirectoryEntriesXMLConstants.DIRECTORY_TAG);
  }
}
