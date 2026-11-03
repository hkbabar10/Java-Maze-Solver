import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Interactive depth-first search with animated backtracking. */
public class MazeSolver {
    private final JFrame frame = new JFrame("Maze Solver & Visualizer");
    private final MazeVisualizer board = new MazeVisualizer();
    private final JComboBox<String> picker = new JComboBox<>(new String[]{"maze1.txt", "maze2.txt", "maze3.txt", "maze4.txt", "maze5.txt"});
    private final JButton start = new JButton("Start"), pause = new JButton("Pause"), reset = new JButton("Reset");
    private final JSlider speed = new JSlider(1, 100, 65);
    private final JLabel status = new JLabel("Choose a maze and press Start");
    private final Deque<Node> stack = new ArrayDeque<>();
    private final Timer timer;
    private Maze maze;
    private boolean[][] seen;
    private int visited;
    private static final int[][] MOVES = {{1,0},{-1,0},{0,1},{0,-1}};

    private static class Node {
        final int row, col;
        int next;
        Node(int r, int c) { row = r; col = c; }
    }

    public MazeSolver() {
        timer = new Timer(delay(), e -> step());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controls.add(new JLabel("Maze:")); controls.add(picker);
        controls.add(start); controls.add(pause); controls.add(reset);
        controls.add(new JLabel("Speed:")); speed.setPreferredSize(new Dimension(125, 24)); controls.add(speed);
        frame.setLayout(new BorderLayout());
        frame.add(controls, BorderLayout.NORTH);
        frame.add(new JScrollPane(board), BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        frame.add(status, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        frame.setLocationRelativeTo(null);
        picker.addActionListener(e -> load());
        start.addActionListener(e -> begin());
        pause.addActionListener(e -> { if (timer.isRunning()) { timer.stop(); pause.setText("Resume"); status.setText("Paused · visited " + visited + " cells"); } else if (!stack.isEmpty()) { timer.start(); pause.setText("Pause"); } });
        reset.addActionListener(e -> load());
        speed.addChangeListener(e -> timer.setDelay(delay()));
        load();
        frame.setVisible(true);
    }

    private int delay() { return Math.max(5, 310 - speed.getValue() * 3); }

    private void load() {
        timer.stop(); stack.clear(); visited = 0;
        start.setEnabled(true); pause.setEnabled(false); pause.setText("Pause");
        try {
            Path file = Paths.get("mazes", (String) picker.getSelectedItem());
            maze = new Maze(file);
            seen = new boolean[maze.cells.length][maze.cells[0].length];
            board.setMaze(maze);
            status.setText("Ready · " + maze.cells[0].length + " × " + maze.cells.length + " grid");
        } catch (Exception ex) {
            maze = null; board.setMaze(null);
            status.setText("Could not load maze: " + ex.getMessage());
            start.setEnabled(false);
        }
    }

    private void begin() {
        if (maze == null) return;
        load();
        if (maze == null) return;
        Node first = new Node(maze.startRow, maze.startCol);
        stack.push(first);
        seen[first.row][first.col] = true;
        board.mark(first.row, first.col, MazeVisualizer.VISITED);
        visited = 1;
        start.setEnabled(false); pause.setEnabled(true);
        timer.start();
    }

    private void step() {
        if (stack.isEmpty()) { finish(false); return; }
        Node node = stack.peek();
        if (node.row == maze.exitRow && node.col == maze.exitCol) {
            List<Point> path = new ArrayList<>();
            for (Node n : stack) path.add(new Point(n.col, n.row));
            board.showPath(path);
            finish(true); return;
        }
        if (node.next >= MOVES.length) {
            stack.pop();
            board.mark(node.row, node.col, MazeVisualizer.BACKTRACKED);
        } else {
            int[] move = MOVES[node.next++];
            int r = node.row + move[0], c = node.col + move[1];
            if (r >= 0 && c >= 0 && r < maze.cells.length && c < maze.cells[0].length
                    && maze.cells[r][c] != '#' && !seen[r][c]) {
                seen[r][c] = true;
                stack.push(new Node(r, c));
                board.mark(r, c, MazeVisualizer.VISITED);
                visited++;
            }
        }
        status.setText("Searching · visited " + visited + " cells · current path " + stack.size() + " cells");
    }

    private void finish(boolean solved) {
        timer.stop(); pause.setEnabled(false); start.setEnabled(true);
        status.setText(solved ? "Solved! · path length " + Math.max(0, stack.size() - 1) + " steps · visited " + visited + " cells" : "No solution found · visited " + visited + " cells");
        stack.clear();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(MazeSolver::new); }
}
