package com.chess.engine.player;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {

        super(board, blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return board.getBlackPieces();
    }

    @Override
    public Allience getAllience() {
        return Allience.BLACK;
    }

    @Override
    public Player getOpponent() {
        return board.getWhitePlayer();
    }

    @Override protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playersKing.isFirstMove() && !this.isInCheck()) {
            //whites king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rockTile = this.board.getTile(7);
                if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(5, opponentLegals).isEmpty() && Player.calculateAttackOnTile(6, opponentLegals).isEmpty()
                        && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
                        kingCastles.add(null);
                    }
                }
            }
            if (!this.board.getTile(0).isTileOccupied() && !this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied()) {
                final Tile rockTile = this.board.getTile(3);
                if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(0, opponentLegals).isEmpty() && Player.calculateAttackOnTile(1, opponentLegals).isEmpty()
                        && Player.calculateAttackOnTile(2, opponentLegals).isEmpty()
                        && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
                        kingCastles.add(null);
                    }
                }

            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
