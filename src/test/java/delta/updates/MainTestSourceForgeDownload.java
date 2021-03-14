package delta.updates;

import java.io.File;

import delta.downloads.Downloader;

/**
 * Test download from SourceForge.
 * @author DAM
 */
public class MainTestSourceForgeDownload
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    String url="https://sourceforge.net/projects/lotrocompanion/files/maps.zip/download";
    Downloader d=new Downloader();
    long now1=System.currentTimeMillis();
    try
    {
      d.downloadToFile(url,new File("maps.zip"));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    long now2=System.currentTimeMillis();
    System.out.println("Duration: "+(now2-now1)+"ms");
  }
}
