package ro.sabin.chess.pieces;

import java.io.File;

import org.eclipse.swt.graphics.Image;

import ro.sabin.chess.Chess;
import ro.sabin.chess.ui.AppBoard;

public abstract class Piece {

  private boolean white = false;
  private String img;

  public Piece(boolean white, String img) {
    this.white = white;
    this.img = img;
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public Image loadImage() {
    File f = null;
    try {
      f = new File(img);
      Image im = null;
      if (f != null && f.exists()) {
        im = new Image(Chess.getShell().getDisplay(), img);
      }
      return im;
    } catch (Exception ex) {
      System.out.println("loadImage(): fisierul '" + img + "' nu a putut fi citit");
      return null;
    }

  }



  public abstract boolean canMove(AppBoard board, Square first, Square last);

}
