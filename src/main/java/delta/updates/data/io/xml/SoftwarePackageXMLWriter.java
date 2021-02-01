package delta.updates.data.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.SoftwarePackage;

/**
 * Writes software packages to XML files.
 * @author DAM
 */
public class SoftwarePackageXMLWriter
{
  /**
   * Write a software package to the given output.
   * @param hd Output.
   * @param softwarePackage Data to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeSoftwarePackage(TransformerHandler hd, SoftwarePackage softwarePackage) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // Name
    String name=softwarePackage.getName();
    if (name.length()>0)
    {
      attrs.addAttribute("","",SoftwarePackageXMLConstants.PACKAGE_NAME_ATTR,XmlWriter.CDATA,name);
    }
    // Version
    int version=softwarePackage.getVersion();
    if (version!=0)
    {
      attrs.addAttribute("","",SoftwarePackageXMLConstants.VERSION_ATTR,XmlWriter.CDATA,String.valueOf(version));
    }
    // Version label
    String versionLabel=softwarePackage.getVersionLabel();
    if (versionLabel.length()>0)
    {
      attrs.addAttribute("","",SoftwarePackageXMLConstants.VERSION_LABEL_ATTR,XmlWriter.CDATA,versionLabel);
    }
    // Description
    String description=softwarePackage.getDescription();
    if (description.length()>0)
    {
      attrs.addAttribute("","",SoftwarePackageXMLConstants.DESCRIPTION_ATTR,XmlWriter.CDATA,description);
    }
    hd.startElement("","",SoftwarePackageXMLConstants.PACKAGE_TAG,attrs);
    // Files
    DirectoryEntryDescription entry=softwarePackage.getFiles();
    DirectoryEntriesXMLWriter.writeEntry(hd,entry);
    hd.endElement("","",SoftwarePackageXMLConstants.PACKAGE_TAG);
  }
}
