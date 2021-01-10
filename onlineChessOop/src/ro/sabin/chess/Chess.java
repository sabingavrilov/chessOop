package ro.sabin.chess;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import ro.sabin.chess.activemq.ActiveMQUtil;
import ro.sabin.chess.pieces.ChessConstants;
import ro.sabin.chess.ui.AppBoard;

public class Chess {

  private static Display display;

  private static Shell shell;

  private static AppBoard myBoard;

  public static void main(String[] args) {
    display = new Display();

    shell = new Shell(display);
    shell = new Shell(shell, SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.CLOSE);
    shell.setSize(1200, 900);
    shell.setLocation(500, 50);
    shell.setLayout(new GridLayout(2, false));
    shell.setText("ID CLIENT: " + ActiveMQUtil.CLIENT_ID);

    myBoard = new AppBoard(shell);

    createButtons();

    open();
  }

  private static void createButtons() {
    // butoane
    Button btnConnect = new Button(shell, SWT.PUSH);
    btnConnect.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    btnConnect.setText("&Start Joc");
    btnConnect.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        ActiveMQUtil.connect(myBoard);
        if (!ActiveMQUtil.send("Cerere Start Joc", ChessConstants.TYPE_START_INIT, "")) {
          MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR);
          mb.setMessage("Eroare la trimitere!");
          mb.open();
        }
      }
    });

//    Button btnSend = new Button(shell, SWT.PUSH);
//    btnSend.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
//    btnSend.setText("&START INIT");
//    btnSend.addSelectionListener(new SelectionAdapter() {
//      @Override
//      public void widgetSelected(SelectionEvent e) {
//
//      }
//    });
  }

  public static Shell getShell() {
    return shell;
  }

  public static void open() {
//   shell.pack();
    shell.open();
    // run the event loop as long as the window is open
    while (!shell.isDisposed()) {
      // read the next OS event queue and transfer it to a SWT event
      if (!display.readAndDispatch()) {
        // if there are currently no other OS event to process
        // sleep until the next OS event is available
        display.sleep();
      }
    }

    // disposes all associated windows and their components
    display.dispose();
  }
}
