package delta.updates.engine.providers;

import java.io.File;

import org.apache.log4j.Logger;

import delta.downloads.DownloadException;
import delta.downloads.Downloader;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;

/**
 * Data provider that downloads data using HTTP.
 * @author DAM
 */
public class HttpProvider implements FileProvider
{
  private static final Logger LOGGER=Logger.getLogger(HttpProvider.class);

  private Downloader _downloader;
  private String _rootURL;

  /**
   * Constructor.
   * @param downloader Downloader.
   * @param rootURL Root URL for downloads.
   */
  public HttpProvider(Downloader downloader, String rootURL)
  {
    _downloader=downloader;
    _rootURL=rootURL;
  }

  /**
   * Get a file.
   * @param entry Entry to use.
   * @return <code>true</code> if download was successfull, <code>false</code> otherwise.
   */
  public boolean getFile(FileDescription entry, File toDir)
  {
    if (_rootURL==null)
    {
      return false;
    }
    String path=EntryUtils.getPath(entry);
    String fullUrl=_rootURL+"/"+encodePath(path);
    File toFile=new File(toDir,path);
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
