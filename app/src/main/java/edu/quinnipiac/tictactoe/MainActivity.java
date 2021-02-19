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

    public void startGame(View view){
        EditText name = (EditText) findViewById(R.id.editName);
        String nameString = name.getText().toString();
        if (nameString.equals("")) {
            Toast.makeText(getApplicationContext(),"ENTER YOUR NAME PLEASE",Toast.LENGTH_SHORT).show();
        } else {
            Intent nextScreen = new Intent(this, CharacterselectionActivity.class);
            nextScreen.putExtra("name",nameString);
            startActivity(nextScreen);
        }
    }
}