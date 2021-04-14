package delta.updates.engine;

import java.util.List;

import junit.framework.TestCase;
import delta.downloads.Downloader;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageUsage;

/**
 * Test for the update engine.
 * @author DAM
 */
public class UpdateEngine2Test extends TestCase
{
  /**
   * Test update.
   */
  public void testUpdate()
  {
    Downloader downloader=new Downloader();
    UpdateEngine2 engine2=new UpdateEngine2(downloader);
    SoftwareDescription soft=engine2.lookForUpdate();
    if (soft==null)
    {
      return;
    }
    List<SoftwarePackageUsage> packages=engine2.getNeededPackages(soft);
    ResourcesAssessment assessment=engine2.assessResources(packages);
    System.out.println("Ressources assessment: "+assessment);
  }
}
