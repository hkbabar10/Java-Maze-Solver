import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Draws the grid and the search state. */
public class MazeVisualizer extends JPanel {
    public static final byte UNVISITED = 0, VISITED = 1, BACKTRACKED = 2, PATH = 3;
    private static final int CELL = 27;
    private Maze maze;
    private byte[][] states;

    public MazeVisualizer() { setBackground(new Color(246, 248, 252)); }

    public void setMaze(Maze maze) {
        this.maze = maze;
        if (maze != null) {
            states = new byte[maze.cells.length][maze.cells[0].length];
            setPreferredSize(new Dimension(maze.cells[0].length * CELL, maze.cells.length * CELL));
        } else { states = null; setPreferredSize(new Dimension(400, 300)); }
        revalidate(); repaint();
    }

    public void mark(int row, int col, byte state) { states[row][col] = state; repaint(); }
    public void showPath(List<Point> path) { for (Point p : path) states[p.y][p.x] = PATH; repaint(); }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        for (int r = 0; r < maze.cells.length; r++) for (int c = 0; c < maze.cells[0].length; c++) {
            char ch = maze.cells[r][c];
            Color fill = Color.WHITE;
            if (ch == '#') fill = new Color(35, 45, 65);
            else if (states[r][c] == PATH) fill = new Color(76, 175, 130);
            else if (states[r][c] == VISITED) fill = new Color(250, 202, 92);
            else if (states[r][c] == BACKTRACKED) fill = new Color(224, 230, 240);
            if (ch == 'S') fill = new Color(64, 175, 110);
            if (ch == 'E') fill = new Color(225, 85, 95);
            int x = c * CELL, y = r * CELL;
            g2.setColor(fill); g2.fillRect(x, y, CELL, CELL);
            g2.setColor(new Color(210, 217, 227)); g2.drawRect(x, y, CELL, CELL);
            if (ch == 'S' || ch == 'E') {
                g2.setColor(Color.WHITE); g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(String.valueOf(ch), x + 9, y + 19);
            }
        }
        g2.dispose();
    }
}
