package udacity.spotifystreamer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import udacity.spotifystreamer.R;


public class ArtistsAlbumsActivity extends AppCompatActivity {

	@InjectView(android.R.id.list)
	ListView listView;

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists_albums);
		ButterKnife.inject(this);
	}

	private void searchSpotifyArtists(String query){
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
