package com.chess.engine.pieces;

import static com.chess.engine.board.Move.AttackMove;
import static com.chess.engine.board.Move.MajorMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class King extends Piece {

  private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

  public King(final int piecePosition, final Allience pieceAllience) {
    super(piecePosition, pieceAllience);
  }

  @Override public Collection<Move> calculateLegalMoves(final Board board) {

    final List<Move> legalMoves = new ArrayList<>();

    for (int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
      final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

      if (isFirstColumnExclusion(piecePosition, currentCandidateOffset) || isEighthColumnExclusion(piecePosition, currentCandidateOffset)) {
        continue;
      }

      if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
        Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
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
    return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
  }

  private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
    return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
  }

  @Override public String toString() {
    return PieceType.KING.toString();
  }

}
