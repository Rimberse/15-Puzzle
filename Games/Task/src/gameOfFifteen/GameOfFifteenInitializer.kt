package gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialize the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        var permutation = (1..15).shuffled()

        while (!isEven(permutation)) {
            // Swap two numbers to make the permutation even
            permutation = permutation.shuffled()
        }

        permutation
    }
}
