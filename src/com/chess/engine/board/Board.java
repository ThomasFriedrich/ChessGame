package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Allience;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rock;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {

  private final List<Tile> gameBoard;
  private final Collection<Piece> whitePieces;
  private final Collection<Piece> blackPieces;
  private final WhitePlayer whitePlayer;
  private final BlackPlayer blackPlayer;
  private final Player currentPlayer;

  private Board(Builder builder) {

    gameBoard = createGameBoard(builder);
    blackPieces = calculateActivePieces(this.gameBoard, Allience.WHITE);
    whitePieces = calculateActivePieces(this.gameBoard, Allience.BLACK);

    final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(whitePieces);
    final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(blackPieces);

    whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
    blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
    currentPlayer = builder.nextMoveMaker.choosePlayer(whitePlayer,blackPlayer);

  }

  @Override public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
      final String tileText = this.gameBoard.get(i).toString();
      builder.append(String.format("%3s", tileText));
      if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0){
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
    final List<Move> legalMoves = new ArrayList<>();
    for (Piece piece : pieces) {
      legalMoves.addAll(piece.calculateLegalMoves(this));
    }
    return ImmutableList.copyOf(legalMoves);
  }

  private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Allience allience) {
    final List<Piece> activePieces = new ArrayList<>();
    for (Tile tile : gameBoard) {
      if (tile.isTileOccupied()) {
        final Piece piece = tile.getPiece();
        if (piece.getPieceAllience() == allience) {
          activePieces.add(piece);
        }
      }
    }

    return ImmutableList.copyOf(activePieces);
  }

  public Tile getTile(final int tileCoordinate) {
    return gameBoard.get(tileCoordinate);
  }

  private static List<Tile> createGameBoard(Builder builder) {
    final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
    for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
      tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
    }
    return ImmutableList.copyOf(tiles);
  }

  public static Board cearteStandardBoard() {
    Builder builder = new Builder();
    builder.setPiece(new Rock(0, Allience.BLACK));
    builder.setPiece(new Knight(1, Allience.BLACK));
    builder.setPiece(new Bishop(2, Allience.BLACK));
    builder.setPiece(new Queen(3, Allience.BLACK));
    builder.setPiece(new King(4, Allience.BLACK));
    builder.setPiece(new Bishop(5, Allience.BLACK));
    builder.setPiece(new Knight(6, Allience.BLACK));
    builder.setPiece(new Rock(7, Allience.BLACK));
    builder.setPiece(new Pawn(8, Allience.BLACK));
    builder.setPiece(new Pawn(9, Allience.BLACK));
    builder.setPiece(new Pawn(10, Allience.BLACK));
    builder.setPiece(new Pawn(11, Allience.BLACK));
    builder.setPiece(new Pawn(12, Allience.BLACK));
    builder.setPiece(new Pawn(13, Allience.BLACK));
    builder.setPiece(new Pawn(14, Allience.BLACK));
    builder.setPiece(new Pawn(15, Allience.BLACK));
    //white pieces
    builder.setPiece(new Pawn(48, Allience.WHITE));
    builder.setPiece(new Pawn(49, Allience.WHITE));
    builder.setPiece(new Pawn(50, Allience.WHITE));
    builder.setPiece(new Pawn(51, Allience.WHITE));
    builder.setPiece(new Pawn(52, Allience.WHITE));
    builder.setPiece(new Pawn(53, Allience.WHITE));
    builder.setPiece(new Pawn(54, Allience.WHITE));
    builder.setPiece(new Pawn(55, Allience.WHITE));
    builder.setPiece(new Rock(56, Allience.WHITE));
    builder.setPiece(new Knight(57, Allience.WHITE));
    builder.setPiece(new Bishop(58, Allience.WHITE));
    builder.setPiece(new Queen(59, Allience.WHITE));
    builder.setPiece(new King(60, Allience.WHITE));
    builder.setPiece(new Bishop(61, Allience.WHITE));
    builder.setPiece(new Knight(62, Allience.WHITE));
    builder.setPiece(new Rock(63, Allience.WHITE));
    builder.setMoveMaker(Allience.WHITE);
    return builder.build();
  }

  public Collection<Piece> getWhitePieces() {
    return whitePieces;
  }

  public Collection<Piece> getBlackPieces() {
    return blackPieces;
  }

  public WhitePlayer getWhitePlayer() {
    return whitePlayer;
  }

  public BlackPlayer getBlackPlayer() {
    return blackPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Iterable<Move> getAllLegalMoves() {
    return Iterables.unmodifiableIterable(Iterables.concat(whitePlayer.getLegalMoves(),blackPlayer.getLegalMoves()));
  }

  public static class Builder {

    Map<Integer, Piece> boardConfig = new HashMap<>();
    Allience nextMoveMaker;
    Pawn enPassentPawn;

    public Builder() {

    }

    public Builder setPiece(final Piece piece) {
      this.boardConfig.put(piece.getPiecePosition(), piece);
      return this;
    }

    public Builder setMoveMaker(final Allience nextMoveMaker) {
      this.nextMoveMaker = nextMoveMaker;
      return this;
    }

    public Board build() {
      return new Board(this);
    }

    public void setEnPassentPawn(Pawn enPassentPawn) {
      this.enPassentPawn = enPassentPawn;
    }

  }

}
