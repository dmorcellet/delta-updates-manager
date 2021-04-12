package delta.updates.engine;

import java.io.File;

import org.apache.log4j.Logger;

import delta.downloads.Downloader;
import delta.updates.data.SoftwareDescription;

/**
 * Another version of the update engine.
 * @author DAM
 */
public class UpdateEngine2
{
  private static final Logger LOGGER=Logger.getLogger(UpdateEngine2.class);

  private LocalDataManager _localData;
  private RemoteDataManager _remoteData;

  /**
   * Constructor.
   * @param downloader Downloader.
   */
  public UpdateEngine2(Downloader downloader)
  {
    _remoteData=new RemoteDataManager(downloader);
    File rootDir=new File(".");
    _localData=new LocalDataManager(rootDir);
  }

  /**
   * Perform update.
   */
  public void doIt()
  {
    SoftwareDescription localDescription=_localData.getSoftware();
    if (localDescription==null)
    {
      return;
    }
    String descriptionURL=localDescription.getDescriptionURL();
    SoftwareDescription current=_remoteData.loadCurrentDescription(descriptionURL);
    if (current==null)
    {
      return;
    }
  }
}
