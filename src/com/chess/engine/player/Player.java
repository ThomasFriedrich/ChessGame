package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

public abstract class Player {

  protected final Board board;
  protected final King playersKing;
  protected final Collection<Move> legalMoves;
  private boolean isInCheck;

  protected Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
    this.board = board;
    this.legalMoves = legalMoves;
    this.playersKing = establishKing();
    this.isInCheck = Player.calculateAttackOnTile(playersKing.getPiecePosition(), opponentMoves).isEmpty();
  }

  /**
   * returns all attacks for the given position with the next move
   *
   * @param piecePosition
   * @param moves
   * @return
   */
  protected static Collection<Move> calculateAttackOnTile(Integer piecePosition, Collection<Move> moves) {
    List<Move> attackMoves = new ArrayList<>();
    for (Move move : moves) {
      if (piecePosition == move.getDestinationCoordinate()) {
        attackMoves.add(move);
      }
    }
    return ImmutableList.copyOf(attackMoves);
  }

  private King establishKing() {
    for (final Piece piece : getActivePieces()) {
      if (piece instanceof King) {
        return (King) piece;
      }
    }
    throw new RuntimeException("not a legal Chessboard without a king");
  }

  protected boolean hasEscapeMoves() {
    for (final Move move : legalMoves) {
      final MoveTransition transition = makeMove(move);
      if (transition.getMoveStatus() == MoveStatus.DONE) {
        return true;
      }
    }
    return false;
  }

  public boolean isMoveLegal(Move move) {
    return legalMoves.contains(move);
  }

  public boolean isInCheck() {
    return isInCheck;
  }

  public boolean isInCheckMate() {
    return isInCheck && !hasEscapeMoves();
  }

  public boolean isInStaleMate() {
    return !isInCheck && !hasEscapeMoves();
  }

  public boolean isCastled() {
    return false;
  }

  public MoveTransition makeMove(Move move) {
    if (!isMoveLegal(move)) {
      return new MoveTransition(board, move, MoveStatus.ILLEGAL_MOVE);
    }
    Board transitionBoard = move.execute();
    // big question mark why ask the current player the attack of the opponent king and why is this an illegal move?
    final Collection<Move> kingAttacks = Player.calculateAttackOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition()
        , transitionBoard.getCurrentPlayer().getLegalMoves());
    if (!kingAttacks.isEmpty()) {
      return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
    }

    return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
  }

  public Collection<Move> getLegalMoves() {
    return legalMoves;
  }

  private Piece getPlayerKing() {
    return playersKing;
  }
  public abstract Collection<Piece> getActivePieces();
  public abstract Allience getAllience();
  public abstract Player getOpponent();

  protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

}

