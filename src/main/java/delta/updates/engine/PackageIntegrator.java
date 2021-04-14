package delta.updates.engine;

import java.io.File;

import org.apache.log4j.Logger;

import delta.updates.data.ArchivedContents;
import delta.updates.data.ContentsDescription;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;

/**
 * Integrates new packages into the software installation.
 * @author DAM
 */
public class PackageIntegrator
{
  private static final Logger LOGGER=Logger.getLogger(PackageIntegrator.class);

  private LocalDataManager _localData;
  private PackagesWorkspace _workspace;

  /**
   * Constructor.
   * @param localData Local data manager.
   * @param workspace Packages workspace.
   */
  public PackageIntegrator(LocalDataManager localData, PackagesWorkspace workspace)
  {
    _localData=localData;
    _workspace=workspace;
  }

  /**
   * Handle a package.
   * @param packageUsage Package usage.
   * @return <code>true</code> if successfull, <ocde>false</code> otherwise.
   */
  public boolean doIt(SoftwarePackageUsage packageUsage)
  {
    boolean ok=writePackage(packageUsage);
    if (ok)
    {
      // Update metadata
      SoftwareDescription localSoftware=_localData.getSoftware();
      localSoftware.addPackage(packageUsage);
      ok=_localData.writePackage(packageUsage);
    }
    return ok;
  }

  private boolean writePackage(SoftwarePackageUsage packageUsage)
  {
    boolean ok;
    try
    {
      SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
      ContentsDescription contents=packageDescription.getContents();
      File packageDir=_workspace.getPackageRootDir(packageUsage.getPackage());
      applyUpdates((ArchivedContents)contents,packageDir);
      ok=true;
    }
    catch(Exception e)
    {
      LOGGER.warn("Could not integrate package "+packageUsage.getPackage(),e);
      ok=false;
    }
    return ok;
  }

  private void applyUpdates(ArchivedContents contents, File packageDir)
  {
    for(DirectoryEntryDescription entry : contents.getEntries())
    {
      apply(entry,packageDir);
    }
  }

  private void apply(DirectoryEntryDescription entry, File packageDir)
  {
    String path=EntryUtils.getPath(entry);
    if (entry instanceof FileDescription)
    {
      // Move file
      File from=new File(packageDir,path);
      File toDir=_localData.getRootDir();
      File to=new File(toDir,path);
      boolean ok=from.renameTo(to);
      if (!ok)
      {
        throw new IllegalStateException("Cannot rename file "+from+" to "+to);
      }
    }
    else
    {
      // Create directory
      File toDir=_localData.getRootDir();
      File to=new File(toDir,path);
      boolean ok=to.mkdirs();
      if (!ok)
      {
        throw new IllegalStateException("Cannot create directory "+to);
      }
      // Handle child files
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        apply(childEntry,packageDir);
      }
    }
  }
}
