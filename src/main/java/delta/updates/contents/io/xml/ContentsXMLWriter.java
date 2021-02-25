package delta.updates.contents.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.contents.ArchivedContents;
import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.RawContents;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.io.xml.DirectoryEntriesXMLWriter;

/**
 * Writes contents descriptions to XML files.
 * @author DAM
 */
public class ContentsXMLWriter
{
  /**
   * Write a contents manager to the given output.
   * @param hd Output.
   * @param contentsMgr Contents to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeContentsManager(TransformerHandler hd, ContentsManager contentsMgr) throws SAXException
  {
    hd.startElement("","",ContentsXMLConstants.CONTENTS_TAG,new AttributesImpl());
    for(ContentsDescription contents : contentsMgr.getContents())
    {
      writeContents(hd,contents);
    }
    hd.endElement("","",ContentsXMLConstants.CONTENTS_TAG);
  }

  /**
   * Write a contents description to the given output.
   * @param hd Output.
   * @param contents Contents to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeContents(TransformerHandler hd, ContentsDescription contents) throws SAXException
  {
    if (contents instanceof RawContents)
    {
      writeRawFile(hd,(RawContents)contents);
    }
    else if (contents instanceof ArchivedContents)
    {
      writeArchive(hd,(ArchivedContents)contents);
    }
  }

  private static void writeRawFile(TransformerHandler hd, RawContents rawContents) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    FileDescription source=rawContents.getDataFile();
    writeFileAttrs(hd,attrs,source);
    hd.startElement("","",ContentsXMLConstants.RAW_FILE_TAG,attrs);
    hd.endElement("","",ContentsXMLConstants.RAW_FILE_TAG);
  }

  private static void writeArchive(TransformerHandler hd, ArchivedContents archivedContents) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    FileDescription source=archivedContents.getDataFile();
    writeFileAttrs(hd,attrs,source);
    hd.startElement("","",ContentsXMLConstants.ARCHIVE_TAG,attrs);
    for(DirectoryEntryDescription entry : archivedContents.getEntries())
    {
      DirectoryEntriesXMLWriter.writeEntry(hd,entry);
    }
    hd.endElement("","",ContentsXMLConstants.ARCHIVE_TAG);
  }

  private static void writeFileAttrs(TransformerHandler hd, AttributesImpl attrs, FileDescription file)
  {
    // path
    String path=EntryUtils.getPath(file);
    attrs.addAttribute("","",ContentsXMLConstants.FILE_PATH_ATTR,XmlWriter.CDATA,path);
    // Size
    long size=file.getSize();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_SIZE_ATTR,XmlWriter.CDATA,String.valueOf(size));
    // CRC
    long crc=file.getCRC();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_CRC_ATTR,XmlWriter.CDATA,String.valueOf(crc));
  }
}
