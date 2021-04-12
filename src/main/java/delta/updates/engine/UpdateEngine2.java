package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.downloads.Downloader;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.Version;

/**
 * Another version of the update engine.
 * @author DAM
 */
public class UpdateEngine2
{
  private LocalDataManager _localData;
  private RemoteDataManager _remoteData;
  private PackagesWorkspace _workspace;

  /**
   * Constructor.
   * @param downloader Downloader.
   */
  public UpdateEngine2(Downloader downloader)
  {
    _remoteData=new RemoteDataManager(downloader);
    File rootDir=new File(".");
    _localData=new LocalDataManager(rootDir);
    File tmpDir=new File("__tmp");
    _workspace=new PackagesWorkspace(downloader,tmpDir);
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
    SoftwareDescription remoteDescription=_remoteData.loadCurrentDescription(descriptionURL);
    if (remoteDescription==null)
    {
      return;
    }
    boolean same=compareDescriptions(localDescription,remoteDescription);
    if (same)
    {
      return;
    }
    List<SoftwarePackageUsage> neededPackages=getNeededPackages(localDescription,remoteDescription);
    if (neededPackages.size()==0)
    {
      return;
    }
    handleNeededPackages(neededPackages);
  }

  private boolean compareDescriptions(SoftwareDescription local, SoftwareDescription remote)
  {
    Version localVersion=local.getVersion();
    Version remoteVersion=remote.getVersion();
    if (localVersion.getId()>=remoteVersion.getId())
    {
      return true;
    }
    return false;
  }

  private List<SoftwarePackageUsage> getNeededPackages(SoftwareDescription local, SoftwareDescription remote)
  {
    List<SoftwarePackageUsage> ret=new ArrayList<SoftwarePackageUsage>();
    for(SoftwarePackageUsage remotePackage : remote.getPackages())
    {
      SoftwareReference packageReference=remotePackage.getPackage();
      boolean hasPackage=local.hasPackage(packageReference);
      if (!hasPackage)
      {
        ret.add(remotePackage);
      }
    }
    return ret;
  }

  private void handleNeededPackages(List<SoftwarePackageUsage> neededPackages)
  {
    for(SoftwarePackageUsage neededPackage : neededPackages)
    {
      _remoteData.resolvePackage(neededPackage);
      _workspace.getPackage(neededPackage);
    }
  }
}
