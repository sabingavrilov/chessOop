package ro.sabin.chess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class InfoBoard {

  private Shell compBoard;

  private Table infoTabel;

  public InfoBoard(Shell compBoard) {
    this.compBoard = compBoard;
    compBoard.setBackground(new Color(compBoard.getDisplay(), 100, 100, 100));

    GridData data = new GridData(GridData.FILL_BOTH);
    compBoard.setLayoutData(data);

    addComponents();

  }

  public void addComponents() {
    infoTabel = new Table(compBoard, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
    infoTabel.setHeaderVisible(true);
    infoTabel.setLinesVisible(true);
    GridData data = new GridData(GridData.FILL_BOTH);
    data.widthHint = 370;
    data.heightHint = 370;
    infoTabel.setLayoutData(data);
    TableColumn col = new TableColumn(infoTabel, SWT.NONE);
    col.setText("Istoric mutari");
    col.setWidth(370);
  }

  public void addItem(String info) {
    TableItem item = new TableItem(infoTabel, SWT.NONE);
    item.setText(info);
  }

}
