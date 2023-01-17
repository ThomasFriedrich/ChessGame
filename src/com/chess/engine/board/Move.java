package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

  final Board board;
  final Piece movedPiece;
  final int destinationCoordinate;

  public static final Move NULL_MOVE = new NullMove();

  private Move(Board board, Piece piece, int destinationCoordinate) {
    this.board = board;
    this.movedPiece = piece;
    this.destinationCoordinate = destinationCoordinate;
  }

    public int getDestinationCoordinate() {
      return destinationCoordinate;
    }

  private int getCurrentCoordinate() {
    return this.movedPiece.getPiecePosition();
  }

  public Piece getMovedPiece() {
    return movedPiece;
  }

  public Board execute() {
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

  public static final class MajorMove extends Move {

    public MajorMove(final Board board, final Piece piece, final int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }

  }

  public static class AttackMove extends Move {

    final Piece attackedPiece;

    public AttackMove(final Board board, final Piece piece, final int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate);
      this.attackedPiece = attackedPiece;
    }

    @Override public Board execute() {
      return null;
    }
  }

  public static final class PawnMove extends Move{

    private PawnMove(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }

  public static class PawnAttackMove extends AttackMove{

    private PawnAttackMove(Board board, Piece piece, int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate,attackedPiece);
    }
  }

  public static final class PawnEnPassantAttackMove extends PawnAttackMove{

    private PawnEnPassantAttackMove(Board board, Piece piece, int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate,attackedPiece);
    }
  }

  public static final class PawnJump extends Move{

    private PawnJump(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }


  static abstract class CastleMove extends Move{

    private CastleMove(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }
  public static final class KingSideCastleMove extends CastleMove{

    private KingSideCastleMove(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }

  public static final class QueenSideCastleMove extends CastleMove{

    private QueenSideCastleMove(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }

  public static final class NullMove extends Move{

    private NullMove() {
      super(null,null,-1);
    }

    @Override public Board execute() {
      throw new RuntimeException("Cannot execute the null move");
    }
  }

  public static class MoveFactory{


    private MoveFactory(){
      throw new RuntimeException("Not instentiable!");
    }

    public static Move createMove(final Board board,final int currentCoordinate, final int destinationCoordinate){
      for (final Move move:board.getAllLegalMoves()){
        if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
          return move;
        }
      }
      return NULL_MOVE;
    }

  }



}
