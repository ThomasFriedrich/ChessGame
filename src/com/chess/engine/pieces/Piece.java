package com.chess.engine.pieces;


import java.util.Collection;
import java.util.Objects;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {

  protected final int piecePosition;
  protected final Allience pieceAllience;
  protected final boolean isFirstMove;
  private final PieceType pieceType;
  private final int cachedHashCode;

  public Piece(int piecePosition, Allience pieceAllience, PieceType pieceType) {
    this.piecePosition = piecePosition;
    this.pieceAllience = pieceAllience;
    this.pieceType = pieceType;
    //TODO: more work here
    this.isFirstMove = false;
    cachedHashCode = computeHashCode();
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

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Piece piece = (Piece) o;
    return piecePosition == piece.piecePosition && isFirstMove == piece.isFirstMove && pieceAllience == piece.pieceAllience && pieceType == piece.pieceType;
  }

  private int computeHashCode() {
    return Objects.hash(piecePosition, pieceAllience, isFirstMove, pieceType);
  }
  @Override public int hashCode() {
    return cachedHashCode;
  }
}

