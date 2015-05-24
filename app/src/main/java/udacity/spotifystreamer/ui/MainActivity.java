package udacity.spotifystreamer.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import udacity.spotifystreamer.R;
import udacity.spotifystreamer.ui.adapter.BindingAdapter;


public class MainActivity extends AppCompatActivity implements
	AdapterView.OnItemClickListener {

	@InjectView(android.R.id.list)
	ListView listView;

	@InjectView(R.id.type_and_search)
	EditText textTypeAndSearch;

	private ProgressDialog mProgressDialog;
	private Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		// Apply IME Action
		textTypeAndSearch.setOnEditorActionListener((example, actionId, event) -> {
			if (actionId == EditorInfo.IME_NULL) {
				searchSpotifyArtists(textTypeAndSearch.getText().toString());
				dismissKeyboard();
			}
			return true;
		});
	}

	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textTypeAndSearch.getWindowToken(), 0);
	}

	private void searchSpotifyArtists(String query){
		SpotifyApi api = new SpotifyApi();
		SpotifyService spotify = api.getService();
		setLoading(true);
		spotify.searchArtists(query, new Callback<ArtistsPager>() {
			@Override
			public void success(ArtistsPager artistsPager, Response response) {
				runOnUiThread(() -> {
					setLoading(false);
					if (artistsPager == null) {
						Toast.makeText(MainActivity.this,
							getString(R.string.fail_response),
							Toast.LENGTH_LONG).show();
					} else if (artistsPager.artists.items.isEmpty()) {
						Toast.makeText(MainActivity.this,
							getString(R.string.fail_no_artists),
							Toast.LENGTH_LONG).show();
					} else {
						createAdapterWithResult(artistsPager.artists);
					}
				});
			}
			@Override
			public void failure(RetrofitError error) {
				runOnUiThread(() -> {
					setLoading(false);
					Toast.makeText(MainActivity.this,
						getString(R.string.fail_loading),
						Toast.LENGTH_LONG).show();
				});
			}
		});
	}
	
	private void createAdapterWithResult(Pager<Artist> artists){
		if(adapter == null){
			adapter = new Adapter(this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		adapter.setRows((ArrayList<Artist>) artists.items);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Artist artist = adapter.getItem(position);
		Intent intent = new Intent(this, ArtistsAlbumsActivity.class);
		intent.putExtra(ArtistsAlbumsActivity.ARTIST_ID, artist.id);
		intent.putExtra(ArtistsAlbumsActivity.ARTIST_NAME, artist.name);
		startActivity(intent);
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

	/**
	 * Main adapter
	 * for this activity
	 */
	public class Adapter extends BindingAdapter<Artist> {

		// Source of items
		private SimpleDateFormat dateFormat;
		private ArrayList<Artist> rows;
		private Context context;

		/**
		 * Constructor
		 * @param context
		 */
		public Adapter(Context context) {
			super(context);
			this.context = context;
			this.rows = new ArrayList<>();
		}

		public void setRows(ArrayList<Artist> rows) {
			this.rows = rows;
		}

		@Override
		public Artist getItem(int position) {
			return this.rows.get(position);
		}

		@Override
		public View newView(LayoutInflater inflater, int position, ViewGroup container) {
			View v = inflater.inflate(R.layout.item_artist, container, false);
			ViewHolder holder = new ViewHolder(v);
			return v;
		}

		@Override
		public void bindView(Artist item, int position, View view) {
			// Option
			Artist artist = this.getItem(position);
			ViewHolder holder = (ViewHolder) view.getTag();
			try {
				Picasso.with(context)
					.load(artist.images.get(0).url)
					.into(holder.artistImage);
			}
			catch(Exception e){
			    e.printStackTrace();
			}
			finally {
				holder.artistName.setText(artist.name);
			}
		}

		@Override
		public int getCount() {
			if(this.rows == null) return 0;
			return this.rows.size();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		/**
		 * View holder we use
		 * to give more style to the
		 * item inside menu.
		 */
		class ViewHolder {

			// UI
			@InjectView(R.id.artist_image)
			ImageView artistImage;

			@InjectView(R.id.artist_name)
			TextView artistName;

			ViewHolder(View v){
				v.setTag(this);
				ButterKnife.inject(this, v);
			}
		}
	}
}
