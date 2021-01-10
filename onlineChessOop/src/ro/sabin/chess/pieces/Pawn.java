package ro.sabin.chess.pieces;

import ro.sabin.chess.ui.AppBoard;

public class Pawn extends Piece {

  public Pawn(boolean white, String file) {
    super(white, file);
  }

  @Override
  public boolean canMove(AppBoard board, Square first, Square last) {
 // verificam daca mutarea pionului se face pe un patratel care are o piesa de aceasi culoare
    if(last.getPiece().isWhite() == this.isWhite()) {
      return false;
    }
    int x = Math.abs(first.getX() - last.getX());
    int y = Math.abs(first.getY() - last.getY());
    if(x * y == 1) {
      if(last.getPiece() != null) {
//        last.getPiece().setKilled(true);
        return true;
      }
      return false;
    }
    return x == 0 ? true : false;

  }

}
