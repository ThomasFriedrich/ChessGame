import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class Main {
  public static void main(String[] args) {
    Board board = Board.cearteStandardBoard();

    System.out.println(board);

    Table table = new Table();


  }
}
