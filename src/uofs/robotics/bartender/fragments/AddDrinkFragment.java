package uofs.robotics.bartender.fragments;

import java.util.HashSet;
import java.util.Set;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.adapters.IngredientAdapter;
import uofs.robotics.bartender.adapters.IngredientListItem;
import uofs.robotics.bartender.models.Beverage;
import uofs.robotics.bartender.models.BeverageDrink;
import uofs.robotics.bartender.models.Drink;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

public class AddDrinkFragment extends Fragment implements OnClickListener {

	private EditText name;
	private ListView ingredientsList;
	private IngredientAdapter adapter;
	private Button addIngredient;
	private Button addDrink;

	public AddDrinkFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_drink, container, false);

		name = (EditText) view.findViewById(R.id.edit_drink_name);
		addDrink = (Button) view.findViewById(R.id.button_add_drink);
		addIngredient = (Button) view.findViewById(R.id.button_add_ingredient);
		ingredientsList = (ListView) view.findViewById(R.id.list_ingredients);

		adapter = new IngredientAdapter(getActivity());
		ingredientsList.setAdapter(adapter);
		ingredientsList.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
				if (position == 1) {
					((ListView) listView).setItemsCanFocus(true);

					// Use afterDescendants, because I don't want the ListView
					// to steal
					// focus
					listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
					name.requestFocus();
				} else {
					if (!listView.isFocused()) {
						((ListView) listView).setItemsCanFocus(false);

						// Use beforeDescendants so that the EditText doesn't
						// re-take
						// focus
						listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
						listView.requestFocus();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		addDrink.setOnClickListener(this);
		addIngredient.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_add_drink) {

			// Start the transaction
			ActiveAndroid.beginTransaction();

			try {
				// Save the drink
				Drink drink = new Drink();
				drink.setName(name.getText().toString());
				drink.save();

				// Make sure there are no dubs
				Set<Beverage> beverages = new HashSet<Beverage>();

				// Go thru each element in the list
				for (int i = 0; i < adapter.getCount(); i++) {
					IngredientListItem item = adapter.getItem(i);

					Beverage beverage = item.getSelectedBeverage();
					int shots = item.getShotAmount();

					if (beverages.contains(beverage)) {

					} else {
						beverages.add(beverage);
					}

					BeverageDrink ingredient = new BeverageDrink(beverage, drink, shots);
					ingredient.save();
				}

				ActiveAndroid.setTransactionSuccessful();
			} finally {
				ActiveAndroid.endTransaction();
			}

			// We are done here
			getActivity().finish();

		} else if (view.getId() == R.id.button_add_ingredient) {
			IngredientListItem item = new IngredientListItem();
			adapter.add(item);
			adapter.notifyDataSetChanged();
		}
	}
}
