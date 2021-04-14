package delta.updates.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Collection of utility methods related to UI.
 * @author DAM
 */
public class UIUtils
{
  /**
   * Show a modal question dialog.
   * @param parent Parent component.
   * @param message Question message.
   * @param title Title of the dialog window.
   * @param optionType Options configuration.
   * @return A result code (see {@link JOptionPane}).
   */
  public static int showQuestionDialog(Component parent, String message, String title, int optionType)
  {
    int ret=JOptionPane.showConfirmDialog(parent,message,title,optionType);
    return ret;
  }

  /**
   * Show a information dialog.
   * @param parent Parent component.
   * @param message Information message.
   * @param title Title of the dialog window.
   */
  public static void showInformationDialog(Component parent, String message, String title)
  {
    JOptionPane.showMessageDialog(parent,message,title,JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Show an error dialog.
   * @param parent Parent component.
   * @param message Information message.
   * @param title Title of the dialog window.
   */
  public static void showErrorDialog(Component parent, String message, String title)
  {
    JOptionPane.showMessageDialog(parent,message,title,JOptionPane.ERROR_MESSAGE);
  }
}
