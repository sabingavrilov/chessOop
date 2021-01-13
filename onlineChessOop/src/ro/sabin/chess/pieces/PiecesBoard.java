package ro.sabin.chess.pieces;

/*
 * Tabla de sah din model
 */
public class PiecesBoard {

  Piece[][] piese = new Piece[8][8];

  public PiecesBoard() {

    // definesti piesele
    Piece queenW = new Queen(true, "src/images/Chess_queen_white.png");
    Piece queenB = new Queen(false, "src/images/Chess_queen.png");

    Piece kingW = new King(true, "src/images/Chess_king_white.png");
    Piece kingB = new King(false, "src/images/Chess_king.png");

    Piece BishopW = new Bishop(true, "src/images/Chess_bishop_white.png");
    Piece BishopB = new Bishop(false, "src/images/Chess_bishop.png");

    Piece KnightW = new Knight(true, "src/images/Chess_knight_white.png");
    Piece KnightB = new Knight(false, "src/images/Chess_knight.png");

    Piece RookW = new Rook(true, "src/images/Chess_rook_white.png");
    Piece RookB = new Rook(false, "src/images/Chess_rook.png");

    Piece PawnW = new Pawn(true, "src/images/Chess_pawn_white.png");
    Piece PawnB = new Pawn(false, "src/images/Chess_pawn.png");

    piese[0][0] = RookW;
    piese[0][1] = KnightW;
    piese[0][2] = BishopW;
    piese[0][3] = kingW;
    piese[0][4] = queenW;
    piese[0][5] = BishopW;
    piese[0][6] = KnightW;
    piese[0][7] = RookW;

    piese[1][0] = PawnW;
    piese[1][1] = PawnW;
    piese[1][2] = PawnW;
    piese[1][3] = PawnW;
    piese[1][4] = PawnW;
    piese[1][5] = PawnW;
    piese[1][6] = PawnW;
    piese[1][7] = PawnW;

    for (int i = 2; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        piese[i][j] = null;
      }
    }

    piese[7][0] = RookB;
    piese[7][1] = KnightB;
    piese[7][2] = BishopB;
    piese[7][3] = kingB;
    piese[7][4] = queenB;
    piese[7][5] = BishopB;
    piese[7][6] = KnightB;
    piese[7][7] = RookB;

    piese[6][0] = PawnB;
    piese[6][1] = PawnB;
    piese[6][2] = PawnB;
    piese[6][3] = PawnB;
    piese[6][4] = PawnB;
    piese[6][5] = PawnB;
    piese[6][6] = PawnB;
    piese[6][7] = PawnB;

  }

  public Piece[][] getBoard() {
    return piese;
  }

  public Piece[][] move(int x_start, int y_start, int x_stop, int y_stop) {
    // validari .... doar daca coordonatele sunt diferite daca nu returneaza maticea neschimbata
    piese[x_stop][y_stop] = piese[x_start][y_start];
    piese[x_start][y_start] = null; // sau o piesa "spatiu"

    return piese;
  }

}
