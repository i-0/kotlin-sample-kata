import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/* Rules:
    1. Any live cell with fewer than two live neighbors dies, as if by underpopulation
    2. Any live cell with two or three live neighbors lives in to the next generation
    3. Any live cell with more than three live neighbors dies, as if by overpopulation
    4. Any dead cell with exactly three neighbors becomes a live cell, as if by reproduction
 */
class GameOfLifeTest {
    @Test
    fun `cell invariants`() {
        assertThat(Cell(0, 0).x).isZero()
        assertThat(Cell(0, 1).y).isOne()
    }

    @Test
    fun `cell equals works`() {
        assertTrue(Cell(0, 0) == Cell(0, 0))
        assertTrue(Cell(1, 1) == Cell(1, 1))
    }

    @Test
    fun `is neighbour`() {
        assertTrue(Cell(0, 0).isNeighbour(Cell(0, 1)))
        assertTrue(Cell(0, 1).isNeighbour(Cell(0, 0)))
        assertFalse(Cell(0, 0).isNeighbour(Cell(0, 0)))
        assertTrue(Cell(1, 1).isNeighbour(Cell(0, 0)))

        assertFalse(Cell(1, 1).isNeighbour(Cell(-1, -1)))
        assertFalse(Cell(1, -1).isNeighbour(Cell(-1, -1)))
        assertFalse(Cell(0, 0).isNeighbour(Cell(99, 99)))
        assertFalse(Cell(1, -1).isNeighbour(Cell(1, 1)))
    }

    @Test
    fun `introducing board`() {
        assertTrue(Board(listOf(Cell(0, 0))).contains(Cell(0, 0)))
        assertFalse(Board(listOf(Cell(1, 1))).contains(Cell(0, 0)))
    }

    @Test
    fun `alive see rule 1`() {
        val b = Board(listOf(Cell(-1,0), Cell(0,0), Cell(0,1)))
        assertFalse(b.alive(Cell(0,1))) // 1 neighbour
        assertFalse(b.alive(Cell(99,99))) // no neighbor whatsoever
    }

    @Test
    fun `alive see rule 2`() {
    }

    @Test
    fun `find neighbours on a board`() {
        val b = Board(listOf(Cell(0, 0), Cell(0, 1), Cell(-1, 0)))
        val n = b.neighbors(Cell(0, 0))
        assertThat(n).hasSize(2) // 2 neighbors
        assertThat(n).contains(Cell(0, 1), Cell(-1,0))

        val b2 = Board(listOf(Cell(-1,0), Cell(0,0), Cell(0,1)))
        assertThat(b2.neighbors(Cell(0,1))).hasSize(1) // 1 neighbour
    }
}

data class Board(val cells: List<Cell>) {
    fun contains(cell: Cell): Boolean = cells.contains(cell)
    fun neighbors(cell: Cell): List<Cell> = cells.filter { it.isNeighbour(cell) }
    fun alive(cell: Cell): Boolean {
        val neighbor = this.neighbors(cell)
        return when (neighbor.size) {
            2 -> true
            else -> false
        }
    }


}

/*  (x,y)
    (-1,-1), (0,-1), (1,-1)
    (-1, 0), (0,0), (1,0)
    (-1, 1), (0,1), (1,1)
 */
data class Cell(val x: Int, val y: Int) {
    fun isNeighbour(other: Cell): Boolean = when {
        this == other -> false
        abs(this.x - other.x) > 1 -> false
        abs(this.y - other.y) > 1 -> false
        else -> true
    }
}
