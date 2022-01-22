import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

public class SpiralIterator implements Iterator<Integer> {
    private static int x = -1;
    private static int y = 0;
    private final int[][] spiralMatrix;

    private static int left;
    private static int right;
    private static int top;
    private static int bottom;
    private Direction direction = Direction.EAST;

    public SpiralIterator(int[][] spiralMatrix) {
        this.spiralMatrix = spiralMatrix;
        left = 0;
        right = spiralMatrix[0].length - 1;
        top = 0;
        bottom = spiralMatrix.length - 1;
    }

    public boolean hasNext() {
        return direction.hasNextInOwnDirection() ||
                Objects.requireNonNull(direction.getNext()).hasNextInOwnDirection();
    }

    public Integer next() {
        switch (direction) {
            case EAST:
                if (x < right) {
                    x++;
                } else if (y < bottom) {
                    direction = Direction.SOUTH;
                    y++;
                    top++;
                } else {
                    throw new NoSuchElementException("EAST failed");
                }
                break;
            case SOUTH:
                if (y < bottom) {
                    y++;
                } else if (x > left) {
                    direction = Direction.WEST;
                    x--;
                    right--;
                } else {
                    throw new NoSuchElementException("SOUTH failed");
                }
                break;
            case WEST:
                if (x > left) {
                    x--;
                } else if (y > top) {
                    direction = Direction.NORTH;
                    y--;
                    bottom--;
                } else {
                    throw new NoSuchElementException("WEST failed");
                }
                break;
            case NORTH:
                if (y > top) {
                    y--;
                } else if (x < right) {
                    direction = Direction.EAST;
                    x++;
                    left++;
                } else {
                    throw new NoSuchElementException("NORTH failed");
                }
                break;
        }
        return spiralMatrix[y][x];
    }

    public enum Direction {
        EAST(() -> x < right),
        SOUTH(() -> y < bottom),
        WEST(() -> x > left),
        NORTH(() -> y > top);

        private final Supplier<Boolean> hasNextInDirection;

        Direction(Supplier<Boolean> hasNextInDirection) {
            this.hasNextInDirection = hasNextInDirection;
        }

        public boolean hasNextInOwnDirection() {
            return hasNextInDirection.get();
        }

        public Direction getNext() {
            switch (this) {
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return WEST;
                case WEST:
                    return NORTH;
                case NORTH:
                    return EAST;
            }
            return null;
        }
    }
}
