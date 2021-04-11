package delta.updates.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import delta.common.utils.xml.DOMParsingTools;
import delta.downloads.DownloadException;
import delta.downloads.Downloader;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.io.xml.SoftwareDescriptionXMLParser;

/**
 * Another version of the update engine.
 * @author DAM
 */
public class UpdateEngine2
{
  private static final Logger LOGGER=Logger.getLogger(UpdateEngine2.class);

  private LocalDataManager _localData;
  private Downloader _downloader;

  /**
   * Constructor.
   * @param downloader Downloader.
   */
  public UpdateEngine2(Downloader downloader)
  {
    _downloader=downloader;
    File rootDir=new File(".");
    _localData=new LocalDataManager(rootDir);
  }

  /**
   * Perform update.
   */
  public void doIt()
  {
    SoftwareDescription current=loadCurrentDescription();
    if (current==null)
    {
      return;
    }
  }

  private SoftwareDescription loadCurrentDescription()
  {
    SoftwareDescription localDescription=_localData.getSoftware();
    if (localDescription==null)
    {
      return null;
    }
    String descriptionURL=localDescription.getDescriptionURL();
    byte[] xmlDoc=downloadData(descriptionURL);
    if (xmlDoc==null)
    {
      return null;
    }
    SoftwareDescription ret=parseSoftwareDescription(xmlDoc);
    resolvePackages(ret);
    return ret;
  }

  private void resolvePackages(SoftwareDescription softwareDescription)
  {
    List<SoftwarePackageUsage> packages=softwareDescription.getPackages();
    for(SoftwarePackageUsage packageUsage : packages)
    {
      String descriptionURL=packageUsage.getDescriptionURL();
      SoftwarePackageDescription packageDescription=loadPackageDescription(descriptionURL);
      packageUsage.setDetailedDescription(packageDescription);
    }
  }

  private SoftwarePackageDescription loadPackageDescription(String descriptionURL)
  {
    byte[] xmlDoc=downloadData(descriptionURL);
    if (xmlDoc==null)
    {
      return null;
    }
    SoftwarePackageDescription ret=parseSoftwarePackageDescription(xmlDoc);
    return ret;
  }

  private SoftwareDescription parseSoftwareDescription(byte[] xmlDoc)
  {
    ByteArrayInputStream bis=new ByteArrayInputStream(xmlDoc);
    Element root=DOMParsingTools.parse(bis);
    SoftwareDescription softwareDescription=SoftwareDescriptionXMLParser.parseSoftwareDescription(root);
    return softwareDescription;
  }

  private SoftwarePackageDescription parseSoftwarePackageDescription(byte[] xmlDoc)
  {
    ByteArrayInputStream bis=new ByteArrayInputStream(xmlDoc);
    Element root=DOMParsingTools.parse(bis);
    SoftwarePackageDescription packageDescription=SoftwareDescriptionXMLParser.parseSoftwarePackage(root);
    return packageDescription;
  }

  private byte[] downloadData(String url)
  {
    byte[] buffer=null;
    try
    {
      buffer=_downloader.downloadBuffer(url);
    }
    catch(DownloadException downloadException)
    {
      LOGGER.error("Download error for "+url,downloadException);
    }
    return buffer;
  }
}
