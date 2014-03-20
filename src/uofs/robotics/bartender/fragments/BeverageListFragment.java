package uofs.robotics.bartender.fragments;

import java.util.List;

import uofs.robotics.bartender.AddBeverageActivity;
import uofs.robotics.bartender.R;
import uofs.robotics.bartender.adapters.BeverageAdapter;
import uofs.robotics.bartender.models.Beverage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BeverageListFragment extends ListFragment {

	public BeverageListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We have our own option menu
		setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		BeverageAdapter adapter = new BeverageAdapter(activity, Beverage.getAll());
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();

		List<Beverage> beverages = Beverage.getAll();

		BeverageAdapter adapter = new BeverageAdapter(getActivity(), beverages);
		setListAdapter(adapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.beverage_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_add_beverage) {
			Intent i = new Intent(getActivity(), AddBeverageActivity.class);
			startActivity(i);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
