// Author: Max Petruzziello
// SER210 Spring 2021
// Assignment 1 Part 2

package edu.quinnipiac.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements ITicTacToe {

    private int playerToken, computerToken;
    private String playerName;
    private char playerCharacter;
    private char compCharacter;
    private int location;
    private int cScore = 0;
    private int pScore = 0;
    private String compName = "Opponent";
    private Button clickedButton;
    private int[][] board = new int[5][5];
    private List<Integer> availableSpaces = new ArrayList<>();
    private TextView computerScoreLabel, playerScoreLabel;

    // set initial values
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializeSpaces();
        Intent intent = getIntent();
        playerName = (String) intent.getStringExtra("name");
        Bundle bundle = getIntent().getExtras();
        int playerCharSelected = bundle.getInt("playerCharacter");
        playerScoreLabel = (TextView) findViewById(R.id.PlayerScore);
        playerScoreLabel.setText(playerName + ": " + pScore);
        computerScoreLabel = (TextView) findViewById(R.id.computerScore);
        computerScoreLabel.setText(compName + ": " + cScore);
        if (playerCharSelected == CROSS) {
            playerToken = CROSS;
            playerCharacter = 'X';
            computerToken = NOUGHT;
            compCharacter = 'O';
        } else if (playerCharSelected == NOUGHT) {
            playerToken = NOUGHT;
            computerToken = CROSS;
            playerCharacter = 'O';
            compCharacter = 'X';
        }
    }

    // called when player clicks a board button - calls setMove to mark that button
    public void boardClicked(View view) {
        clickedButton = (Button) findViewById(view.getId());
        location = Integer.parseInt((String) clickedButton.getTag());
        setMove(playerCharacter, location);
    }

    // resets the game
    public void resetGame(View view) {
        cScore = 0;
        pScore = 0;
        computerScoreLabel.setText(compName + ": " + cScore);
        playerScoreLabel.setText(playerName + ": " + pScore);
        board = new int[5][5];
        initializeSpaces();
        clearBoard();
    }

    // continuing the game - only called from a Win, Loss, or Tie Alert Dialog
    public void newGame(){
        board = new int[5][5];
        initializeSpaces();
        clearBoard();
    }

    // called when the resetButton is clicked or when the alertdialog calls NewGame or ResetGame
    @Override
    public void clearBoard() {
        for (int i = 0; i <= 24; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
                    this.getPackageName()));
            button.setText("");
            button.setEnabled(true);
        }
        // the reset button is briefly disabled when a player wins or loses - we re-enable it when the game restarts after the alert dialog
        Button button = (Button) findViewById(getResources().getIdentifier("resetButton", "id",
                this.getPackageName()));
        button.setEnabled(true);
    }

    // sets Player's position once button is clicked
    @Override
    public void setMove(int player, int location) {
        int x = location/5;
        int y = location%5;
        if (playerToken == NOUGHT) {
            clickedButton.setText("" + playerCharacter);
            clickedButton.setEnabled(false);
            board[x][y] = NOUGHT;
        } else {
            clickedButton.setText("" + playerCharacter);
            clickedButton.setEnabled(false);
            board[x][y] = CROSS;
        }
        // remove space from available spaces bc no longer available
        availableSpaces.remove(Integer.valueOf(location));
        // check for winner; act accordingly
        int winner = checkForWinner();
        if (winner == playerToken){
            playerWon();
            return;
        } else if (winner == 3){
            nobodyWon();
        }
        // allow until no more spots open
        if (availableSpaces.size() != 0 ) {
            getComputerMove();
            winner = checkForWinner();
            if (winner == computerToken){
                computerWon();
                return;
            }
        } else {
            return;
        }
    }

    // generate computer move
    @Override
    public int getComputerMove() {
        boolean notGoodRandom = true;
        if (availableSpaces == null) {
            System.exit(0);
        }
        while (notGoodRandom == true) {
            // get random location
            Collections.shuffle(availableSpaces);
            int randomLoc = availableSpaces.get(0);
            boolean locationIsAvailable = isOpenSpot(randomLoc);
            if (locationIsAvailable == true) {
                int x = randomLoc/5;
                int y = randomLoc%5;
                if (computerToken == NOUGHT) {
                    board[x][y] = NOUGHT;
                } else {
                    board[x][y] = CROSS;
                }
                // set marked location button to disabled and remove location from availablespaces
                Button button = (Button) findViewById(getResources().getIdentifier("button" + randomLoc, "id",
                        this.getPackageName()));
                button.setText("" + compCharacter);
                button.setEnabled(false);
                availableSpaces.remove(Integer.valueOf(randomLoc));
                notGoodRandom = false;
            } else {
                notGoodRandom = true;
            }
        }
        return 0;
    }

    // check if spot is open - used when computer attempts a random location
    public boolean isOpenSpot(int location) {
        if (availableSpaces.contains(location)) {
            return true;
        } else {
            return false;
        }
    }

    // method to display a win by the user
    public void playerWon(){
        pScore++;
        playerScoreLabel.setText(playerName + ": " + pScore);
        // briefly disable all buttons - player can see where they won!
        disableAllButtons();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("You Won!")
                .setMessage("What do you want to do next?")
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })
                .setNegativeButton("Reset Game",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      resetGame(null);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        // handler used to generate a 1 second pause between WIN and win alert dialog popping up
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.show();
            }
        }, 1000);
    }

    // called if nobody wins - A draw
    public void nobodyWon(){
        // briefly disable all buttons
        disableAllButtons();
       AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("It's a draw! Nobody Won!")
                .setMessage("What do you want to do next?")
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })
                .setNegativeButton("Reset Game",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame(null);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        // handler used to generate a 1 second pause between tie and tie alert dialog popping up
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.show();
            }
        }, 1000);
    }

    public void computerWon(){
        cScore++;
        computerScoreLabel.setText(compName + ": " + cScore);
        // briefly disable all buttons - player can see where they lost :(
        disableAllButtons();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("You Lost :/")
                .setMessage("What do you want to do next?")
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })
                .setNegativeButton("Reset Game",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame(null);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        // handler used to generate a 1 second pause between loss and loss alert dialog popping up
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.show();
            }
        }, 1000);
    }

    @Override
    public int checkForWinner() {
        // if the board is full - return checkforwinner = 3 ; calls the method NobodyWon()
        if (availableSpaces.size() == 0){
            return 3;
        }

        // check for horizontal and vertical wins

        for (int i = 0; i < 5; i++) {
            if ((board[i][0] == CROSS && board[i][1] == CROSS && board[i][2] == CROSS && board[i][3] == CROSS) ||
                    (board[i][1] == CROSS && board[i][2] == CROSS && board[i][3] == CROSS && board[i][4] == CROSS)) {
                return CROSS;
            } else if ((board[i][0] == NOUGHT && board[i][1] == NOUGHT && board[i][2] == NOUGHT && board[i][3] == NOUGHT) ||
                    (board[i][1] == NOUGHT && board[i][2] == NOUGHT && board[i][3] == NOUGHT && board[i][4] == NOUGHT)) {
                return NOUGHT;
            }
        }

        for (int j = 0; j < 5; j++) {
            if ((board[0][j] == CROSS && board[1][j] == CROSS && board[2][j] == CROSS && board[3][j] == CROSS) ||
                    (board[1][j] == CROSS && board[2][j] == CROSS && board[3][j] == CROSS && board[4][j] == CROSS)) {
                return CROSS;
            } else if ((board[0][j] == NOUGHT && board[1][j] == NOUGHT && board[2][j] == NOUGHT && board[3][j] == NOUGHT) ||
                    (board[1][j] == NOUGHT && board[2][j] == NOUGHT && board[3][j] == NOUGHT && board[4][j] == NOUGHT)) {
                return NOUGHT;
            }
        }

        // check for specific diagonal wins

        if ((board[0][0] == CROSS && board[1][1] == CROSS && board[2][2] == CROSS && board[3][3] == CROSS)
                || (board[1][1] == CROSS && board[2][2] == CROSS && board[3][3] == CROSS && board[4][4] == CROSS)) {
            return CROSS;
        } else if ((board[0][1] == CROSS && board[1][2] == CROSS && board[2][3] == CROSS && board[3][4] == CROSS)
                || (board[1][0] == CROSS && board[2][1] == CROSS && board[3][2] == CROSS && board[4][3] == CROSS)) {
            return CROSS;
        } else if ((board[3][0] == CROSS && board[2][1] == CROSS && board[1][2] == CROSS && board[0][3] == CROSS)
                || (board[4][1] == CROSS && board[3][2] == CROSS && board[2][3] == CROSS && board[1][4] == CROSS)) {
            return CROSS;
        } else if ((board[4][0] == CROSS && board[3][1] == CROSS && board[2][2] == CROSS && board[1][3] == CROSS)
                || (board[3][1] == CROSS && board[2][2] == CROSS && board[1][3] == CROSS && board[0][4] == CROSS)) {
            return CROSS;
        }

        if ((board[0][0] == NOUGHT && board[1][1] == NOUGHT && board[2][2] == NOUGHT && board[3][3] == NOUGHT)
                || (board[1][1] == NOUGHT && board[2][2] == NOUGHT && board[3][3] == NOUGHT && board[4][4] == NOUGHT)) {
            return NOUGHT;
        } else if ((board[0][1] == NOUGHT && board[1][2] == NOUGHT && board[2][3] == NOUGHT && board[3][4] == NOUGHT)
                || (board[1][0] == NOUGHT && board[2][1] == NOUGHT && board[3][2] == NOUGHT && board[4][3] == NOUGHT)) {

            return NOUGHT;
        } else if ((board[3][0] == NOUGHT && board[2][1] == NOUGHT && board[1][2] == NOUGHT && board[0][3] == NOUGHT)
                || (board[4][1] == NOUGHT && board[3][2] == NOUGHT && board[2][3] == NOUGHT && board[1][4] == NOUGHT)) {
            return NOUGHT;
        } else if ((board[4][0] == NOUGHT && board[3][1] == NOUGHT && board[2][2] == NOUGHT && board[1][3] == NOUGHT)
                || (board[3][1] == NOUGHT && board[2][2] == NOUGHT && board[1][3] == NOUGHT && board[0][4] == NOUGHT)) {
            return NOUGHT;
        }

        return 0;
    }

    // initialize ArrayList of availablespaces
    public void initializeSpaces() {
        availableSpaces.clear();
        Collections.addAll(availableSpaces, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24);
    }

    // used to briefly disable all buttons before a Win, Lose, or Tie AlertDialog - all buttons are re-enable when user makes next choice from alert dialog
    public void disableAllButtons(){
        for (int i = 0; i <= 24; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
                    this.getPackageName()));
            button.setEnabled(false);
        }
        Button button = (Button) findViewById(getResources().getIdentifier("resetButton", "id",
                this.getPackageName()));
        button.setEnabled(false);
    }
}