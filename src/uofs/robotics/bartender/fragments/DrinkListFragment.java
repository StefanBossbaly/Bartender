package uofs.robotics.bartender.fragments;

import java.util.List;

import uofs.robotics.bartender.AddDrinkActivity;
import uofs.robotics.bartender.DrinkInfoActivity;
import uofs.robotics.bartender.R;
import uofs.robotics.bartender.adapters.DrinkAdapter;
import uofs.robotics.bartender.models.Drink;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class DrinkListFragment extends ListFragment {

	private static final String TAG = "DrinkListFragment";

	public static DrinkListFragment newInstance() {
		return new DrinkListFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We have our own option menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.list_loader, container, false);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Query the database
		List<Drink> drinks = Drink.getAll();

		Log.d("Bartender", "Displaying " + drinks.size() + " drink(s)");

		DrinkAdapter adapter = new DrinkAdapter(activity, drinks);
		setListAdapter(adapter);

		Toast.makeText(activity, "onAttach()", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		super.onResume();

		List<Drink> drinks = Drink.getAll();

		DrinkAdapter adapter = new DrinkAdapter(getActivity(), drinks);
		setListAdapter(adapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.drink_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_add_drink) {
			Intent i = new Intent(getActivity(), AddDrinkActivity.class);
			startActivity(i);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Get the selected drink
		Drink drink = ((DrinkAdapter) l.getAdapter()).getItem(position);

		// Declare our paramaters
		Bundle params = new Bundle();
		params.putLong(DrinkInfoActivity.PARAM_DRINK_ID, drink.getId());
		Log.d(TAG, "Putting param drink id into bundle with value of " + drink.getId());

		// Create the intent
		Intent i = new Intent(getActivity(), DrinkInfoActivity.class);
		i.putExtras(params);

		// Off we go!
		getActivity().startActivity(i);
	}
}
