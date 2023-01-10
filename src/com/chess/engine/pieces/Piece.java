package com.chess.engine.pieces;

import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {

  protected final int piecePosition;
  protected final Allience pieceAllience;

  public Piece(int piecePosition, Allience pieceAllience) {
    this.piecePosition = piecePosition;
    this.pieceAllience = pieceAllience;
  }

  public Allience getPieceAllience() {
    return pieceAllience;
  }

  public abstract List<Move> calculateLegalMoves(final Board board);
}
