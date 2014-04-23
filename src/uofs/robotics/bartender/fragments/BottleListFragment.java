package uofs.robotics.bartender.fragments;

import java.util.List;

import uofs.robotics.bartender.AddBottleActivity;
import uofs.robotics.bartender.adapters.BottleAdapter;
import uofs.robotics.bartender.adapters.DrinkAdapter;
import uofs.robotics.bartender.models.Beverage;
import uofs.robotics.bartender.models.Bottle;
import uofs.robotics.bartender.models.Drink;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class BottleListFragment extends ListFragment {

	public static BottleListFragment newInstance() {
		return new BottleListFragment();
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

		BottleAdapter adapter = new BottleAdapter(activity, Bottle.getAll());

		setListAdapter(adapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add code
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		// Get the bottles
		List<Bottle> bottles = Bottle.getAll();

		// Create a new adapter
		BottleAdapter adapter = new BottleAdapter(getActivity(), bottles);
		setListAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Bottle bottle = (Bottle) l.getAdapter().getItem(position);

		if (bottle == null) {
			Intent i = new Intent(getActivity(), AddBottleActivity.class);
			i.putExtra(AddBottleActivity.PARAM_SLOT, position);
			startActivity(i);
		} else {
			Intent i = new Intent(getActivity(), AddBottleActivity.class);
			i.putExtra(AddBottleActivity.PARAM_BOTTLE_ID, bottle.getId());
			startActivity(i);
		}
	}
}
