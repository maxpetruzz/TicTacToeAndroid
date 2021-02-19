package edu.quinnipiac.tictactoe;
/**
 * ITicTacToe interface.
 * @author relkharboutly
 * @date 1/5/2017
 */
public interface ITicTacToe {
    // Name-constants to represent the seeds and cell contents
    public static final int EMPTY = 0;
    public static final int CROSS = 1;
    public static final int NOUGHT = 2;

    // Name-constants to represent the various states of the game
    public static final int PLAYING = 0;
    public static final int TIE = 1;
    public static final int CROSS_WON = 2;
    public static final int NOUGHT_WON = 3;

    /**
     * clear the board of all X's and Y's by setting all spots to EMPTY
     */
    public void clearBoard();
    /** Sets the given player at the given location on the game board.
     * The location must be available, or the board will not be changed
     * @param player - HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(int player, int location);
    /** Returns the best move for the computer to make. You must call setMove()
     * to actually make the computer move to the location
     * @return the best move for the computer to make (0-8)
     */
    public int getComputerMove();
    /**
     * Check for a winner and return a status value indicating who has won.
     * @return PLAYING if still playing, TIE if its a tie, CROSS_WON if X won, or NOUGH_WON if 0 won
     */
    public int checkForWinner();
}
