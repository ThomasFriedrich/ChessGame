package com.chess.gui;

import static com.chess.gui.Table.BoardDirection.NORMAL;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

public class Table {

  private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
  private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
  private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
  private static String defaultPieceImagesPath = "art/simple/";

  private Board chessBoard;

  private final Color lightTileColor = Color.decode("#FFFACD");
  private final Color darkTileColor = Color.decode("#593E1A");
  private final JFrame gameFrame;
  private final BoardPanel boardPanel;

  private BoardDirection boardDirection;

  private Tile sourceTile;
  private Tile destinationTile;
  private Piece humanMovedPiece;

  public Table() {
    gameFrame = new JFrame("JChess");
    gameFrame.setLayout(new BorderLayout());
    final JMenuBar tableMenuBar = createTableMenuBar();
    gameFrame.setJMenuBar(tableMenuBar);
    chessBoard = Board.cearteStandardBoard();
    boardPanel = new BoardPanel();
    boardDirection = NORMAL;
    gameFrame.add(boardPanel, BorderLayout.CENTER);
    gameFrame.setSize(OUTER_FRAME_DIMENSION);
    gameFrame.setVisible(true);
    gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  private JMenuBar createTableMenuBar() {
    JMenuBar tableMenuBar = new JMenuBar();
    tableMenuBar.add(createFileMenu());
    tableMenuBar.add(createPreferenceMenu());
    return tableMenuBar;
  }

  private JMenu createFileMenu() {
    final JMenu fileMenu = new JMenu("File");
    final JMenuItem openPGN = new JMenuItem("Load PGN File");
    fileMenu.add(openPGN);
    openPGN.addActionListener(actionEvent -> System.out.println("open up that pgn file!"));

    final JMenuItem exitMenuItem = new JMenuItem("Exit");
    fileMenu.add(exitMenuItem);
    exitMenuItem.addActionListener(actionEvent -> System.exit(0));

    return fileMenu;
  }

  private JMenu createPreferenceMenu(){
    final JMenu preferenceMenu = new JMenu("Preferences");
    final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
    flipBoardMenuItem.addActionListener(actionEvent -> {
      boardDirection = boardDirection == NORMAL ? BoardDirection.FLIPPED : NORMAL;
      boardPanel.drawBoard(chessBoard);
    });
    preferenceMenu.add(flipBoardMenuItem);
    return preferenceMenu;
  }

  public enum BoardDirection {
    NORMAL {
      @Override List<TilePanel> traverse(List<TilePanel> tilePanels) {
        return tilePanels;
      }
    },FLIPPED {
      @Override List<TilePanel> traverse(List<TilePanel> tilePanels) {
        return Lists.reverse(tilePanels);
      }
    };

    abstract List<TilePanel> traverse(List<TilePanel> tilePanels);

  }

  private class BoardPanel extends JPanel {
    final List<TilePanel> boardTiles;

    BoardPanel() {
      super(new GridLayout(8, 8));
      this.boardTiles = new ArrayList<>();
      for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
        final TilePanel tilePanel = new TilePanel(this, i);
        boardTiles.add(tilePanel);
        add(tilePanel);
      }
      setPreferredSize(BOARD_PANEL_DIMENSION);
      validate();
    }

    public void drawBoard(Board chessBoard) {
      removeAll();
      for (TilePanel boardTile : boardDirection.traverse(boardTiles)) {
        boardTile.drawTile(chessBoard);
        add(boardTile);
        if(sourceTile!=null){
          boardTile.highlightLegalMoves(chessBoard);
        }
      }
      validate();
      repaint();

    }
  }

  private class TilePanel extends JPanel {

    private final int tileId;

    private TilePanel(BoardPanel boardPanel, int tileId) {
      super(new GridBagLayout());
      this.tileId = tileId;
      setPreferredSize(TILE_PANEL_DIMENSION);
      assignTileColor();
      assignTilePieceIcon(chessBoard);
      addMouseListener(new MouseListener() {
        @Override public void mouseClicked(final MouseEvent mouseEvent) {
          if(isRightMouseButton(mouseEvent)){
            sourceTile=null;
            destinationTile=null;
            humanMovedPiece=null;

          }else if(isLeftMouseButton(mouseEvent)){
            if(sourceTile==null){
              System.out.println("first click!");
              sourceTile=chessBoard.getTile(tileId);
              humanMovedPiece = sourceTile.getPiece();
              if(humanMovedPiece == null){
                sourceTile = null;
              }
            }else {
              //second click
              System.out.println("second click!");
              destinationTile = chessBoard.getTile(tileId);
              final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
              final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
              if(transition.getMoveStatus()== MoveStatus.DONE){
                chessBoard=transition.getTransitionBoard();
                //TODO add the move taht was made to the move log
              }
              sourceTile=null;
              destinationTile=null;
              humanMovedPiece=null;
            }
            SwingUtilities.invokeLater(() -> boardPanel.drawBoard(chessBoard));
          }


        }

        @Override public void mousePressed(final MouseEvent mouseEvent) {

        }

        @Override public void mouseReleased(final MouseEvent mouseEvent) {

        }

        @Override public void mouseEntered(final MouseEvent mouseEvent) {

        }

        @Override public void mouseExited(final MouseEvent mouseEvent) {

        }
      });
      validate();
    }

    private void assignTileColor() {

      boolean isLight = (tileId + tileId / 8) % 2 == 0;
      setBackground(isLight ? lightTileColor : darkTileColor);
    }

    private void assignTilePieceIcon(final Board board) {
      this.removeAll();
      if (board.getTile(tileId).isTileOccupied()) {
        try {
          final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath
              + board.getTile(tileId).getPiece().getPieceAllience().toString().substring(0, 1)
          +board.getTile(tileId).getPiece().toString()
          +".gif"));
          add(new JLabel(new ImageIcon(image)));
        }catch (IOException ioe){
          ioe.printStackTrace();

        }
      }
    }

    public void drawTile(Board board) {
      assignTileColor();
      assignTilePieceIcon(board);
      validate();
      repaint();
    }
    private void highlightLegalMoves(final Board board){
      if(true){
        for (Move move:pieceLegalMoves(board)){
          if(move.getDestinationCoordinate() == tileId){
            try{
              add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
            }catch (IOException e){
              e.printStackTrace();
            }
          }
        }
      }
    }

    private Collection<Move> pieceLegalMoves(Board board) {
      if(humanMovedPiece != null && humanMovedPiece.getPieceAllience()==board.getCurrentPlayer().getAllience()){
        return humanMovedPiece.calculateLegalMoves(board);
      }
      return Collections.emptyList();
    }

  }



}
