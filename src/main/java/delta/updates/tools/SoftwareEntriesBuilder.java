package delta.updates.tools;

import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntriesBuilder;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;

/**
 * Builds directory description for softwares.
 * @author DAM
 */
public class SoftwareEntriesBuilder
{
  private EntriesBuilder _entriesBuilder;

  /**
   * Build a directory description for a software.
   * @param software Software to use.
   * @return A directory description.
   */
  public DirectoryDescription build(SoftwareDescription software)
  {
    _entriesBuilder=new EntriesBuilder();
    for(SoftwarePackageUsage packageUsage : software.getPackages())
    {
      SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
      handlePackage(packageDescription);
    }
    DirectoryDescription root=_entriesBuilder.buildDirectoryFromPath("");
    _entriesBuilder=null;
    return root;
  }

  private void handlePackage(SoftwarePackageDescription packageDescription)
  {
    ArchivedContents contents=(ArchivedContents)packageDescription.getContents();
    for(DirectoryEntryDescription entry : contents.getEntries())
    {
      handleEntry(entry);
    }
  }

  private void handleEntry(DirectoryEntryDescription entry)
  {
    String path=EntryUtils.getPath(entry);
    if (".".equals(path)) path="";
    if (path.startsWith("./")) path=path.substring(2);
    DirectoryEntryDescription newEntry=null;
    if (entry instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)entry;
      newEntry=_entriesBuilder.buildDirectoryFromPath(path);
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        handleEntry(childEntry);
      }
    }
    else if (entry instanceof FileDescription)
    {
      FileDescription fileEntry=(FileDescription)entry;
      FileDescription newFileEntry=_entriesBuilder.buildFileFromPath(path);
      newFileEntry.setCRC(fileEntry.getCRC());
      newFileEntry.setSize(fileEntry.getSize());
      newEntry=newFileEntry;
    }
  }
}
