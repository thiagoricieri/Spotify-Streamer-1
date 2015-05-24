package udacity.spotifystreamer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import udacity.spotifystreamer.R;


public class TrackActivity extends AppCompatActivity {

	public static final String TRACK_ID = "id";
	public static final String TRACK_NAME = "name";
	public static final String TRACK_ARTIST = "artist";
	public static final String TRACK_ALBUM = "album";
	public static final String TRACK_SEEK = "seek";
	public static final String TRACK_IMAGE = "image";

	private ProgressDialog mProgressDialog;
	private boolean playing = false;

	@InjectView(R.id.artist_name)
	TextView artistName;

	@InjectView(R.id.music_name)
	TextView musicName;

	@InjectView(R.id.album_name)
	TextView albumName;

	@InjectView(R.id.seek_count)
	TextView seekCount;

	@InjectView(R.id.seek_total)
	TextView seekTotal;

	@InjectView(R.id.album_image)
	ImageView albumImage;

	@InjectView(R.id.seek_bar)
	SeekBar seekBar;

	@InjectView(R.id.btn_play)
	Button btnPlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track);
		ButterKnife.inject(this);

		// values
		try {
			Picasso.with(this)
				.load(getIntent().getStringExtra(TRACK_IMAGE))
				.into(albumImage);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			artistName.setText(getIntent().getStringExtra(TRACK_ARTIST));
			albumName.setText(getIntent().getStringExtra(TRACK_ALBUM));
			musicName.setText(getIntent().getStringExtra(TRACK_NAME));
			seekTotal.setText(getIntent().getStringExtra(TRACK_SEEK));
		}

		// Play
		playTrack();
	}

	@OnClick(R.id.btn_play)
	public void onPlayClick(View v){

	}

	@OnClick(R.id.btn_rewind)
	public void onRewindClick(View v){

	}

	@OnClick(R.id.btn_forward)
	public void onForwardClick(View v){

	}

	private void playTrack() {
		String trackId = getIntent().getStringExtra(TRACK_ID);
	}

	private void setLoading(boolean loading){
		if(loading){
			mProgressDialog = ProgressDialog.show(this, null, getString(R.string.generic_loading));
		}
		else if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
