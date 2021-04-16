package delta.updates.engine;

import java.util.List;

import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;

/**
 * Evaluator for resources needs of an update.
 * @author DAM
 */
public class ResourcesEvaluator
{
  /**
   * Perform assessment.
   * @param packages Packages to use.
   * @return An assessment.
   */
  public ResourcesAssessment doIt(List<SoftwarePackageUsage> packages)
  {
    int packagesCount=packages.size();
    long downloadSize=getDownloadSize(packages);
    long diskSize=getTotalDiskSize(packages);
    return new ResourcesAssessment(packagesCount,downloadSize,diskSize);
  }

  private long getDownloadSize(List<SoftwarePackageUsage> packages)
  {
    long size=0;
    for(SoftwarePackageUsage packageUsage : packages)
    {
      long packageSize=getPackageSize(packageUsage);
      size+=packageSize;
    }
    return size;
  }

  private long getTotalDiskSize(List<SoftwarePackageUsage> packages)
  {
    long size=0;
    for(SoftwarePackageUsage packageUsage : packages)
    {
      long packageSize=assessPackageSize(packageUsage);
      size+=packageSize;
    }
    return size;
  }

  private long assessPackageSize(SoftwarePackageUsage packageUsage)
  {
    // ZIP size
    long packageSize=getPackageSize(packageUsage);
    // Total size of files
    SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
    ArchivedContents contents=packageDescription.getContents();
    long expandedSize=0;
    if (contents!=null)
    {
      for(DirectoryEntryDescription entry : contents.getEntries())
      {
        long entrySize=getEntrySize(entry);
        expandedSize+=entrySize;
      }
    }
    long total=packageSize+expandedSize;
    return total;
  }

  private long getEntrySize(DirectoryEntryDescription entry)
  {
    if (entry instanceof FileDescription)
    {
      long size=((FileDescription)entry).getSize();
      return size;
    }
    else if (entry instanceof DirectoryDescription)
    {
      long totalSize=0;
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        long size=getEntrySize(childEntry);
        totalSize+=size;
      }
      return totalSize;
    }
    return 0;
  }

  private long getPackageSize(SoftwarePackageUsage packageUsage)
  {
    SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
    ArchivedContents contents=packageDescription.getContents();
    if (contents==null)
    {
      return 0;
    }
    FileDescription dataFile=contents.getDataFile();
    long packageSize=dataFile.getSize();
    return packageSize;
  }
}
