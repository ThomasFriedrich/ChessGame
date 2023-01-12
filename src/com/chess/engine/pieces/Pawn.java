package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

  private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { 7, 8, 9, 16 };

  public Pawn(final int piecePosition, final Allience pieceAllience) {
    super(piecePosition, pieceAllience, PieceType.PAWN);
  }

  @Override public Collection<Move> calculateLegalMoves(final Board board) {

    final List<Move> legalMoves = new ArrayList<>();
    for (final int currentCanditateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
      final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAllience().getDirection() * currentCanditateOffset);
      if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
        continue;
      }

      if (currentCanditateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
        //TODO: more work to do here (deal with promotions)!!!
        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));

      } else if (currentCanditateOffset == 16 && this.isFirstMove() && (BoardUtils.SECOND_ROW[piecePosition] && pieceAllience.isBlack())
          || (BoardUtils.SEVENTH_ROW[piecePosition] && pieceAllience.isWhite())) {
        final int behindCandidateDestinationCoordinate = piecePosition + (pieceAllience.getDirection() * 8);
        if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
          legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
        }
      } else if (currentCanditateOffset == 7
                && !((BoardUtils.EIGHTH_COLUMN[piecePosition] && pieceAllience.isWhite())
                ||(BoardUtils.FIRST_COLUMN[piecePosition] && pieceAllience.isBlack()))) {
        if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
          final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
          if(pieceAllience != pieceOnCandidate.getPieceAllience()){
            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
          }

        }

      } else if (currentCanditateOffset == 9
          && !((BoardUtils.EIGHTH_COLUMN[piecePosition] && pieceAllience.isBlack())
          ||(BoardUtils.FIRST_COLUMN[piecePosition] && pieceAllience.isWhite()))) {
        if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
          final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
          if(pieceAllience != pieceOnCandidate.getPieceAllience()){
            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate,pieceOnCandidate));
          }

        }


      }
    }

    return ImmutableList.copyOf(legalMoves);
  }

  @Override public String toString() {
    return PieceType.PAWN.toString();
  }
}
