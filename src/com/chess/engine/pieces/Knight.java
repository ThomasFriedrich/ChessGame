package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece {

  private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

  public Knight(final int piecePosition, final Allience pieceAllience) {
    super(piecePosition, pieceAllience);
  }

  @Override public List<Move> calculateLegalMoves(Board board) {

    int candidateDestinationCoordinate;
    List<Move> legalMoves = new ArrayList<>();

    for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
      candidateDestinationCoordinate = this.piecePosition + currentCandidate;
      if (true/*=isValidTileCoordinate*/) {
        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

        if (!candidateDestinationTile.isTileOccupied()) {
          legalMoves.add(new Move());
        } else {
          final Piece pieceAtDestination = candidateDestinationTile.getPiece();
          final Allience pieceAllienceAtDestination = pieceAtDestination.getPieceAllience();
          if (pieceAllience != pieceAllienceAtDestination) {
            legalMoves.add(new Move());
          }
        }

      }
    }

    return ImmutableList.copyOf(legalMoves);
  }
}
