package udacity.spotifystreamer.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import udacity.spotifystreamer.R;
import udacity.spotifystreamer.ui.adapter.BindingAdapter;


public class ArtistsAlbumsActivity extends AppCompatActivity implements
	AdapterView.OnItemClickListener {

	public static final String ARTIST_ID = "artistId";
	public static final String ARTIST_NAME = "artistName";

	@InjectView(android.R.id.list)
	ListView listView;

	private ProgressDialog mProgressDialog;
	private Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists_albums);
		ButterKnife.inject(this);
		searchTracksTopTracks(getIntent().getStringExtra(ARTIST_ID));

		try {
			getSupportActionBar().setTitle(getIntent().getStringExtra(ARTIST_NAME));
			getSupportActionBar().setSubtitle(getString(R.string.top_tracks));
		}
		catch(Exception e){
		    e.printStackTrace();
		}
	}

	private void searchTracksTopTracks(String artistId){
		SpotifyApi api = new SpotifyApi();
		SpotifyService spotify = api.getService();
		setLoading(true);
		spotify.getArtistTopTrack(artistId, new Callback<Tracks>() {
			@Override
			public void success(Tracks tracks, Response response) {
				runOnUiThread(() -> {
				setLoading(false);
					if (tracks == null) {
						Toast.makeText(ArtistsAlbumsActivity.this,
							getString(R.string.fail_response),
							Toast.LENGTH_LONG).show();
					}
					else if(tracks.tracks.isEmpty()){
						Toast.makeText(ArtistsAlbumsActivity.this,
							getString(R.string.fail_no_tracks),
							Toast.LENGTH_LONG).show();
					}
					else {
						createAdapterWithResult(tracks.tracks);
					}
				});
			}
			@Override
			public void failure(RetrofitError error) {
				runOnUiThread(() -> {
					setLoading(false);
					Toast.makeText(ArtistsAlbumsActivity.this,
						getString(R.string.fail_loading),
						Toast.LENGTH_LONG).show();
				});
			}
		});
	}

	private void createAdapterWithResult(List<Track> tracks){
		if(adapter == null){
			adapter = new Adapter(this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		adapter.setRows((ArrayList<Track>) tracks);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Track artist = adapter.getItem(position);
		Intent intent = new Intent(this, ArtistsAlbumsActivity.class);
		intent.putExtra(ArtistsAlbumsActivity.ARTIST_ID, artist.id);
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
	public class Adapter extends BindingAdapter<Track> {

		// Source of items
		private SimpleDateFormat dateFormat;
		private ArrayList<Track> rows;
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

		public void setRows(ArrayList<Track> rows) {
			this.rows = rows;
		}

		@Override
		public Track getItem(int position) {
			return this.rows.get(position);
		}

		@Override
		public View newView(LayoutInflater inflater, int position, ViewGroup container) {
			View v = inflater.inflate(R.layout.item_album, container, false);
			ViewHolder holder = new ViewHolder(v);
			return v;
		}

		@Override
		public void bindView(Track item, int position, View view) {
			// Option
			Track track = this.getItem(position);
			ViewHolder holder = (ViewHolder) view.getTag();
			try {
				Picasso.with(context)
					.load(track.album.images.get(0).url)
					.into(holder.albumImage);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
				holder.albumName.setText(track.album.name);
				holder.trackName.setText(track.name);
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
			@InjectView(R.id.album_image)
			ImageView albumImage;

			@InjectView(R.id.album_name)
			TextView albumName;

			@InjectView(R.id.music_name)
			TextView trackName;

			ViewHolder(View v){
				v.setTag(this);
				ButterKnife.inject(this, v);
			}
		}
	}
}
