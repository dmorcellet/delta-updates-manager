package delta.updates.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.updates.engine.UpdateController;
import delta.updates.engine.listener.UpdateStatus;
import delta.updates.engine.listener.UpdateStatusData;
import delta.updates.engine.listener.UpdateStatusListener;

/**
 * Controller for a panel to display the status update.
 * @author DAM
 */
public class UpdateStatusUIController implements UpdateStatusListener
{
  // Controllers
  private UpdateController _updateController;
  // UI
  private JDialog _dialog;
  private JLabel _statusLabel;
  private JButton _button;
  private enum BUTTON_STATE
  {
    NOTHING(""),
    OK("OK"),
    CANCEL("Cancel");
    private String _label;

    private BUTTON_STATE(String label)
    {
      _label=label;
    }

    public String getLabel()
    {
      return _label;
    }
  }
  private BUTTON_STATE _buttonState;

  /**
   * Constructor.
   * @param updateController Update controller.
   */
  public UpdateStatusUIController(UpdateController updateController)
  {
    _updateController=updateController;
    _buttonState=BUTTON_STATE.CANCEL;
    _dialog=buildUI();
  }

  /**
   * Get the managed dialog.
   * @return the managed dialog.
   */
  public JDialog getDialog()
  {
    return _dialog;
  }

  @Override
  public void statusUpdate(UpdateStatusData data)
  {
    // Message
    String message=data.getStatusMessage();
    _statusLabel.setText(message);
    UpdateStatus status=data.getUpdateStatus();
    _buttonState=getButtonState(status);
    if (_buttonState!=null)
    {
      String buttonLabel=_buttonState.getLabel();
      _button.setText(buttonLabel);
    }
  }

  private JDialog buildUI()
  {
    JDialog dialog=new JDialog();
    dialog.setModal(true);
    dialog.setTitle("Update...");
    JPanel panel=buildPanel();
    Container contentPane=dialog.getContentPane();
    contentPane.add(panel,BorderLayout.CENTER);
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    return dialog;
  }

  private JPanel buildPanel()
  {
    // Label
    _statusLabel=buildLabel();
    _statusLabel.setText("?");
    // Button
    _button=buildButton();
    _button.setText("?");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButton();
      }
    };
    _button.addActionListener(al);
    JPanel ret=buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,0),0,0);
    ret.add(_statusLabel,c);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,5,0),0,0);
    ret.add(_button,c);
    c=new GridBagConstraints(0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(Box.createHorizontalStrut(300),c);
    return ret;
  }

  private void handleButton()
  {
    if (_buttonState==BUTTON_STATE.OK)
    {
      _dialog.setVisible(false);
      _dialog.dispose();
      _updateController=null;
    }
    else if (_buttonState==BUTTON_STATE.CANCEL)
    {
      _updateController.cancel();
    }
  }

  private BUTTON_STATE getButtonState(UpdateStatus updateStatus)
  {
    if (updateStatus==UpdateStatus.OFF) return BUTTON_STATE.NOTHING;
    if (updateStatus==UpdateStatus.RUNNING) return BUTTON_STATE.CANCEL;
    if (updateStatus==UpdateStatus.FINISHED) return BUTTON_STATE.OK;
    if (updateStatus==UpdateStatus.FAILED) return BUTTON_STATE.OK;
    return null;
  }

  private JLabel buildLabel()
  {
    JLabel ret=new JLabel();
    ret.setBackground(Color.WHITE);
    ret.setForeground(Color.BLACK);
    return ret;
  }

  private JButton buildButton()
  {
    JButton ret=new JButton();
    ret.setBackground(Color.WHITE);
    ret.setForeground(Color.BLACK);
    return ret;
  }

  private JPanel buildPanel(LayoutManager layout)
  {
    JPanel ret=new JPanel(layout);
    ret.setBackground(Color.WHITE);
    return ret;
  }
}
