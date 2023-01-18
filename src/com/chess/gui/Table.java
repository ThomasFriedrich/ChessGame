package com.chess.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Table {

  private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
  private final JFrame gameFrame;

  public Table() {
    gameFrame = new JFrame("JChess");
    final JMenuBar tableMenuBar = new JMenuBar();
    populateMenuBar(tableMenuBar);
    gameFrame.setJMenuBar(tableMenuBar);
    gameFrame.setSize(OUTER_FRAME_DIMENSION);
    gameFrame.setVisible(true);
    gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  private void populateMenuBar(JMenuBar tableMenuBar) {
    tableMenuBar.add(createFileMenu());
  }

  private JMenu createFileMenu() {
    final JMenu fileMenu = new JMenu("File");
    final JMenuItem openPGN = new JMenuItem("Load PGN File");
    fileMenu.add(openPGN);
    openPGN.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("open up that pgn file!");
      }
    });
    return fileMenu;
  }
}
