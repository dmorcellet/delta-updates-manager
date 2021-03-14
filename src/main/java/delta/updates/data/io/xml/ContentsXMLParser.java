package delta.updates.data.io.xml;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.ArchivedContents;
import delta.updates.data.ContentsDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntriesBuilder;
import delta.updates.data.FileDescription;
import delta.updates.data.RawContents;

/**
 * Parser for contents descriptions stored in XML.
 * @author DAM
 */
public class ContentsXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(ContentsXMLParser.class);

  /**
   * Build a contents description from an XML tag.
   * @param root Root XML tag.
   * @return A contents description or <code>null</code>.
   */
  public static ContentsDescription parseContentsDescriptionTag(Element root)
  {
    EntriesBuilder entriesBuilder=new EntriesBuilder();
    return parseContentsDescriptionTag(root,entriesBuilder);
  }

  /**
   * Build a contents description from an XML tag.
   * @param root Root XML tag.
   * @param entriesBuilder Entries builder.
   * @return A contents description or <code>null</code>.
   */
  private static ContentsDescription parseContentsDescriptionTag(Element root, EntriesBuilder entriesBuilder)
  {
    String tagName=root.getTagName();
    if (ContentsXMLConstants.RAW_FILE_TAG.equals(tagName))
    {
      return parseRawFileTag(root,entriesBuilder);
    }
    if (ContentsXMLConstants.ARCHIVE_TAG.equals(tagName))
    {
      return parseArchiveTag(root,entriesBuilder);
    }
    LOGGER.warn("Unmanaged tag: "+tagName);
    return null;
  }

  private static RawContents parseRawFileTag(Element rawFileTag, EntriesBuilder entriesBuilder)
  {
    FileDescription source=parseFileAttrs(rawFileTag,entriesBuilder);
    RawContents ret=new RawContents();
    ret.setDataFile(source);
    return ret;
  }

  private static ArchivedContents parseArchiveTag(Element archiveTag, EntriesBuilder entriesBuilder)
  {
    FileDescription source=parseFileAttrs(archiveTag,entriesBuilder);
    ArchivedContents ret=new ArchivedContents();
    ret.setDataFile(source);
    // Children
    List<Element> childTags=DOMParsingTools.getChildTags(archiveTag);
    for(Element childTag : childTags)
    {
      DirectoryEntryDescription entry=DirectoryEntriesXMLParser.parseEntry(childTag);
      ret.addEntry(entry);
    }
    return ret;
  }

  private static FileDescription parseFileAttrs(Element fileTag, EntriesBuilder entriesBuilder)
  {
    NamedNodeMap attrs=fileTag.getAttributes();
    // Path
    String path=DOMParsingTools.getStringAttribute(attrs,ContentsXMLConstants.FILE_PATH_ATTR,"");
    FileDescription file=entriesBuilder.buildFileFromPath(path);
    // Size
    long size=DOMParsingTools.getLongAttribute(attrs,ContentsXMLConstants.FILE_SIZE_ATTR,0);
    file.setSize(size);
    // CRC
    long crc=DOMParsingTools.getLongAttribute(attrs,ContentsXMLConstants.FILE_CRC_ATTR,0);
    file.setCRC(crc);
    return file;
  }
}
