package delta.updates.engine.providers;

import java.io.File;

import delta.common.utils.files.archives.ArchiveDeflater;
import delta.updates.contents.ArchivedContents;
import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.RawContents;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;

/**
 * Smart provider, that uses a contents manager to
 * decide which files to get to satisfy requests.
 * @author DAM
 */
public class SmartProvider implements FileProvider
{
  private FileProvider _realProvider;
  private ContentsManager _contentsMgr;

  /**
   * Constructor.
   * @param contentsMgr Contents manager.
   * @param realProvider Provider to use.
   */
  public SmartProvider(ContentsManager contentsMgr, FileProvider realProvider)
  {
    _contentsMgr=contentsMgr;
    _realProvider=realProvider;
  }

  @Override
  public boolean getFile(FileDescription entry, File toDir)
  {
    ContentsDescription contents=_contentsMgr.getSourceContents(entry);
    if (contents==null)
    {
      return false;
    }
    String path=EntryUtils.getPath(entry);
    File targetFile=new File(toDir,path);
    if (targetFile.exists())
    {
      return true;
    }
    if (contents instanceof RawContents)
    {
      RawContents rawContents=(RawContents)contents;
      return _realProvider.getFile(rawContents.getDataFile(),toDir);
    }
    else if (contents instanceof ArchivedContents)
    {
      ArchivedContents archivedContents=(ArchivedContents)contents;
      FileDescription archiveDescription=archivedContents.getDataFile();
      boolean gotArchive=_realProvider.getFile(archiveDescription,toDir);
      if (!gotArchive)
      {
        return false;
      }
      boolean ret=false;
      String archivePath=EntryUtils.getPath(archiveDescription);
      File archiveFile=new File(toDir,archivePath);
      String basePath=EntryUtils.getPath(archiveDescription.getParent());
      File targetDir=new File(toDir,basePath);
      ArchiveDeflater deflater=new ArchiveDeflater(archiveFile,targetDir);
      boolean ok=deflater.go();
      if (!ok)
      {
        ret=false;
      }
      ret=targetFile.exists();
      archiveFile.delete();
      return ret;
    }
    return false;
  }
}
