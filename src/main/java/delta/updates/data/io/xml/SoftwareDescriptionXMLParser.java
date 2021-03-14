package delta.updates.data.io.xml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.ContentsDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageReference;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.Version;

/**
 * Parser for software descriptions stored in XML.
 * @author DAM
 */
public class SoftwareDescriptionXMLParser
{
  /**
   * Load a software description from an XML tag.
   * @param root Root XML tag.
   * @return the loaded data.
   */
  public static SoftwareDescription parseSoftwareDescription(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    // ID
    int id=DOMParsingTools.getIntAttribute(attrs,SoftwareDescriptionXMLConstants.ID_ATTR,0);
    SoftwareDescription ret=new SoftwareDescription(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.NAME_ATTR,"");
    ret.setName(name);
    // Version
    Version version=parseVersion(attrs);
    ret.setVersion(version);
    // Date
    long date=DOMParsingTools.getLongAttribute(attrs,SoftwareDescriptionXMLConstants.SOFTWARE_DATE_ATTR,0); 
    ret.setDate(date);
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.DESCRIPTION_ATTR,"");
    ret.setDescription(description);
    // Package usages
    List<Element> packageTags=DOMParsingTools.getChildTagsByName(root,SoftwareDescriptionXMLConstants.PACKAGE_USAGE_TAG);
    for(Element packageTag : packageTags)
    {
      SoftwarePackageUsage packageUsage=parsePackageUsage(packageTag);
      ret.addPackage(packageUsage);
    }
    return ret;
  }

  private static SoftwarePackageUsage parsePackageUsage(Element packageUsageTag)
  {
    NamedNodeMap attrs=packageUsageTag.getAttributes();
    // Reference
    SoftwarePackageReference packageReference=parsePackageReference(packageUsageTag);
    SoftwarePackageUsage ret=new SoftwarePackageUsage(packageReference);
    // Relative path
    String relativePath=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.PACKAGE_USAGE_RELATIVE_PATH_ATTR,"");
    ret.setRelativePath(relativePath);
    // Description URL
    String descriptionURL=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.PACKAGE_USAGE_URL_ATTR,"");
    ret.setDescriptionURL(descriptionURL);
    return ret;
  }

  /**
   * Build a software package from an XML tag.
   * @param root Root XML tag.
   * @return A software package.
   */
  public static SoftwarePackageDescription parseSoftwarePackage(Element root)
  {
    SoftwarePackageDescription ret=new SoftwarePackageDescription();
    // Reference
    SoftwarePackageReference ref=parsePackageReference(root);
    ret.setReference(ref);
    // Source URLs
    List<Element> sourceTags=DOMParsingTools.getChildTagsByName(root,SoftwareDescriptionXMLConstants.SOURCE_URL_TAG);
    for(Element sourceTag : sourceTags)
    {
      String url=DOMParsingTools.getStringAttribute(sourceTag.getAttributes(),SoftwareDescriptionXMLConstants.URL_ATTR,"");
      ret.addSourceURL(url);
    }
    // Contents
    Element contentsTags=DOMParsingTools.getChildTagByName(root,ContentsXMLConstants.ARCHIVE_TAG);
    if (contentsTags!=null)
    {
      ContentsDescription contents=ContentsXMLParser.parseContentsDescriptionTag(contentsTags);
      ret.setContents(contents);
    }
    return ret;
  }

  /**
   * Load a software package reference an XML tag.
   * @param root Root XML tag.
   * @return the loaded data.
   */
  private static SoftwarePackageReference parsePackageReference(Element root)
  {
    NamedNodeMap attrs=root.getAttributes();
    // ID
    int id=DOMParsingTools.getIntAttribute(attrs,SoftwareDescriptionXMLConstants.ID_ATTR,0);
    SoftwarePackageReference ret=new SoftwarePackageReference(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.NAME_ATTR,"");
    ret.setName(name);
    // Version
    Version version=parseVersion(attrs);
    ret.setVersion(version);
    return ret;
  }

  private static Version parseVersion(NamedNodeMap attrs)
  {
    // Version
    int version=DOMParsingTools.getIntAttribute(attrs,SoftwareDescriptionXMLConstants.VERSION_ATTR,0);
    // Version label
    String versionLabel=DOMParsingTools.getStringAttribute(attrs,SoftwareDescriptionXMLConstants.VERSION_LABEL_ATTR,"");
    return new Version(version,versionLabel);
  }
}
