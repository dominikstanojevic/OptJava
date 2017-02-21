package hr.fer.zemris.optjava.dz12.models;

import hr.fer.zemris.optjava.dz12.models.nodes.Action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dominik on 6.2.2017..
 */
public class Grid {
    private boolean[][] food;

    private Position position = new Position();
    private int collected = 0;
    private int movesLeft;
    private int move;

    private Action[] actions;

    public Grid(boolean[][] food) {
        Objects.requireNonNull(food, "Food array cannot be null.");

        this.food = food;
    }

    public static Grid loadMap(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);

        String[] dimensions = lines.get(0).trim().split("x");
        if (dimensions.length != 2) {
            throw new RuntimeException("Invalid dimension line. Expected <rows>x<columns>.");
        }
        int rows = Integer.parseInt(dimensions[0].trim());
        int columns = Integer.parseInt(dimensions[1].trim());

        boolean[][] food = new boolean[rows][columns];

        for (int y = 0; y < rows; y++) {
            String[] row = lines.get(y + 1).trim().split("");
            for (int x = 0; x < columns; x++) {
                food[y][x] = row[x].equals("1");
            }
        }

        return new Grid(food);
    }

    public static Grid copy(Grid src) {
        int rows = src.food.length;
        int columns = src.food[0].length;

        Grid dest = new Grid(new boolean[rows][columns]);
        copy(src, dest);

        return dest;
    }

    public static void copy(Grid src, Grid dest) {
        Objects.requireNonNull(src, "Source map cannot be null.");
        Objects.requireNonNull(dest, "Destination map cannot be null.");

        boolean[][] srcArray = src.food;
        boolean[][] destArray = dest.food;

        for (int i = 0; i < srcArray.length; i++) {
            for (int j = 0; j < srcArray[0].length; j++) {
                destArray[i][j] = srcArray[i][j];
            }
        }
    }

    public void reset(int moves) {
        position.reset();
        actions = new Action[moves];

        move = 0;
        movesLeft = moves;
        collected = collectFood() ? 1 : 0;
    }

    public int play(Ant ant, int moves) {
        reset(moves);

        while (movesLeft > 0) {
            ant.getNextAction(this);
        }

        return collected;
    }

    private boolean collectFood() {
        if (food[position.y][position.x]) {
            food[position.y][position.x] = false;
            return true;
        } else {
            return false;
        }
    }

    public void executeAction(Action action) {
        if (movesLeft <= 0) {
            return;
        }

        actions[move] = action;
        move++;

        switch (action) {
            case LEFT:
                position.direction = Position.Direction.left.get(position.direction);
                break;
            case RIGHT:
                position.direction = Position.Direction.right.get(position.direction);
                break;
            case MOVE:
                position.direction.changePosition(position);
                position.x = Math.floorMod(position.x, food[0].length);
                position.y = Math.floorMod(position.y, food.length);
                break;
            default:
                throw new RuntimeException();
        }

        if (collectFood()) {
            collected++;
        }

        movesLeft--;
    }

    public boolean isFoodAhead() {
        switch (position.direction) {
            case UP:
                return food[Math.floorMod(position.y - 1, food.length)][position.x];
            case LEFT:
                return food[position.y][Math.floorMod(position.x - 1, food[0].length)];
            case RIGHT:
                return food[position.y][Math.floorMod(position.x + 1, food[0].length)];
            case DOWN:
                return food[Math.floorMod(position.y + 1, food.length)][position.x];
            default:
                throw new RuntimeException();
        }
    }

    public boolean[][] getFood() {
        return food;
    }

    public Action[] getActions() {
        return actions;
    }

    public Position getPosition() {
        return position;
    }

    public static class Position {
        public int x;
        public int y;
        public Direction direction;

        public enum Direction {
            UP(0, -1), LEFT(-1, 0), RIGHT(1, 0), DOWN(0, 1);

            private int offsetX;
            private int offsetY;

            Direction(int offsetX, int offsetY) {
                this.offsetX = offsetX;
                this.offsetY = offsetY;
            }

            void changePosition(Position position) {
                position.x = position.x + offsetX;
                position.y = position.y + offsetY;
            }

            static Map<Direction, Direction> left = new HashMap<>();
            static Map<Direction, Direction> right = new HashMap<>();

            static {
                left.put(UP, LEFT);
                left.put(LEFT, DOWN);
                left.put(DOWN, RIGHT);
                left.put(RIGHT, UP);

                right.put(UP, RIGHT);
                right.put(RIGHT, DOWN);
                right.put(DOWN, LEFT);
                right.put(LEFT, UP);
            }
        }

        public Position() {
            reset();
        }

        public void reset() {
            x = y = 0;
            direction = Direction.RIGHT;
        }
    }


}
