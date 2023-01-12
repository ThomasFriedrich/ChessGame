package com.chess.engine.player;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {

        super(board, blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    protected Collection<Piece> getActivePieces() {
        return board.getBlackPieces();
    }

    @Override
    protected Allience getAllience() {
        return Allience.BLACK;
    }

    @Override
    protected Player getOpponent() {
        return board.getWhitePlayer();
    }
}
