package delta.updates.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.utils.FileUtils;

/**
 * Integrates new packages into the software installation.
 * @author DAM
 */
public class PackageIntegrator
{
  private static final Logger LOGGER=LoggerFactory.getLogger(PackageIntegrator.class);

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
    if (ok)
    {
      ok=_localData.writeSoftware();
    }
    return ok;
  }

  private boolean writePackage(SoftwarePackageUsage packageUsage)
  {
    boolean ok;
    try
    {
      SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
      ArchivedContents contents=packageDescription.getContents();
      if (contents!=null)
      {
        File packageDir=_workspace.getPackageRootDir(packageUsage.getPackage());
        applyUpdates(contents,packageDir);
      }
      applyDeletes(packageDescription.getEntriesToDelete());
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
    if (contents!=null)
    {
      for(DirectoryEntryDescription entry : contents.getEntries())
      {
        apply(entry,packageDir);
      }
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
      try
      {
        FileUtils.move(from.toPath(),to.toPath());
      }
      catch(IOException ioe)
      {
        throw new IllegalStateException("Cannot move file "+from+" to "+to, ioe);
      }
    }
    else
    {
      // Create directory
      File toDir=_localData.getRootDir();
      File to=new File(toDir,path);
      if (!to.exists())
      {
        boolean ok=to.mkdirs();
        if (!ok)
        {
          throw new IllegalStateException("Cannot create directory "+to);
        }
      }
      // Handle child files
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        apply(childEntry,packageDir);
      }
    }
  }

  private void applyDeletes(List<String> paths)
  {
    for(String path : paths)
    {
      applyDelete(path);
    }
  }

  private void applyDelete(String path)
  {
    File toDir=_localData.getRootDir();
    File to=new File(toDir,path);
    if (to.exists())
    {
      boolean ok=to.delete();
      if (!ok)
      {
        throw new IllegalStateException("Cannot delete file "+to);
      }
    }
  }
}
