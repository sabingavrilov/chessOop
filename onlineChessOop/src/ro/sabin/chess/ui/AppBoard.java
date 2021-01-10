package ro.sabin.chess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ro.sabin.chess.activemq.ActiveMQUtil;
import ro.sabin.chess.pieces.ChessConstants;
import ro.sabin.chess.pieces.Piece;
import ro.sabin.chess.pieces.PiecesBoard;

public class AppBoard extends Composite {

  private static final String DATA_ORING_BG = "DATA_ORING_BG";
  private final int SQUARE_SIZE = 80;
  private Label[][] piese;

  private int x_start = -1, y_start = -1;

  public PiecesBoard appBoard;

  private Label selectedLabel;

  private boolean isSelected = false;

  final Color red = new Color(this.getDisplay(), 222, 0, 0);

  public AppBoard(Shell compBoard) {
    super(compBoard, SWT.NONE);
    try {
      Color brown = new Color(this.getDisplay(), 136, 69, 0);
      Color lightBrown = new Color(this.getDisplay(), 222, 152, 106);

      this.setBackground(new Color(this.getDisplay(), 100, 100, 100));

      GridData data = new GridData(GridData.FILL_BOTH);
      this.setLayoutData(data);

      appBoard = new PiecesBoard();

      piese = new Label[8][8];
      boolean bl = false;
      for (int i = 0; i < 10; i++) {
        bl = !bl;
        for (int j = 0; j < 10; j++) {
          Label lbl = new Label(this, SWT.NONE);
          lbl.setSize(SQUARE_SIZE, SQUARE_SIZE);
          lbl.setLocation(j * SQUARE_SIZE, i * SQUARE_SIZE);
          if ((i == 0 && j == 0) || (i == 0 && j == 9) || (i == 9 && j == 0) || (i == 9 && j == 9)) {
            // nu pun nimic in colturi
            continue;
          }
          if (i == 0 || i == 9) {
            lbl.setText(new Character((char)(64 + j)).toString());
          } else if (j == 0 || j == 9) {
            lbl.setText(String.valueOf(8 - i + 1));
          } else {
            Color bg = bl ? brown : lightBrown;
            lbl.setBackground(bg);
            lbl.setData(DATA_ORING_BG, bg);
            piese[i - 1][j - 1] = lbl;
            bl = !bl;
          }
        }
      }

      initChessTable();

        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            MouseListener mouseListener = new MyMouseListener(i, j);
            piese[i][j].addMouseListener(mouseListener);
          }
        }

      printTable();

    } catch (Exception e) {
      System.out.println(e.getMessage() + "aaa");
    }

  }

  public void initChessTable() {
    Piece[][] board = appBoard.getBoard();

    // tabla
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (board[i][j] != null) {
          piese[i][j].setImage(board[i][j].loadImage());
        } else {
          if (piese[i][j].getImage() != null) {
            piese[i][j].getImage().dispose();
          }
        }
      }
    }
  }

  public void udpateTableMove(int xSrc, int ySrc, int xDest, int yDest) {
//    System.out.println(xSrc + " " + ySrc + " " + piese[xSrc][ySrc]);
    Image srcImg = piese[xSrc][ySrc].getImage();
    if (srcImg != null && !srcImg.isDisposed()) {
      piese[xDest][yDest].setImage(srcImg);
    }
    piese[xSrc][ySrc].setImage(null);
  }

  public void printTable() {
    System.out.println("------------ TABLE START ------------");
    for (int i = 0; i < piese.length; i++) {
      for (int j = 0; j < piese[i].length; j++) {
        System.out.print("[" + i + j + ":" + (piese[i][j].getImage() != null ? "X" : " ") + "] ");
      }
      System.out.println();
    }
    System.out.println("------------  TABLE END  ------------");
  }

  class MyMouseListener implements MouseListener {
    private final int coordX;
    private final int coordY;

    public MyMouseListener(int i, int j) {
      coordX = i;
      coordY = j;
    }

    @Override
    public void mouseUp(MouseEvent arg0) {
    }

    @Override
    public void mouseDown(MouseEvent event) {
      if (ActiveMQUtil.getGameIsStarted()) {
        System.out.println("mouseDown");
        if (x_start == -1) {
          System.out.println("ssss");
          x_start = coordX;
          y_start = coordY;
          isSelected = true;
          selectedLabel = (Label)event.widget;
        }
        if ((coordX != x_start || coordY != y_start) && x_start != -1 && y_start != -1) {
          printTable();
          System.out.println("222");
          System.out.println("moved " + x_start + "|" + y_start + " => " + coordX + "|" + coordY);
          appBoard.move(x_start, y_start, coordX, coordY);
          udpateTableMove(x_start, y_start, coordX, coordY);
          ActiveMQUtil.send("Mutare", ChessConstants.TYPE_MOVE, "" + x_start + y_start + coordX + coordY);

          //initChessTable();


          printTable();
          isSelected = false;
          x_start = -1;
          y_start = -1;

        }
        Color origBg = (Color)selectedLabel.getData(DATA_ORING_BG);
        if (isSelected && selectedLabel.getBackground() != red) {
          selectedLabel.setBackground(red);
          isSelected = false;
        } else {
          selectedLabel.setBackground(origBg);
          x_start = -1;
          y_start = -1;
        }
      }
    }

    @Override
    public void mouseDoubleClick(MouseEvent arg0) {
      System.out.println("dc");
    }

  }
}
