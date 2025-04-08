package delta.updates.data.io.xml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;

/**
 * Parser for directory entries stored in XML.
 * @author DAM
 */
public class DirectoryEntriesXMLParser
{
  private static final Logger LOGGER=LoggerFactory.getLogger(DirectoryEntriesXMLParser.class);

  /**
   * Build a directory entry from an XML tag.
   * @param root Root XML tag.
   * @return A directory entry.
   */
  public static DirectoryEntryDescription parseEntry(Element root)
  {
    String tagName=root.getTagName();
    if (DirectoryEntriesXMLConstants.DIRECTORY_TAG.equals(tagName))
    {
      return parseDirectoryTag(root);
    }
    if (DirectoryEntriesXMLConstants.FILE_TAG.equals(tagName))
    {
      return parseFileTag(root);
    }
    LOGGER.warn("Unmanaged tag: {}",tagName);
    return null;
  }

  private static DirectoryDescription parseDirectoryTag(Element directoryTag)
  {
    NamedNodeMap attrs=directoryTag.getAttributes();
    DirectoryDescription ret=new DirectoryDescription();
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,DirectoryEntriesXMLConstants.ENTRY_NAME_ATTR,"");
    ret.setName(name);
    // Children
    List<Element> childTags=DOMParsingTools.getChildTags(directoryTag);
    for(Element childTag : childTags)
    {
      DirectoryEntryDescription entry=parseEntry(childTag);
      ret.addEntry(entry);
      entry.setParent(ret);
    }
    return ret;
  }

  private static FileDescription parseFileTag(Element fileTag)
  {
    NamedNodeMap attrs=fileTag.getAttributes();
    FileDescription ret=new FileDescription();
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,DirectoryEntriesXMLConstants.ENTRY_NAME_ATTR,"");
    ret.setName(name);
    // Size
    long size=DOMParsingTools.getLongAttribute(attrs,DirectoryEntriesXMLConstants.FILE_SIZE_ATTR,0);
    ret.setSize(size);
    // CRC
    long crc=DOMParsingTools.getLongAttribute(attrs,DirectoryEntriesXMLConstants.FILE_CRC_ATTR,0);
    ret.setCRC(crc);
    return ret;
  }
}
