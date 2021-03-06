/*******************************************************************************
 * Copyright (C) 2010 Atlas of Living Australia
 * All Rights Reserved.
 *  
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *  
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.fielddata.mobile.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ListView;

import au.org.ala.fielddata.mobile.Reloadable;
import au.org.ala.fielddata.mobile.dao.SpeciesDAO;
import au.org.ala.fielddata.mobile.model.Species;
import au.org.ala.fielddata.mobile.nrmplus.R;

import com.actionbarsherlock.app.SherlockListFragment;
import com.commonsware.cwac.loaderex.acl.AbstractCursorLoader;

public class SpeciesListFragment extends SherlockListFragment implements LoaderCallbacks<Cursor>, Reloadable {

    public static final String QUERY_ARG = "query";
    public String query;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Activity activity = getSherlockActivity();
		if (activity instanceof SpeciesSelectionListener) {
			Species species = (Species)l.getAdapter().getItem(position);
			((SpeciesSelectionListener)activity).onSpeciesSelected(species);
		}
	}
	
    protected void init() {
    	if (getActivity() != null) {
    		
    		
    		SpeciesAdapter adapter = new SpeciesAdapter(getActivity(),  R.layout.species_list_row, null);
    		setListAdapter(adapter);
    		LoaderManager manager = getActivity().getSupportLoaderManager();
    		manager.initLoader(0, null, this);
    		
    	}
    }


    public void reload() {
        if (getActivity() != null) {
            LoaderManager manager = getActivity().getSupportLoaderManager();
            manager.restartLoader(0, null, this);
        }
    }

    public void clearSearch() {
        this.query = null;
        reload();
    }

    public void speciesSearch(String query) {
        this.query = query;
        if (getActivity() != null) {
            LoaderManager manager = getActivity().getSupportLoaderManager();
            Bundle args = new Bundle();
            args.putString(QUERY_ARG, query);
            manager.restartLoader(0, args, this);
        }
    }
    
    @Override
	public void onResume() {
		super.onResume();

	}

	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        String query = null;
        if (args != null) {
            query = args.getString(QUERY_ARG);
        }
		return new SpeciesLoader(getActivity(), query);
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        ((CursorAdapter)getListAdapter()).swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
        ((CursorAdapter)getListAdapter()).swapCursor(null);
		
	}
    
    static class SpeciesLoader extends AbstractCursorLoader {

    	private SpeciesDAO speciesDAO;
        private String query;
    	public SpeciesLoader(Context ctx, String query) {
    		super(ctx);
    		speciesDAO = new SpeciesDAO(ctx);
            this.query = query;
    	}
		@Override
		protected Cursor buildCursor() {
            if (query != null) {
                return speciesDAO.searchSpecies(query);
            }
			return speciesDAO.loadSpecies();
		}
    	
    }
    
    public static class SpeciesAdapter extends ResourceCursorAdapter {

    	private Context ctx;

		public SpeciesAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 0);	
			ctx = context;
		}

		@Override
		public void bindView(View row, Context arg1, Cursor cursor) {
			SpeciesViewHolder viewHolder = (SpeciesViewHolder) row.getTag();
			if (viewHolder == null) {
				viewHolder = new SpeciesViewHolder(row);
				row.setTag(viewHolder);
			}

            String previousGroup = null;
            int position = cursor.getPosition();
            if (position > 0) {
                cursor.moveToPrevious();
                previousGroup = cursor.getString(cursor.getColumnIndex(SpeciesDAO.SPECIES_GROUP_NAME_COLUMN_NAME));
                cursor.moveToPosition(position);
            }

			viewHolder.populate(
					cursor.getString(cursor.getColumnIndex(SpeciesDAO.SCIENTIFIC_NAME_COLUMN_NAME)), 
					cursor.getString(cursor.getColumnIndex(SpeciesDAO.COMMON_NAME_COLUMN_NAME)),
					cursor.getString(cursor.getColumnIndex(SpeciesDAO.IMAGE_URL_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(SpeciesDAO.SPECIES_GROUP_NAME_COLUMN_NAME)),
                    previousGroup);

			
		}
		
		public Species getSpecies(int position) {
			Cursor cursor = (Cursor)super.getItem(position);
			return new SpeciesDAO(ctx).load(Species.class, cursor.getInt(0));
		}
    	
    }
	
	
}
