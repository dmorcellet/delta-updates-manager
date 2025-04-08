package delta.updates.ui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.utils.misc.IntegerHolder;
import delta.common.utils.text.EndOfLine;
import delta.updates.data.SoftwareDescription;
import delta.updates.engine.ResourcesAssessment;

/**
 * UI for the updates manager.
 * @author DAM
 */
public class UpdateUI
{
  private static final Logger LOGGER=LoggerFactory.getLogger(UpdateUI.class);

  /**
   * Ask if the update is allowed.
   * @param local Local sofware.
   * @param remote Remote software.
   * @param assessment Ressources assessment.
   * @return <code>true</code> if update is allowed, <code>false</code> otherwise.
   */
  public static boolean askForUpdate(SoftwareDescription local, SoftwareDescription remote, ResourcesAssessment assessment)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      LOGGER.warn("This should not be called from the UI thread!", new Exception());
      return false;
    }
    final String message=getUpdateQuestion(local,remote,assessment);
    final IntegerHolder resultHolder=new IntegerHolder();
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        int result=UIUtils.showQuestionDialog(null,message,"Update?",JOptionPane.YES_NO_OPTION);
        resultHolder.setInt(result);
      }
    };
    try
    {
      SwingUtilities.invokeAndWait(r);
      return (resultHolder.getInt()==JOptionPane.OK_OPTION);
    }
    catch(Exception e) // NOSONAR
    {
      LOGGER.warn("Caught exception in invokeAndWait!", e);
      return false;
    }
  }

  private static String getUpdateQuestion(SoftwareDescription local, SoftwareDescription remote, ResourcesAssessment assessment)
  {
    String localVersion=local.getVersion().getName();
    String remoteVersion=remote.getVersion().getName();

    String message1="Do you want to update this application?";
    String message2="This will upgrade from version "+localVersion+" to version "+remoteVersion+".";
    int packagesCount=assessment.getPackagesCount();
    String packageWord=(packagesCount>1)?"packages":"package";
    String downloadSize=SizeUtils.getSizeLabel(assessment.getDownloadSize());
    String message3="This will download "+downloadSize+" in "+packagesCount+" "+packageWord+".";
    String diskSize=SizeUtils.getSizeLabel(assessment.getDownloadSize());
    String message4="This will require "+diskSize+" disk size.";
    String eol=EndOfLine.NATIVE_EOL;
    String message=message1+eol+eol+message2+eol+message3+eol+message4;
    return message;
  }
}
