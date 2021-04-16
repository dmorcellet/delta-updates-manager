package delta.updates.data.io.xml;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlWriter;
import delta.updates.data.ArchivedContents;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.Version;

/**
 * Writes software/package descriptions to XML files.
 * @author DAM
 */
public class SoftwareDescriptionXMLWriter
{
  /**
   * Write a software description to the given output.
   * @param hd Output.
   * @param software Data to write.
   * @throws SAXException If an error occurs.
   */
  public static void writeSoftware(TransformerHandler hd, SoftwareDescription software) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // ID
    int id=software.getId();
    attrs.addAttribute("","",SoftwareDescriptionXMLConstants.ID_ATTR,XmlWriter.CDATA,String.valueOf(id));
    // Name
    String name=software.getName();
    if (name.length()>0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.NAME_ATTR,XmlWriter.CDATA,name);
    }
    // Version
    writeVersion(attrs,software.getVersion());
    // Date
    long date=software.getDate();
    if (date!=0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.SOFTWARE_DATE_ATTR,XmlWriter.CDATA,String.valueOf(date));
    }
    // Contents Description
    String contentsDescription=software.getContentsDescription();
    if (contentsDescription.length()>0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.CONTENTS_DESCRIPTION_ATTR,XmlWriter.CDATA,contentsDescription);
    }
    // Description URL
    String descriptionURL=software.getDescriptionURL();
    if (descriptionURL.length()>0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.DESCRIPTION_URL_ATTR,XmlWriter.CDATA,descriptionURL);
    }
    hd.startElement("","",SoftwareDescriptionXMLConstants.SOFTWARE_TAG,attrs);
    // Package usages
    for(SoftwarePackageUsage packageUsage : software.getPackages())
    {
      writePackageUsage(hd,packageUsage);
    }
    hd.endElement("","",SoftwareDescriptionXMLConstants.SOFTWARE_TAG);
  }

  private static void writePackageUsage(TransformerHandler hd, SoftwarePackageUsage packageUsage) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // Reference
    SoftwareReference packageReference=packageUsage.getPackage();
    writePackageReference(attrs,packageReference);
    // Relative path
    String relativePath=packageUsage.getRelativePath();
    attrs.addAttribute("","",SoftwareDescriptionXMLConstants.PACKAGE_USAGE_RELATIVE_PATH_ATTR,XmlWriter.CDATA,relativePath);
    // Description URL
    String url=packageUsage.getDescriptionURL();
    attrs.addAttribute("","",SoftwareDescriptionXMLConstants.PACKAGE_USAGE_URL_ATTR,XmlWriter.CDATA,url);
    hd.startElement("","",SoftwareDescriptionXMLConstants.PACKAGE_USAGE_TAG,attrs);
    hd.endElement("","",SoftwareDescriptionXMLConstants.PACKAGE_USAGE_TAG);
  }

  /**
   * Write a package description to the given output.
   * @param hd Output.
   * @param packageDescription Data to write.
   * @throws SAXException If an error occurs.
   */
  public static void writePackageDescription(TransformerHandler hd, SoftwarePackageDescription packageDescription) throws SAXException
  {
    AttributesImpl attrs=new AttributesImpl();
    // Reference
    SoftwareReference packageReference=packageDescription.getReference();
    writePackageReference(attrs,packageReference);
    hd.startElement("","",SoftwareDescriptionXMLConstants.PACKAGE_TAG,attrs);
    // Source URLs
    for(String sourceURL : packageDescription.getSourceURLs())
    {
      AttributesImpl sourceURLAttrs=new AttributesImpl();
      sourceURLAttrs.addAttribute("","",SoftwareDescriptionXMLConstants.URL_ATTR,XmlWriter.CDATA,sourceURL);
      hd.startElement("","",SoftwareDescriptionXMLConstants.SOURCE_URL_TAG,sourceURLAttrs);
      hd.endElement("","",SoftwareDescriptionXMLConstants.SOURCE_URL_TAG);
    }
    // Contents
    ArchivedContents contents=packageDescription.getContents();
    if (contents!=null)
    {
      ContentsXMLWriter.writeContents(hd,contents);
    }
    // Entries to delete
    for(String path : packageDescription.getEntriesToDelete())
    {
      AttributesImpl deleteAttrs=new AttributesImpl();
      deleteAttrs.addAttribute("","",SoftwareDescriptionXMLConstants.DELETE_PATH_ATTR,XmlWriter.CDATA,path);
      hd.startElement("","",SoftwareDescriptionXMLConstants.DELETE_TAG,deleteAttrs);
      hd.endElement("","",SoftwareDescriptionXMLConstants.DELETE_TAG);
    }
    hd.endElement("","",SoftwareDescriptionXMLConstants.PACKAGE_TAG);
  }

  private static void writePackageReference(AttributesImpl attrs, SoftwareReference packageReference)
  {
    // ID
    int id=packageReference.getId();
    attrs.addAttribute("","",SoftwareDescriptionXMLConstants.ID_ATTR,XmlWriter.CDATA,String.valueOf(id));
    // Name
    String name=packageReference.getName();
    if (name.length()>0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.NAME_ATTR,XmlWriter.CDATA,name);
    }
    // Version
    writeVersion(attrs,packageReference.getVersion());
  }

  private static void writeVersion(AttributesImpl attrs, Version version)
  {
    // Version
    int versionCode=version.getId();
    if (versionCode!=0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.VERSION_ATTR,XmlWriter.CDATA,String.valueOf(versionCode));
    }
    // Version label
    String versionLabel=version.getName();
    if (versionLabel.length()>0)
    {
      attrs.addAttribute("","",SoftwareDescriptionXMLConstants.VERSION_LABEL_ATTR,XmlWriter.CDATA,versionLabel);
    }
  }
}
