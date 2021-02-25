package delta.updates.contents.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.contents.ArchivedContents;
import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.RawContents;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;

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
    hd.startElement("","",ContentsXMLConstants.RAW_FILE_TAG,new AttributesImpl());
    FileDescription source=rawContents.getDataFile();
    writeFile(hd,ContentsXMLConstants.FROM_TAG,source);
    hd.endElement("","",ContentsXMLConstants.RAW_FILE_TAG);
  }

  private static void writeArchive(TransformerHandler hd, ArchivedContents archivedContents) throws SAXException
  {
    hd.startElement("","",ContentsXMLConstants.ARCHIVE_TAG,new AttributesImpl());
    FileDescription source=archivedContents.getDataFile();
    writeFile(hd,ContentsXMLConstants.FROM_TAG,source);
    for(FileDescription childFile : archivedContents.getFiles())
    {
      writeFile(hd,ContentsXMLConstants.FILE_TAG,childFile);
    }
    hd.endElement("","",ContentsXMLConstants.ARCHIVE_TAG);
  }

  private static void writeFile(TransformerHandler hd, String tagName, FileDescription file) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // path
    String path=EntryUtils.getPath(file);
    attrs.addAttribute("","",ContentsXMLConstants.FILE_PATH_ATTR,XmlWriter.CDATA,path);
    // Size
    long size=file.getSize();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_SIZE_ATTR,XmlWriter.CDATA,String.valueOf(size));
    // CRC
    long crc=file.getCRC();
    attrs.addAttribute("","",ContentsXMLConstants.FILE_CRC_ATTR,XmlWriter.CDATA,String.valueOf(crc));
    hd.startElement("","",tagName,attrs);
    hd.endElement("","",tagName);
  }
}
