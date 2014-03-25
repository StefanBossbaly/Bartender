package uofs.robotics.bartender.adapters;

import java.util.List;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Beverage;
import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class IngredientListItem implements OnItemSelectedListener, OnFocusChangeListener {

	private int selected;
	private Spinner beverageSpinner;
	private ArrayAdapter<Beverage> spinnerAdapter;
	private EditText shotAmountEdit;
	private String shotAmount;

	public IngredientListItem() {
		selected = 0;
		beverageSpinner = null;
		spinnerAdapter = null;
		shotAmountEdit = null;
		shotAmount = null;
	}

	public void setupView(Context parent, View view) {
		// Get all of the beverages
		List<Beverage> beverages = Beverage.getAll();

		beverageSpinner = (Spinner) view.findViewById(R.id.spinner_beverage);
		shotAmountEdit = (EditText) view.findViewById(R.id.edit_shot_amount);
		
		// WORKAROUND the focus in list view
		shotAmountEdit.setText(shotAmount);
		shotAmountEdit.setOnFocusChangeListener(this);

		// Set up of adapter
		spinnerAdapter = new ArrayAdapter<Beverage>(parent, android.R.layout.simple_spinner_item, beverages);
		beverageSpinner.setAdapter(spinnerAdapter);
		beverageSpinner.setOnItemSelectedListener(this);
		beverageSpinner.setSelection(selected);
	}

	public Beverage getSelectedBeverage() {
		return spinnerAdapter.getItem(selected);
	}

	public int getShotAmount() {
		return Integer.parseInt(shotAmountEdit.getText().toString());
	}
	
	public void setShotAmount(String amount){
		shotAmount = amount;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
		selected = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}

	// WORKAROUND the focus issue
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			shotAmount = shotAmountEdit.getText().toString();
		}
	}
}
