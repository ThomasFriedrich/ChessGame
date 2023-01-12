package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Player {

    final Board board;
    final King playersKing;
    final Collection<Move> legalMoves;

    protected Player(final Board board,final Collection<Move> legalMoves,final Collection<Move> opponentMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.playersKing = establishKing();
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece instanceof King){
                return (King) piece;
            }
        }
        throw new RuntimeException("not a legal Chessboard without a king");
    }

    protected abstract Collection<Piece> getActivePieces();
}

