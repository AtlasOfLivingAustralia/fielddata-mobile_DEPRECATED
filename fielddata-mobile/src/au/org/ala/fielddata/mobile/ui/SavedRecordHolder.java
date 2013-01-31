package au.org.ala.fielddata.mobile.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import au.org.ala.fielddata.mobile.R;
import au.org.ala.fielddata.mobile.model.Record;
import au.org.ala.fielddata.mobile.model.Survey;
import au.org.ala.fielddata.mobile.service.StorageManager;

/**
 * Caches references to the UI components that display the details 
 * of a single Record.
 * Used to prevent continual calls to findViewById during View recycling
 * while the list is scrolling. 
 */
public class SavedRecordHolder {

	
	public static class RecordView {
		
		public RecordView(Record record, Survey survey) {
			this.record = record;
			this.survey = survey;
		}
		
		public Record record;
		public Survey survey;
	}
	
	TextView statusLabel = null;
	ImageView icon = null;
	TextView recordSpecies = null;
	TextView recordTime = null;
	TextView surveyName = null;
	public CheckBox checkbox = null;
	
	private StorageManager storageManager;
	
	public SavedRecordHolder(View row) {
		recordSpecies = (TextView)row.findViewById(R.id.record_description_species);
		recordTime = (TextView)row.findViewById(R.id.record_description_time);
		icon = (ImageView)row.findViewById(R.id.record_image);
		checkbox = (CheckBox)row.findViewById(R.id.checkbox);
		surveyName = (TextView)row.findViewById(R.id.survey_name);
		storageManager = new StorageManager(row.getContext());
		statusLabel = (TextView)row.findViewById(R.id.status);
	}
	
	public void populate(RecordView recordView) {
		
		Record record = recordView.record;
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
		surveyName.setText(recordView.survey.name);
		Date created = new Date(record.when);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		recordTime.setText(format.format(created));
		Record.Status status = record.getStatus();
		if (status != Record.Status.COMPLETE) {
			if (status == Record.Status.COMPLETE) {
				statusLabel.setVisibility(View.GONE);
			}
			else {
				statusLabel.setVisibility(View.VISIBLE);
				switch (status) {
				case SCHEDULED_FOR_UPLOAD:
					statusLabel.setText("Upload pending");
					statusLabel.setBackgroundColor(Color.parseColor("#32cd32"));
				    statusLabel.setTextColor(Color.parseColor("#000000"));
					break;
				case DRAFT:
					statusLabel.setText("Draft");
					statusLabel.setBackgroundColor(Color.parseColor("#FFFFCC"));
				    statusLabel.setTextColor(Color.parseColor("#555555"));
					break;
				case UPLOADING:
					statusLabel.setText("Uploading...");
					statusLabel.setBackgroundColor(Color.parseColor("#32cd32"));
				    statusLabel.setTextColor(Color.parseColor("#000000"));
					break;
				case FAILED_TO_UPLOAD:
					statusLabel.setText("Upload Failed");
					statusLabel.setBackgroundColor(Color.parseColor("#FF0000"));
				    statusLabel.setTextColor(Color.parseColor("#000000"));
					break;
				}
			}
		}
		
	}
	
	
	
}