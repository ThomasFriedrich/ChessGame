package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {

  protected final int piecePosition;
  protected final Allience pieceAllience;
  protected final boolean isFirstMove;
  private final PieceType pieceType;

  public Piece(int piecePosition, Allience pieceAllience, PieceType pieceType) {
    this.piecePosition = piecePosition;
    this.pieceAllience = pieceAllience;
    this.pieceType = pieceType;
    //TODO: more work here
    this.isFirstMove = false;
  }

  public Allience getPieceAllience() {
    return pieceAllience;
  }

  public boolean isFirstMove() {
    return isFirstMove;
  }

  public abstract Collection<Move> calculateLegalMoves(final Board board);

  public Integer getPiecePosition() {
    return piecePosition;
  }

  public PieceType getPieceType() {
    return pieceType;
  }

  public abstract Piece movePiece(Move move);

  public enum PieceType {

    PAWN("P"), KNIGHT("N"), BISHOP("B"), ROCK("R"), QUEEN("Q"), KING("K");

    private String pieceName;

    PieceType(String pieceName) {
      this.pieceName = pieceName;
    }

    @Override public String toString() {
      return pieceName;
    }
  }

}

