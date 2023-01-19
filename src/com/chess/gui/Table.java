package com.chess.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

public class Table {

  private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
  private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
  private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
  private static String defaultPieceImagesPath = "art/simple/";

  private final Board chessBoard;

  private final Color lightTileColor = Color.decode("#FFFACD");
  private final Color darkTileColor = Color.decode("#593E1A");
  private final JFrame gameFrame;
  private final BoardPanel boardPanel;

  public Table() {
    gameFrame = new JFrame("JChess");
    gameFrame.setLayout(new BorderLayout());
    final JMenuBar tableMenuBar = createTableMenuBar();
    gameFrame.setJMenuBar(tableMenuBar);
    chessBoard = Board.cearteStandardBoard();
    boardPanel = new BoardPanel();
    gameFrame.add(boardPanel, BorderLayout.CENTER);
    gameFrame.setSize(OUTER_FRAME_DIMENSION);
    gameFrame.setVisible(true);
    gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  private JMenuBar createTableMenuBar() {
    JMenuBar tableMenuBar = new JMenuBar();
    tableMenuBar.add(createFileMenu());
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
  }

  private class TilePanel extends JPanel {

    private final int tileId;

    private TilePanel(BoardPanel boardPanel, int tileId) {
      super(new GridBagLayout());
      this.tileId = tileId;
      setPreferredSize(TILE_PANEL_DIMENSION);
      assignTileColor();
      validate();
    }

    private void assignTileColor() {

      boolean isLight = (tileId + tileId / 8) % 2 == 0;
      setBackground(isLight ? lightTileColor : darkTileColor);
      assignTilePieceIcon(chessBoard);
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
  }

}
