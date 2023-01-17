package com.chess.engine.board;

import java.util.Objects;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rock;

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

  public boolean isAttack() {
    return false;
  }

  public boolean isCastlingMove() {
    return false;
  }

  public Piece getAttackedPiece() {
    return null;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Move move = (Move) o;
    return destinationCoordinate == move.destinationCoordinate && movedPiece.equals(move.movedPiece);
  }

  @Override public int hashCode() {
    return Objects.hash(movedPiece, destinationCoordinate);
  }

  public Board execute() {
    final Board.Builder builder = new Board.Builder();
    for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
      if (!movedPiece.equals(piece)) {
        builder.setPiece(piece);
      }
    }
    for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
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

    @Override public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      if (!super.equals(o))
        return false;
      AttackMove that = (AttackMove) o;
      return attackedPiece.equals(that.attackedPiece);
    }

    @Override public int hashCode() {
      return Objects.hash(super.hashCode(), attackedPiece);
    }

    @Override public Board execute() {
      return null;
    }

    @Override public boolean isAttack() {
      return true;
    }

    @Override public Piece getAttackedPiece() {
      return this.attackedPiece;
    }
  }

  public static final class PawnMove extends Move {

    private PawnMove(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }
  }

  public static class PawnAttackMove extends AttackMove {

    private PawnAttackMove(Board board, Piece piece, int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate, attackedPiece);
    }
  }

  public static final class PawnEnPassantAttackMove extends PawnAttackMove {

    private PawnEnPassantAttackMove(Board board, Piece piece, int destinationCoordinate, Piece attackedPiece) {
      super(board, piece, destinationCoordinate, attackedPiece);
    }
  }

  public static final class PawnJump extends Move {

    private PawnJump(Board board, Piece piece, int destinationCoordinate) {
      super(board, piece, destinationCoordinate);
    }

    @Override public Board execute() {
      final Board.Builder builder = new Board.Builder();
      for (Piece activePiece : board.getCurrentPlayer().getActivePieces()) {
        if (!this.movedPiece.equals(activePiece)) {
          builder.setPiece(activePiece);
        }
      }
      for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
        builder.setPiece(piece);
      }
      final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
      builder.setPiece(movedPawn);
      builder.setEnPassentPawn(movedPawn);
      builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAllience());
      return builder.build();
    }
  }

  static abstract class CastleMove extends Move {

    protected final Rock castleRock;
    protected final int castleRockStart;
    protected final int castleRockDestination;

    private CastleMove(Board board, final Piece piece, final int destinationCoordinate, final Rock castleRock, final int castleRockStart, final int castleRockDestination) {
      super(board, piece, destinationCoordinate);
      this.castleRock = castleRock;
      this.castleRockStart = castleRockStart;
      this.castleRockDestination = castleRockDestination;
    }

    public Rock getCastleRock() {
      return castleRock;
    }


    @Override public boolean isCastlingMove() {
      return true;
    }

    @Override public Board execute() {
      Board.Builder builder = new Board.Builder();
      for(final Piece piece : board.getCurrentPlayer().getActivePieces())
      {
        if(!movedPiece.equals(piece) && !this.castleRock.equals(piece)){
          builder.setPiece(piece);
        }
      }
      for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
        builder.setPiece(piece);
      }
      builder.setPiece(movedPiece.movePiece(this));
      builder.setPiece(new Rock(castleRockDestination,this.castleRock.getPieceAllience()));
      builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAllience());
      return builder.build();
    }
  }

  public static final class KingSideCastleMove extends CastleMove {

    public KingSideCastleMove(Board board, Piece piece, int destinationCoordinate, final Rock castleRock, final int castleRockStart, final int castleRockDestination) {
      super(board, piece, destinationCoordinate, castleRock, castleRockStart, castleRockDestination);
    }

    @Override public String toString() {
      return "0-0";
    }
  }

  public static final class QueenSideCastleMove extends CastleMove {

    public QueenSideCastleMove(Board board, Piece piece, int destinationCoordinate, final Rock castleRock, final int castleRockStart, final int castleRockDestination) {
      super(board, piece, destinationCoordinate, castleRock, castleRockStart, castleRockDestination);
    }
    @Override public String toString() {
      return "0-0-0";
    }
  }

  public static final class NullMove extends Move {

    private NullMove() {
      super(null, null, -1);
    }

    @Override public Board execute() {
      throw new RuntimeException("Cannot execute the null move");
    }
  }

  public static class MoveFactory {

    private MoveFactory() {
      throw new RuntimeException("Not instentiable!");
    }

    public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
      for (final Move move : board.getAllLegalMoves()) {
        if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
          return move;
        }
      }
      return NULL_MOVE;
    }

  }

}
