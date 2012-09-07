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
package au.org.ala.fielddata.mobile.validation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import au.org.ala.fielddata.mobile.model.Attribute;
import au.org.ala.fielddata.mobile.model.SurveyViewModel;
import au.org.ala.fielddata.mobile.ui.MultiSpinner;
import au.org.ala.fielddata.mobile.ui.MultiSpinner.MultiSpinnerListener;
import au.org.ala.fielddata.mobile.validation.Validator.ValidationResult;

public class MultiSpinnerBinder implements Binder, MultiSpinnerListener {

	private MultiSpinner view;
	private Attribute attribute;
	private SurveyViewModel model;
	private Context ctx;
	private boolean updating;
	private List<String> items;
	

	public MultiSpinnerBinder(Context ctx, MultiSpinner view, Attribute attribute, SurveyViewModel model) {
		this.ctx = ctx;
		this.view = view;
		this.attribute = attribute;
		this.model = model;
		updating = false;
		
		String value = model.getValue(attribute);
		
		items = new ArrayList<String>();
		boolean[] selected =  new boolean[attribute.options.length];
		for (int i=0; i < attribute.options.length; i++) {
			items.add(attribute.options[i].value);
			if (value.contains(attribute.options[i].value)) {
				selected[i] = true;
			}
		}
		view.setItems(items, value, this);
		
		view.setSelected(selected);
	}

	public void onItemsSelected(boolean[] selected) {
		StringBuffer selectedItems = new StringBuffer();
		for (int i=0; i<selected.length; i++) {
			if (selected[i]) {
				selectedItems.append(items.get(i) + ", ");
			}
		}
		bind(selectedItems.toString().substring(0,selectedItems.length()-2));
	}
	
		
	public void onAttributeChange(Attribute attribute) {
		if (attribute.getServerId() != this.attribute.getServerId()) {
			return;
		}
		try {
			updating = true;
			bind();
		}
		finally {
			updating = false;
		}
	}

	public void onValidationStatusChange(Attribute attribute, ValidationResult result) {
		if (attribute.getServerId() != this.attribute.getServerId()) {
			return;
		}
		
		View selected = view.getSelectedView();
		if (selected instanceof TextView) {
			TextView textView = (TextView)selected;
			if (result.isValid()) {
				textView.setError(null);
			}
			else {
			    textView.setError(result.getMessage(ctx));
			}
		}
	}
	
	public void bind() {
		if (!updating) {
			bind(nullSafeText());
		}
	}
	
	private void bind(String value) {
		model.setValue(attribute, value);
	}

	private String nullSafeText() {
		
		boolean[] selected = view.getSelected();
		StringBuffer selectedItems = new StringBuffer();
		for (int i=0; i<selected.length; i++) {
			if (selected[i]) {
				selectedItems.append(items.get(i) + ", ");
			}
		}
		String result = selectedItems.length() > 0 ? selectedItems.toString().substring(0,selectedItems.length()-2) : "";
		return result;
	}

}
