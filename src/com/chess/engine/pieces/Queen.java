package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Queen extends Piece {
  private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

  public Queen(int piecePosition, Allience pieceAllience) {
    super(piecePosition, pieceAllience, PieceType.QUEEN);
  }

  @Override public Collection<Move> calculateLegalMoves(Board board) {
    final List<Move> legalMoves = new ArrayList<>();
    for (int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
      int candidateDestinationCoordinate = this.piecePosition;
      while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
        if (isFirstColumnExclution(candidateDestinationCoordinate, candidateCoordinateOffset)
            || isEighthColumnExclution(candidateDestinationCoordinate, candidateCoordinateOffset)) {
          break;
        }
        candidateDestinationCoordinate += candidateCoordinateOffset;
        if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
          final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

          if (!candidateDestinationTile.isTileOccupied()) {
            legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
          } else {
            final Piece pieceAtDestination = candidateDestinationTile.getPiece();
            final Allience pieceAllienceAtDestination = pieceAtDestination.getPieceAllience();
            if (pieceAllience != pieceAllienceAtDestination) {
              legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
            }
            break;
          }
        }
      }
    }

    return ImmutableList.copyOf(legalMoves);
  }

  @Override public Queen movePiece(final Move move) {
    return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllience());
  }
  private static boolean isFirstColumnExclution(final int currentPosition, final int candidateOffset) {
    return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
  }

  private static boolean isEighthColumnExclution(final int currentPosition, final int candidateOffset) {
    return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 9 || candidateOffset == 1 || candidateOffset == -7);
  }

  @Override public String toString() {
    return PieceType.QUEEN.toString();
  }
}
