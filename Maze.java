import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Validated maze input. # = wall, S = start, E = exit, space = corridor. */
public class Maze {
    public final char[][] cells;
    public final int startRow, startCol, exitRow, exitCol;

    public Maze(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty() || lines.get(0).isEmpty()) throw new IOException("Maze is empty.");
        int width = lines.get(0).length();
        cells = new char[lines.size()][width];
        int sr = -1, sc = -1, er = -1, ec = -1;
        for (int r = 0; r < lines.size(); r++) {
            String line = lines.get(r);
            if (line.length() != width) throw new IOException("Maze rows must have equal width (row " + (r + 1) + ").");
            for (int c = 0; c < width; c++) {
                char ch = line.charAt(c);
                if (ch != '#' && ch != ' ' && ch != 'S' && ch != 'E') throw new IOException("Invalid maze character at row " + (r + 1));
                if (ch == 'S') { if (sr >= 0) throw new IOException("Multiple start positions."); sr = r; sc = c; }
                if (ch == 'E') { if (er >= 0) throw new IOException("Multiple exits."); er = r; ec = c; }
                cells[r][c] = ch;
            }
        }
        if (sr < 0 || er < 0) throw new IOException("Maze must contain exactly one S and one E.");
        startRow = sr; startCol = sc; exitRow = er; exitCol = ec;
    }
}
