package delta.updates.engine.providers;

import java.io.File;

import org.apache.log4j.Logger;

import delta.downloads.DownloadException;
import delta.downloads.Downloader;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;

/**
 * Data provider that downloads data using HTTP.
 * @author DAM
 */
public class HttpProvider
{
  private static final Logger LOGGER=Logger.getLogger(HttpProvider.class);

  private Downloader _downloader;
  private String _rootURL;
  private File _toDir;

  /**
   * Constructor.
   * @param downloader Downloader.
   * @param rootURL Root URL for downloads.
   * @param toDir Target directory for downloaded files.
   */
  public HttpProvider(Downloader downloader, String rootURL, File toDir)
  {
    _downloader=downloader;
    _rootURL=rootURL;
    _toDir=toDir;
  }

  /**
   * Get a file.
   * @param entry Entry to use.
   * @return <code>true</code> if download was successfull, <code>false</code> otherwise.
   */
  public boolean getFile(DirectoryEntryDescription entry)
  {
    if (_rootURL==null)
    {
      return false;
    }
    if (_toDir==null)
    {
      return false;
    }
    String path=EntryUtils.getPath(entry);
    String fullUrl=_rootURL+"/"+encodePath(path);
    File toFile=new File(_toDir,path);
    LOGGER.info("Downloading: "+path+" from "+fullUrl+" to "+toFile);
    toFile.getParentFile().mkdirs();
    boolean ok=false;
    try
    {
      ok=_downloader.downloadToFile(fullUrl,toFile);
    }
    catch(DownloadException downloadException)
    {
      LOGGER.error("Download error for "+fullUrl,downloadException);
    }
    return ok;
  }

  private String encodePath(String path)
  {
    path=path.replace(" ","%20");
    return path;
  }
}
