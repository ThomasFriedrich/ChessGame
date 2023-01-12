package com.chess.engine.player;

import com.chess.engine.Allience;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playersKing;
    protected final Collection<Move> legalMoves;
    private boolean isInCheck;

    protected Player(final Board board,final Collection<Move> legalMoves,final Collection<Move> opponentMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.playersKing = establishKing();
        this.isInCheck = Player.calculateAttackOnTile(playersKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    private static Collection<Move> calculateAttackOnTile(Integer piecePosition, Collection<Move> moves) {
        List<Move> attackMoves = new ArrayList<>();
        for (Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece instanceof King){
                return (King) piece;
            }
        }
        throw new RuntimeException("not a legal Chessboard without a king");
    }


    public boolean isMoveLegal(Move move){
        return legalMoves.contains(move);
    }
    //TODO: implement this methods later
    public boolean isInCheck(){
        return isInCheck;
    }
    public boolean isInCheckMate(){
        return false;
    }
    public boolean isInStaleMate(){
        return false;
    }
    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(Move move){
        return null;
    }

    protected abstract Collection<Piece> getActivePieces();
    protected abstract Allience getAllience();

    protected abstract Player getOpponent();

}

