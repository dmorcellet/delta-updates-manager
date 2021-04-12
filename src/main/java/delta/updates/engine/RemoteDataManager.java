package delta.updates.engine;

import java.io.ByteArrayInputStream;

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
 * Remote data manager for the updates manager:
 * <ul>
 * <li>current software,
 * <li>current packages definitions.
 * </ul>
 * @author DAM
 */
public class RemoteDataManager
{
  private static final Logger LOGGER=Logger.getLogger(RemoteDataManager.class);

  private Downloader _downloader;

  /**
   * Constructor.
   * @param downloader Downloader.
   */
  public RemoteDataManager(Downloader downloader)
  {
    _downloader=downloader;
  }

  /**
   * Load the current software description from remote.
   * @param descriptionURL URL to use.
   * @return a software description or <code>null</code> if not found.
   */
  public SoftwareDescription loadCurrentDescription(String descriptionURL)
  {
    byte[] xmlDoc=downloadData(descriptionURL);
    if (xmlDoc==null)
    {
      return null;
    }
    SoftwareDescription ret=parseSoftwareDescription(xmlDoc);
    return ret;
  }

  /**
   * Resolve a package usage (find its detailed description).
   * @param packageUsage Targeted package usage.
   */
  public void resolvePackage(SoftwarePackageUsage packageUsage)
  {
    String descriptionURL=packageUsage.getDescriptionURL();
    SoftwarePackageDescription packageDescription=loadPackageDescription(descriptionURL);
    packageUsage.setDetailedDescription(packageDescription);
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
