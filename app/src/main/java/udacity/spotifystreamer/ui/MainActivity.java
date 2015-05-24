package udacity.spotifystreamer.ui;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import udacity.spotifystreamer.R;


public class MainActivity extends AppCompatActivity {

	@InjectView(android.R.id.list)
	ListView listView;

	@InjectView(R.id.type_and_search)
	EditText textTypeAndSearch;

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		// Apply IME Action
		textTypeAndSearch.setOnEditorActionListener((example, actionId, event) -> {
			if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
				searchSpotifyArtists(textTypeAndSearch.getText().toString());
			}
			return true;
		});
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
