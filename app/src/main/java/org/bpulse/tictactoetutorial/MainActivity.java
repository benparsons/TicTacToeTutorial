package org.bpulse.tictactoetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boardArray[position] = "X";
        arrayAdapter.notifyDataSetChanged();
      }
    });

    for (int i = 0; i < boardArray.length; i++) {
      boardArray[i] = "";
    }
  }
}
