package edu.quinnipiac.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CharacterselectionActivity extends AppCompatActivity {
    public static final String PLAYER_NAME = "player";
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characterselection);
        Intent intent = getIntent();
        playerName = (String) intent.getStringExtra("name");
    }

    public void goToGame(View view){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedCharacter = radioGroup.getCheckedRadioButtonId();
        if (selectedCharacter == R.id.radio_X) {
            selectedCharacter = 1;
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("name",playerName);
            intent.putExtra("playerCharacter",selectedCharacter);
            startActivity(intent);
        } else if (selectedCharacter == R.id.radio_O) {
            selectedCharacter = 2;
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("name",playerName);
            intent.putExtra("playerCharacter",selectedCharacter);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"Please choose a character to play as...",Toast.LENGTH_SHORT).show();
        }
    }




}