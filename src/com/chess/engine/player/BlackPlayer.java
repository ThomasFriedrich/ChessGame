package com.chess.engine.player;

import static com.chess.engine.board.Move.*;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rock;
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
            //black king side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rockTile = this.board.getTile(7);
                if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(5, opponentLegals).isEmpty() && Player.calculateAttackOnTile(6, opponentLegals).isEmpty()
                        && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
                        kingCastles.add(new KingSideCastleMove(board,playersKing,6,(Rock)rockTile.getPiece(),rockTile.getTileCoordinate(),5));
                    }
                }
            }
            //black queen side castle
            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rockTile = this.board.getTile(0);
                if (rockTile.isTileOccupied() && rockTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(1, opponentLegals).isEmpty() && Player.calculateAttackOnTile(2, opponentLegals).isEmpty()
                        && Player.calculateAttackOnTile(3, opponentLegals).isEmpty()
                        && rockTile.getPiece().getPieceType() == Piece.PieceType.ROCK) {
                        kingCastles.add(new QueenSideCastleMove(board,playersKing,1,(Rock)rockTile.getPiece(),rockTile.getTileCoordinate(),2));
                    }
                }

            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
