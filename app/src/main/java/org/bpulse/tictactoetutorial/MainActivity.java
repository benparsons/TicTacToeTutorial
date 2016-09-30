package org.bpulse.tictactoetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

  public final String[] boardArray = new String[9];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ArrayAdapter<String> arrayAdapter =
            new ArrayAdapter<String>(this, R.layout.tile, boardArray);
    final GridView board = (GridView)findViewById(R.id.gvBoard);

    board.setAdapter(arrayAdapter);

    for (int i = 0; i < boardArray.length; i++) {
      boardArray[i] = "X";
    }
  }
}
