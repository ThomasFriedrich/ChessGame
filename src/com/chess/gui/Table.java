package com.chess.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.chess.engine.board.BoardUtils;

public class Table {

  private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
  private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
  private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

  private final Color lightTileColor = Color.decode("#FFFACD");
  private final Color darkTileColor = Color.decode("#593E1A");
  private final JFrame gameFrame;
  private final BoardPanel boardPanel;

  public Table() {
    gameFrame = new JFrame("JChess");
    gameFrame.setLayout(new BorderLayout());
    final JMenuBar tableMenuBar = createTableMenuBar();
    gameFrame.setJMenuBar(tableMenuBar);
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
      setBackground(isLight?lightTileColor:darkTileColor);
//
//      if (BoardUtils.FIRST_ROW[tileId] || BoardUtils.THIRD_ROW[tileId] || BoardUtils.FIFTH_ROW[tileId] || BoardUtils.SEVENTH_ROW[tileId]) {
//        setBackground(tileId % 2 == 0 ? lightTileColor : darkTileColor);
//      } else if (BoardUtils.SECOND_ROW[tileId] || BoardUtils.FOURTH_ROW[tileId] || BoardUtils.SIXTH_ROW[tileId] || BoardUtils.EIGHT_ROW[tileId]) {
//        setBackground(tileId % 2 != 0 ? lightTileColor : darkTileColor);
//      }
    }
  }

}
