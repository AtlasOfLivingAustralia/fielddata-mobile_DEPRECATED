package au.org.ala.fielddata.mobile.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import au.org.ala.fielddata.mobile.R;
import au.org.ala.fielddata.mobile.model.Record;
import au.org.ala.fielddata.mobile.service.StorageManager;

/**
 * Caches references to the UI components that display the details 
 * of a single Record.
 * Used to prevent continual calls to findViewById during View recycling
 * while the list is scrolling. 
 */
public class SavedRecordHolder {

	ImageView icon = null;
	TextView recordSpecies = null;
	TextView recordTime = null;
	private StorageManager storageManager;
	
	public SavedRecordHolder(View row) {
		recordSpecies = (TextView)row.findViewById(R.id.record_description_species);
		recordTime = (TextView)row.findViewById(R.id.record_description_time);
		
		icon = (ImageView)row.findViewById(R.id.record_image);
		
		storageManager = new StorageManager(row.getContext());
	}
	
	public void populate(Record record) {
		Uri image = record.getFirstImageUri();
		if (image != null) {
		
			int w = icon.getLayoutParams().width;
			int h = icon.getLayoutParams().height;
			if (w > 0 && h > 0) {
				Bitmap bitMap = storageManager.bitmapFromUri(image, w, h);
				icon.setImageBitmap(bitMap);
			}
		}
		else {
			icon.setImageBitmap(null);
		}
		if (record.taxon_id != null && record.taxon_id > 0) {
			recordSpecies.setText(record.scientificName);
		}
		else {
			recordSpecies.setText("No species recorded");
		}
		Date created = new Date(record.when);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		recordTime.setText(format.format(created));
		
		
	}
	
}