package au.org.ala.fielddata.mobile;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.org.ala.fielddata.mobile.ui.MenuHelper;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class LocationSelectionActivity extends SherlockMapActivity implements
		LocationListener {

	private LocationManager locationManager;
	private MapView mapView;
	private MyLocationOverlay overlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_selection);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(7);
		mapView.getController().setCenter(new GeoPoint(-27561777, 151493591));

		addEventHandlers();

		// getCurrentLocation();
	}

	private Location getCurrentLocation() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAltitudeRequired(false);
		criteria.setAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setCostAllowed(false);

		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		locationManager.requestLocationUpdates(provider, 0, 0, this);

		return location;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		new MenuInflater(this).inflate(R.menu.common_menu_items, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return new MenuHelper(this).handleMenuItemSelection(item);
	}

	private void addEventHandlers() {
		Button button = (Button) findViewById(R.id.mapNext);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				Species species = (Species) getIntent().getExtras().get(
//						CollectSurveyData.SPECIES_BUNDLE_KEY);
//				Intent intent = new Intent(LocationSelectionActivity.this,
//						CollectSurveyData.class);
//				intent.putExtra(CollectSurveyData.SPECIES_BUNDLE_KEY, species);
//				startActivity(intent);
				finish();

			}
		});
		button.setEnabled(false);

		button = (Button) findViewById(R.id.mapCurrentLocation);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				updateLocation();
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	public void onLocationChanged(Location location) {
		// updateLocation(location);

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	private void updateLocation() {
		final MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
		overlay.enableMyLocation();
		mapView.getOverlays().add(overlay);
		
		overlay.runOnFirstFix(new Runnable() {
			
			public void run() {
				GeoPoint point = overlay.getMyLocation();
				if (point != null) {
					mapView.getController().setCenter(point);
					mapView.getController().setZoom(18);
					runOnUiThread(new Runnable() {
						public void run() {
							Button button = (Button) findViewById(R.id.mapNext);
							button.setEnabled(true);
						}
					});
					
				}
			}
		});
		
		
	}

}