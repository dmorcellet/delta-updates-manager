package delta.updates.contents.io.xml;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.contents.ArchivedContents;
import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.RawContents;
import delta.updates.data.EntriesBuilder;
import delta.updates.data.FileDescription;

/**
 * Parser for contents descriptions stored in XML.
 * @author DAM
 */
public class ContentsXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(ContentsXMLParser.class);

  /**
   * Build a directory entry from an XML tag.
   * @param root Root XML tag.
   * @return A directory entry.
   */
  public static ContentsManager parseContentsManager(Element root)
  {
    EntriesBuilder entriesBuilder=new EntriesBuilder();
    ContentsManager contentsMgr=new ContentsManager();
    List<Element> childTags=DOMParsingTools.getChildTags(root);
    for(Element childTag : childTags)
    {
      ContentsDescription contents=parseContentsDescriptionTag(childTag,entriesBuilder);
      if (contents!=null)
      {
        contentsMgr.addContents(contents);
      }
    }
    return contentsMgr;
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
    Element fromTag=DOMParsingTools.getChildTagByName(rawFileTag,ContentsXMLConstants.FROM_TAG);
    FileDescription source=parseFileTag(fromTag,entriesBuilder);
    RawContents ret=new RawContents();
    ret.setDataFile(source);
    return ret;
  }

  private static ArchivedContents parseArchiveTag(Element archiveTag, EntriesBuilder entriesBuilder)
  {
    Element fromTag=DOMParsingTools.getChildTagByName(archiveTag,ContentsXMLConstants.FROM_TAG);
    FileDescription source=parseFileTag(fromTag,entriesBuilder);
    ArchivedContents ret=new ArchivedContents();
    ret.setDataFile(source);
    // Children
    List<Element> childTags=DOMParsingTools.getChildTagsByName(archiveTag,ContentsXMLConstants.FILE_TAG);
    for(Element childTag : childTags)
    {
      FileDescription childFile=parseFileTag(childTag,entriesBuilder);
      ret.addFile(childFile);
    }
    return ret;
  }

  private static FileDescription parseFileTag(Element fileTag, EntriesBuilder entriesBuilder)
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
