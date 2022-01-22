import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpiralTest {
    private final int[] row1 = {1, 2, 3, 4};
    private final int[] row2 = {10, 11, 12, 5};
    private final int[] row3 = {9, 8, 7, 6};

    private final int[] row4 = {};
    private final int[] row5 = {1};

    @Test
    public void testSpiral_mainCase() {
        int[][] matrix = {row1, row2, row3};
        Iterator<Integer> spiralIterator = getSpiralIterator(matrix);

        int expected = 1;
        while (spiralIterator.hasNext()) {
            assertEquals(expected, spiralIterator.next());
            expected++;
        }
        assertEquals(13, expected);
    }

    @Test
    public void testSpiral_emptyCase() {
        int[][] matrix = {row4};
        Iterator<Integer> spiralIterator = getSpiralIterator(matrix);
        int expected = 1;
        while (spiralIterator.hasNext()) {
            System.out.println(spiralIterator.next());
            expected++;
        }
        assertEquals(1, expected);
    }

    @Test
    public void testSpiral_singleCase() {
        int[][] matrix = {row5};
        Iterator<Integer> spiralIterator = getSpiralIterator(matrix);
        int expected = 1;
        while (spiralIterator.hasNext()) {
            assertEquals(expected, spiralIterator.next());
            expected++;
        }
        assertEquals(2, expected);
    }

    public Iterator<Integer> getSpiralIterator(int[][] spiralMatrix) {
        return new Iterator<>() {
            private int x = -1;
            private int y = 0;

            private int left = 0;
            private int right = spiralMatrix[0].length - 1;
            private int top = 0;
            private int bottom = spiralMatrix.length - 1;
            private SpiralIterator.Direction direction = SpiralIterator.Direction.EAST;

            public boolean hasNext() {
                switch (direction) {
                    case EAST:
                        return x < right || y < bottom;
                    case SOUTH:
                        return y < bottom || x > left;
                    case WEST:
                        return x > left || y > top;
                    case NORTH:
                        return y > top || x < right;
                }
                return false;
            }

            public Integer next() {
                switch (direction) {
                    case EAST:
                        if (x < right) {
                            x++;
                        } else if (y < bottom) {
                            direction = SpiralIterator.Direction.SOUTH;
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
                            direction = SpiralIterator.Direction.WEST;
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
                            direction = SpiralIterator.Direction.NORTH;
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
                            direction = SpiralIterator.Direction.EAST;
                            x++;
                            left++;
                        } else {
                            throw new NoSuchElementException("NORTH failed");
                        }
                        break;
                }
                return spiralMatrix[y][x];
            }
        };
    }


}
