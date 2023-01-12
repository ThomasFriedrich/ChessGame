package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {

  protected final int piecePosition;
  protected final Allience pieceAllience;
  protected final boolean isFirstMove;

  public Piece(int piecePosition, Allience pieceAllience) {
    this.piecePosition = piecePosition;
    this.pieceAllience = pieceAllience;
    //TODO: more work here
    this.isFirstMove = false;
  }

  public Allience getPieceAllience() {
    return pieceAllience;
  }

  public boolean isFirstMove(){
    return isFirstMove;
  }

  public abstract Collection<Move> calculateLegalMoves(final Board board);



}

