package org.bpulse.tictactoetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

  public final String[] boardArray = new String[9];
  public int turnCount = 0;

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
        if (boardArray[position] == "") {
          boardArray[position] = getTurn();
          arrayAdapter.notifyDataSetChanged();

          Boolean needsRestart = checkState();
          if (needsRestart) {
            setupBoard();
          }
          else {
            turnCount++;
            Log.i("turn count", String.valueOf(turnCount));
          }
        }
        else {
          Log.i("illegal move", "there is already a piece at that position");
        }
      }
    });

    setupBoard();
  }

  public String getTurn() {
    if (turnCount % 2 == 0) {
      return "X";
    }
    else {
      return "O";
    }
  }

  public Boolean checkState() {
    if ((boardArray[0] != "" && boardArray[0] == boardArray[1] && boardArray[0] == boardArray[2]) || // top
        (boardArray[3] != "" && boardArray[3] == boardArray[4] && boardArray[3] == boardArray[5]) || // middle
        (boardArray[6] != "" && boardArray[6] == boardArray[7] && boardArray[6] == boardArray[8]) || // bottom
        (boardArray[0] != "" && boardArray[0] == boardArray[3] && boardArray[0] == boardArray[6]) || // left
        (boardArray[1] != "" && boardArray[1] == boardArray[4] && boardArray[1] == boardArray[7]) || // middle
        (boardArray[2] != "" && boardArray[2] == boardArray[5] && boardArray[2] == boardArray[8]) || // right
        (boardArray[0] != "" && boardArray[0] == boardArray[4] && boardArray[0] == boardArray[8]) || // down diagonal
        (boardArray[6] != "" && boardArray[6] == boardArray[4] && boardArray[6] == boardArray[2]) // up diagonal
     ) {
      Log.i("winner", getTurn() + " wins.");

      return true;
    }
    else if (turnCount == 8) {
      Log.i("draw", "what a shame, a draw");

      return true;
    }

    return false;
  }

  public void setupBoard() {
    for (int i = 0; i < boardArray.length; i++) {
      boardArray[i] = "";
    }

    turnCount = 0;
  }
}
