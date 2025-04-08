package delta.updates.data.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;

/**
 * Writes contents descriptions to XML files.
 * @author DAM
 */
public class ContentsXMLWriter
{
  /**
   * Write a contents description to the given output.
   * @param hd Output.
   * @param archivedContents Contents to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeContents(TransformerHandler hd, ArchivedContents archivedContents) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    FileDescription source=archivedContents.getDataFile();
    writeFileAttrs(attrs,source);
    hd.startElement("","",ContentsXMLConstants.ARCHIVE_TAG,attrs);
    for(DirectoryEntryDescription entry : archivedContents.getEntries())
    {
      DirectoryEntriesXMLWriter.writeEntry(hd,entry);
    }
    hd.endElement("","",ContentsXMLConstants.ARCHIVE_TAG);
  }

  private static void writeFileAttrs(AttributesImpl attrs, FileDescription file)
  {
    // path
    String path=EntryUtils.getPath(file);
    attrs.addAttribute("","",ContentsXMLConstants.FILE_NAME_ATTR,XmlWriter.CDATA,path);
    // Size
    long size=file.getSize();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_SIZE_ATTR,XmlWriter.CDATA,String.valueOf(size));
    // CRC
    long crc=file.getCRC();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_CRC_ATTR,XmlWriter.CDATA,String.valueOf(crc));
  }
}
