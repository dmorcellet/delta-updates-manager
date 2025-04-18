package delta.updates.data.io.xml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;

/**
 * Parser for contents descriptions stored in XML.
 * @author DAM
 */
public class ContentsXMLParser
{
  private static final Logger LOGGER=LoggerFactory.getLogger(ContentsXMLParser.class);

  /**
   * Build a contents description from an XML tag.
   * @param root Root XML tag.
   * @return A contents description or <code>null</code>.
   */
  public static ArchivedContents parseContentsDescriptionTag(Element root)
  {
    String tagName=root.getTagName();
    if (ContentsXMLConstants.ARCHIVE_TAG.equals(tagName))
    {
      return parseArchiveTag(root);
    }
    LOGGER.warn("Unmanaged tag: {}",tagName);
    return null;
  }

  private static ArchivedContents parseArchiveTag(Element archiveTag)
  {
    FileDescription source=parseFileAttrs(archiveTag);
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

  private static FileDescription parseFileAttrs(Element fileTag)
  {
    NamedNodeMap attrs=fileTag.getAttributes();
    FileDescription file=new FileDescription();
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,ContentsXMLConstants.FILE_NAME_ATTR,"");
    file.setName(name);
    // Size
    long size=DOMParsingTools.getLongAttribute(attrs,ContentsXMLConstants.FILE_SIZE_ATTR,0);
    file.setSize(size);
    // CRC
    long crc=DOMParsingTools.getLongAttribute(attrs,ContentsXMLConstants.FILE_CRC_ATTR,0);
    file.setCRC(crc);
    return file;
  }
}
