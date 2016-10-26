package org.bpulse.tictactoetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity implements AdListener {

  public final String[] boardArray = new String[9];
  public int turnCount = 0;
  public int xWins = 0;
  public int oWins = 0;

  private NativeAd nativeAd;

  private GridView board;

  private LinearLayout  nativeAdContainer;
  private LinearLayout adView;
  private AdChoicesView adChoicesView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AdSettings.addTestDevice("32d480622e6b91e5371732f5529cba14");

    final ArrayAdapter<String> arrayAdapter =
            new ArrayAdapter<String>(this, R.layout.tile, boardArray);
    board = (GridView)findViewById(R.id.gvBoard);

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
      setMessage(getTurn() + " wins.");

      if (getTurn() == "X") xWins++;
      if (getTurn() == "O") oWins++;

      setScore(String.format("X has %d points, while O has %d points.", xWins, oWins));

      return true;
    }
    else if (turnCount == 8) {
      setMessage("what a shame, a draw");

      return true;
    }

    return false;
  }

  public void setupBoard() {
    for (int i = 0; i < boardArray.length; i++) {
      boardArray[i] = "";
    }

    turnCount = 0;

    showNativeAd();
  }

  public void setMessage(String message) {
    final TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
    tvMessage.setText(message);

    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  public void setScore(String score) {
    final TextView tvScore = (TextView) findViewById(R.id.tvScore);
    tvScore.setText(score);
  }

  @Override
  public void onError(Ad ad, AdError adError) {

  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad != nativeAd) {
      return;
    }

    // Add ad into the ad container.
    nativeAdContainer = (LinearLayout)findViewById(R.id.native_ad_container);
    LayoutInflater inflater = LayoutInflater.from(this);
    adView = (LinearLayout)inflater.inflate(R.layout.ad_unit,nativeAdContainer, false);
    nativeAdContainer.addView(adView);

    // Create native UI using the ad metadata.
    ImageView nativeAdIcon = (ImageView)adView.findViewById(R.id.native_ad_icon);
    TextView nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
    TextView nativeAdBody = (TextView)adView.findViewById(R.id.native_ad_body);
    MediaView nativeAdMedia = (MediaView)adView.findViewById(R.id.native_ad_media);
    TextView nativeAdSocialContext = (TextView)adView.findViewById(R.id.native_ad_social_context);
    Button nativeAdCallToAction = (Button)adView.findViewById(R.id.native_ad_call_to_action);

    // Setting the Text.
    nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
    nativeAdTitle.setText(nativeAd.getAdTitle());
    nativeAdBody.setText(nativeAd.getAdBody());

    // Downloading and setting the ad icon.
    NativeAd.Image adIcon = nativeAd.getAdIcon();
    NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

    // Download and setting the cover image.
    nativeAdMedia.setNativeAd(nativeAd);

    // Add adChoices icon
    if (adChoicesView == null) {
      adChoicesView = new AdChoicesView(this, nativeAd, true);
      adView.addView(adChoicesView, 0);
    }

    nativeAd.registerViewForInteraction(adView);

    this.board.setVisibility(View.GONE);

    Button nativeAdClose = (Button)adView.findViewById(R.id.native_ad_close);

    nativeAdClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        board.setVisibility(View.VISIBLE);
        ((LinearLayout)findViewById(R.id.native_ad_container)).removeAllViews();
      }
    });
  }

  @Override
  public void onAdClicked(Ad ad) {

  }

  private void showNativeAd() {
    nativeAd = new NativeAd(this, "1507746186203265_1511395212505029");
    nativeAd.setAdListener(this);
    nativeAd.loadAd();
  }
}
