// Author: Max Petruzziello
// SER210 Spring 2021
// Assignment 1 Part 2
package edu.quinnipiac.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // retrieve and store player name input into edit text
    public void startGame(View view){
        EditText name = (EditText) findViewById(R.id.editName);
        String nameString = name.getText().toString();

        // if player has not input their name they cannot continue to the game
        if (nameString.equals("")) {
            Toast.makeText(getApplicationContext(),"Enter Your Name to Continue!",Toast.LENGTH_SHORT).show();
        } else {
            Intent nextScreen = new Intent(this, CharacterselectionActivity.class);
            nextScreen.putExtra("name",nameString);
            startActivity(nextScreen);
        }
    }
}