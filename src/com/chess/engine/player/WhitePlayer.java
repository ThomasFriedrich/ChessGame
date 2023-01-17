package com.chess.engine.player;

import static com.chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rock;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player {
  public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {

    super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
  }

  @Override
  public Collection<Piece> getActivePieces() {
    return board.getWhitePieces();
  }

  @Override
  public Allience getAllience() {
    return Allience.WHITE;
  }

  @Override
  public Player getOpponent() {
    return board.getBlackPlayer();
  }

  @Override protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
    final List<Move> kingCastles = new ArrayList<>();

    if (this.playersKing.isFirstMove() && !this.isInCheck()) {
      //whites king side castle
      if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
        final Tile rockTile = this.board.getTile(63);
        if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
          if (Player.calculateAttackOnTile(61, opponentLegals).isEmpty() && Player.calculateAttackOnTile(62, opponentLegals).isEmpty()
              && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
            kingCastles.add(new KingSideCastleMove(board,playersKing,62,(Rock)rockTile.getPiece(),rockTile.getTileCoordinate(),61));
          }
        }
      }
      //white queen side castle
      if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
        final Tile rockTile = this.board.getTile(56);
        if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
          if (Player.calculateAttackOnTile(59, opponentLegals).isEmpty() && Player.calculateAttackOnTile(58, opponentLegals).isEmpty()
              && Player.calculateAttackOnTile(57, opponentLegals).isEmpty()
              && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
            kingCastles.add(new QueenSideCastleMove(board,playersKing,58,(Rock)rockTile.getPiece(),rockTile.getTileCoordinate(),59));
          }
        }

      }
    }
    return ImmutableList.copyOf(kingCastles);
  }
}
