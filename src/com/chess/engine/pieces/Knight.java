package com.chess.engine.pieces;

import static com.chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece {

  private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

  public Knight(final int piecePosition, final Allience pieceAllience) {
    super(piecePosition, pieceAllience);
  }

  @Override public Collection<Move> calculateLegalMoves(final Board board) {

    List<Move> legalMoves = new ArrayList<>();

    for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
      final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
      if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

        if (isFirstColumnExclusion(piecePosition, currentCandidateOffset) || isSecondColumnExclusion(piecePosition, currentCandidateOffset) || isSeventhColumnExclusion(
            piecePosition, currentCandidateOffset) || isEighthColumnExclusion(piecePosition, currentCandidateOffset)) {
          continue;
        }

        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

        if (!candidateDestinationTile.isTileOccupied()) {
          legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
        } else {
          final Piece pieceAtDestination = candidateDestinationTile.getPiece();
          final Allience pieceAllienceAtDestination = pieceAtDestination.getPieceAllience();
          if (pieceAllience != pieceAllienceAtDestination) {
            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
          }
        }

      }
    }

    return ImmutableList.copyOf(legalMoves);
  }

  private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
    return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15);
  }

  private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
    return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
  }

  private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
    return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
  }

  private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
    return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 17 || candidateOffset == 10 || candidateOffset == -6 || candidateOffset == -15);
  }

  @Override public String toString() {
    return PieceType.KNIGHT.toString();
  }
}
