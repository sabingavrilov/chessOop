package ro.sabin.chess.pieces;

import ro.sabin.chess.ui.AppBoard;

public class Rook extends Piece {

  public Rook(boolean white, String file) {
    super(white, file);
  }

  @Override
  public boolean canMove(AppBoard board, Square first, Square last) {
    // verificam daca mutarea turei se face pe un patratel care are o piesa de aceasi culoare
    if (last.getPiece().isWhite() == this.isWhite()) {
      return false;
    }

    int x = Math.abs(first.getX() - last.getX());
    int y = Math.abs(first.getY() - last.getY());
    return (x * y) == 0 ? true : false;
  }



}