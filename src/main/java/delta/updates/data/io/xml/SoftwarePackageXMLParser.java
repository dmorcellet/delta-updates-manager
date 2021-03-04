package delta.updates.data.io.xml;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.SoftwarePackage;
import delta.updates.data.SoftwarePackageSummary;

/**
 * Parser for software packages stored in XML.
 * @author DAM
 */
public class SoftwarePackageXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(SoftwarePackageXMLParser.class);

  /**
   * Load a software package summary from an XML tag.
   * @param root Root XML tag.
   * @param storage Storage for loaded data.
   */
  public static void parseSoftwarePackageSummary(Element root, SoftwarePackageSummary storage)
  {
    NamedNodeMap attrs=root.getAttributes();
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,SoftwarePackageXMLConstants.PACKAGE_NAME_ATTR,"");
    storage.setName(name);
    // Version
    int version=DOMParsingTools.getIntAttribute(attrs,SoftwarePackageXMLConstants.VERSION_ATTR,0);
    storage.setVersion(version);
    // Version label
    String versionLabel=DOMParsingTools.getStringAttribute(attrs,SoftwarePackageXMLConstants.VERSION_LABEL_ATTR,"");
    storage.setVersionLabel(versionLabel);
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,SoftwarePackageXMLConstants.DESCRIPTION_ATTR,"");
    storage.setDescription(description);
  }

  /**
   * Build a software package from an XML tag.
   * @param root Root XML tag.
   * @return A software package.
   */
  public static SoftwarePackage parseSoftwarePackage(Element root)
  {
    SoftwarePackage ret=new SoftwarePackage();
    SoftwarePackageSummary summary=ret.getSummary();
    parseSoftwarePackageSummary(root,summary);
    // Entries
    List<Element> childTags=DOMParsingTools.getChildTags(root);
    int count=childTags.size();
    if (count==1)
    {
      DirectoryEntryDescription entry=DirectoryEntriesXMLParser.parseEntry(childTags.get(0));
      ret.setRootEntry(entry);
    }
    else
    {
      LOGGER.warn("Unexpected child tags count: "+count);
    }
    return ret;
  }
}
