package au.org.ala.fielddata.mobile.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import au.org.ala.fielddata.mobile.R;
import au.org.ala.fielddata.mobile.dao.GenericDAO;
import au.org.ala.fielddata.mobile.model.Species;

/**
 * A list adapter for displaying lists of Species.
 */
public class SpeciesListAdapter extends ArrayAdapter<Species> {
	
	public SpeciesListAdapter(Context ctx) {
		this(ctx, -1);
	}
	
	public SpeciesListAdapter(Context ctx, int excludedGroupId) {
		super(ctx, R.layout.species_row, R.id.scientificName,
				new ArrayList<Species>());
		GenericDAO<Species> speciesDao = new GenericDAO<Species>(ctx);
		new GetSpeciesTask(speciesDao, this, excludedGroupId).execute();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = super.getView(position, convertView, parent);
		SpeciesViewHolder viewHolder = (SpeciesViewHolder) row.getTag();
		if (viewHolder == null) {
			viewHolder = new SpeciesViewHolder(row);
			row.setTag(viewHolder);
		}

		viewHolder.populate(getItem(position));
		return row;
	}

	static class GetSpeciesTask extends AsyncTask<Void, Void, List<Species>> {

		private GenericDAO<Species> dao;
		private SpeciesListAdapter adapter;
		private int excludedGroupId;

		public GetSpeciesTask(GenericDAO<Species> speciesDao,
				SpeciesListAdapter adapter, int excludedGroupId) {
			this.dao = speciesDao;
			this.adapter = adapter;
			this.excludedGroupId = excludedGroupId;
		}

		protected List<Species> doInBackground(Void... ignored) {

			List<Species> species = new ArrayList<Species>();
			try {
				List<Species> allSpecies = dao.loadAll(Species.class);
				for (Species taxon : allSpecies) {
					if (taxon.getTaxonGroupId()
							!= excludedGroupId) {
						species.add(taxon);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return species;
		}

		protected void onPostExecute(List<Species> speciesList) {
			adapter.setNotifyOnChange(false);
			for (Species species : speciesList) {
				adapter.add(species);
			}
			adapter.notifyDataSetChanged();
		}
	}

}