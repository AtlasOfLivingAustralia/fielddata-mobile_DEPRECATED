package au.org.ala.fielddata.mobile;

import android.os.Bundle;
import android.util.Log;
import au.org.ala.fielddata.mobile.dao.GenericDAO;
import au.org.ala.fielddata.mobile.model.Attribute;
import au.org.ala.fielddata.mobile.model.Record;
import au.org.ala.fielddata.mobile.model.Survey;
import au.org.ala.fielddata.mobile.model.SurveyViewModel;
import au.org.ala.fielddata.mobile.model.SurveyViewModel.TempValue;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;


/**
 * Makes use of the behaviour that fragments can survive the re-creation
 * of their containing activity to keep a reference to the view model
 * alive during configuration changes.
 */
public class SurveyModelHolder extends SherlockFragment {

	private SurveyViewModel model;
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		int surveyId = getActivity().getIntent().getIntExtra(CollectSurveyData.SURVEY_BUNDLE_KEY, 0);
		int recordId = getActivity().getIntent().getIntExtra(CollectSurveyData.RECORD_BUNDLE_KEY, 0);
		Log.d("SurveyModelHolder", "Found survey "+surveyId+" in intent");
		Log.d("SurveyModelHolder", "Found record "+recordId+" in intent");
		
		
		if (savedInstanceState != null) {
			surveyId = savedInstanceState.getInt(CollectSurveyData.SURVEY_BUNDLE_KEY, surveyId);
			recordId = savedInstanceState.getInt(CollectSurveyData.RECORD_BUNDLE_KEY, recordId);
			
			Log.d("SurveyModelHolder", "Found survey "+surveyId+" in bundle");
			Log.d("SurveyModelHolder", "Found record "+recordId+" in bundle");
			
		}
		
		setRetainInstance(true);
		updateModel(surveyId, recordId);
		
		// Now restore the default value if required
		if (savedInstanceState != null) {
			int attributeId = savedInstanceState.getInt("TempAttribute", -1);
			if (attributeId > 0) {
				Attribute attr = model.getSurvey().getAttribute(attributeId);
				String value = savedInstanceState.getString("TempAttributeValue");
				model.setTempValue(attr, value);
			}
		}
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		GenericDAO<Record> recordDao = new GenericDAO<Record>(getActivity());
		recordDao.save(model.getRecord());
		Log.i("SurveyModelHolder", this+"Saving survey: "+model.getSurvey().server_id);
		Log.i("SurveyModelHolder", this+"Saving record: "+model.getRecord().getId());
		
		outState.putInt(CollectSurveyData.SURVEY_BUNDLE_KEY, model.getSurvey().server_id);
		outState.putInt(CollectSurveyData.RECORD_BUNDLE_KEY, model.getRecord().getId());
		TempValue toSave = model.getTempValue();
		if (toSave != null) {
			outState.putInt("TempAttribute", toSave.getAttribute().server_id);
			outState.putString("TempAttributeValue", toSave.getValue());
		}
	}



	private synchronized void updateModel(int surveyId, int recordId) {
		if (model == null) {
			Record record = null;
			Survey survey;
			if (recordId > 0) {
				record = initRecord(recordId, surveyId);
				surveyId = record.survey_id;
			}
			try {
				survey = initSurvey(surveyId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (recordId <= 0) {
				record = initRecord(recordId, surveyId);
			}
			model = new SurveyViewModel(survey, record,
					getActivity().getPackageManager());

		}
		((CollectSurveyData)getActivity()).setViewModel(model);
	}
	
	private Survey initSurvey(int surveyId) throws Exception {
		GenericDAO<Survey> surveyDAO = new GenericDAO<Survey>(getActivity().getApplicationContext());
		return surveyDAO.findByServerId(Survey.class, surveyId);
	}
	
	
	private Record initRecord(int recordId, int surveyId) {
		Record record;
		if (recordId <= 0) {
			record = new Record();
			record.survey_id = surveyId;
			record.when = System.currentTimeMillis();
			Log.d("SurveyModelHolder", "Creating new record for survey: "+surveyId);

		} else {
			GenericDAO<Record> recordDAO = new GenericDAO<Record>(getActivity().getApplicationContext());
			record = recordDAO.load(Record.class, recordId);
			Log.d("SurveyModelHolder", "Loaded record with id: "+recordId+", record="+new Gson().toJson(record));
		}
		return record;
	}

	
}
