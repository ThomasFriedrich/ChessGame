package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

  final Board board;
  final Piece piece;
  final int destinationCoordinate;

  private Move(Board board, Piece piece, int destinationCoordinate) {
    this.board = board;
    this.piece = piece;
    this.destinationCoordinate = destinationCoordinate;
  }

    public int getDestinationCoordinate() {
      return destinationCoordinate;
    }

  public abstract Board execute();

  public static final class MajorMove extends Move {

    public MajorMove(final Board board, final Piece piece, final int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }

    @Override public Board execute() {
      return null;
    }
  }

  public static final class AttackMove extends Move {

    final Piece attackedPiece;

    public AttackMove(final Board board, final Piece piece, final int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate);
      this.attackedPiece = attackedPiece;
    }

    @Override public Board execute() {
      return null;
    }
  }

}
