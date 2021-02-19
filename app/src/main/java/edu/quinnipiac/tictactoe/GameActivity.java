package edu.quinnipiac.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements ITicTacToe {

    private int currentState = ITicTacToe.PLAYING;
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

    public void resetGame(View view) {
        cScore = 0;
        pScore = 0;
        computerScoreLabel.setText(compName + ": " + cScore);
        playerScoreLabel.setText(playerName + ": " + pScore);
        board = new int[5][5];
        currentState = PLAYING;
        initializeSpaces();
        clearBoard();
    }

    @Override
    public void clearBoard() {
        for (int i = 0; i <= 24; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
                    this.getPackageName()));
            button.setText("");
            button.setEnabled(true);
        }
    }

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
        availableSpaces.remove(Integer.valueOf(location));
        int winner = checkForWinner();
        if (winner == playerToken){
            playerWon();
            return;
        }
        if (availableSpaces.size() != 0 ) {
            getComputerMove();
            Log.d("string", Arrays.deepToString(board));
            winner = checkForWinner();
            if (winner == computerToken){
                computerWon();
                return;
            }
        } else {
            return;
        }
    }

    @Override
    public int getComputerMove() {
        boolean notGoodRandom = true;
        if (availableSpaces == null) {
            System.exit(0);
        }
        while (notGoodRandom == true) {
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
        checkForWinner();
        return 0;
    }

    public boolean isOpenSpot(int location) {
        if (availableSpaces.contains(location)) {
            return true;
        } else {
            return false;
        }
    }

    public void playerWon(){
        pScore++;
        playerScoreLabel.setText(playerName + ": " + pScore);
        new AlertDialog.Builder(this)
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
                .show();
    }

    public void newGame(){
        board = new int[5][5];
        initializeSpaces();
        clearBoard();
    }

    public void computerWon(){
        cScore++;
        computerScoreLabel.setText(compName + ": " + cScore);
        new AlertDialog.Builder(this)
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
                .show();
    }

    @Override
    public int checkForWinner() {
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


//    @Override
//    public int checkForWinner() {
//        for (int i = 0; i < 5; i++) {
//            if ((board[i][0] == CROSS && board[i][1] == CROSS && board[i][2] == CROSS && board[i][3] == CROSS) ||
//                    (board[i][1] == CROSS && board[i][2] == CROSS && board[i][3] == CROSS && board[i][4] == CROSS)) {
//                System.out.println("cross won");
//                return CROSS_WON;
//            } else if ((board[i][0] == NOUGHT && board[i][1] == NOUGHT && board[i][2] == NOUGHT
//                    && board[i][3] == NOUGHT)
//                    || (board[i][1] == NOUGHT && board[i][2] == NOUGHT && board[i][3] == NOUGHT
//                    && board[i][4] == NOUGHT)) {
//                return NOUGHT_WON;
//            }
//        }
//
//        for (int j = 0; j < 5; j++) {
//            if ((board[0][j] == CROSS && board[1][j] == CROSS && board[2][j] == CROSS && board[3][j] == CROSS)
//                    || (board[1][j] == CROSS && board[2][j] == CROSS && board[3][j] == CROSS && board[4][j] == CROSS)) {
//                return CROSS_WON;
//            } else if ((board[0][j] == NOUGHT && board[1][j] == NOUGHT && board[2][j] == NOUGHT
//                    && board[3][j] == NOUGHT)
//                    || (board[1][j] == NOUGHT && board[2][j] == NOUGHT && board[3][j] == NOUGHT
//                    && board[4][j] == NOUGHT)) {
//                return NOUGHT_WON;
//            }
//        }
//        return 0;
//    }

    public void initializeSpaces() {
        availableSpaces.clear();
        Collections.addAll(availableSpaces, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24);
    }
}