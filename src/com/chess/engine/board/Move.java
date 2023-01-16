package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

  final Board board;
  final Piece movedPiece;
  final int destinationCoordinate;

  private Move(Board board, Piece piece, int destinationCoordinate) {
    this.board = board;
    this.movedPiece = piece;
    this.destinationCoordinate = destinationCoordinate;
  }

    public int getDestinationCoordinate() {
      return destinationCoordinate;
    }

  public Piece getMovedPiece() {
    return movedPiece;
  }

  public abstract Board execute();

  public static final class MajorMove extends Move {

    public MajorMove(final Board board, final Piece piece, final int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }

    @Override public Board execute() {
      final Board.Builder builder = new Board.Builder();
      for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
        if(!movedPiece.equals(piece)){
          builder.setPiece(piece);
        }
      }
      for(Piece piece:board.getCurrentPlayer().getOpponent().getActivePieces()){
        builder.setPiece(piece);
      }
      //move the movedPiece
      builder.setPiece(movedPiece.movePiece(this));
      builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAllience());

      return builder.build();
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
