package uofs.robotics.bartender;

import uofs.robotics.bartender.fragments.BeverageListFragment;
import uofs.robotics.bartender.fragments.BottleListFragment;
import uofs.robotics.bartender.fragments.DrinkListFragment;
import uofs.robotics.bartender.services.BluetoothService;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static final String TAG = "MainActivity";

	private static BluetoothService bluetoothService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get the bluetooth service
		bluetoothService = ((BartenderApplication) getApplication()).getBluetoothService();

		showTabNav();
	}

	private void showTabNav() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[] { getString(R.string.title_drinks),
						getString(R.string.title_bottles), getString(R.string.title_beverages), }), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_bluetooth_device) {
			Intent i = new Intent(this, BluetoothDeviceListActivity.class);
			startActivity(i);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.

		if (position == 0) {
			Fragment fragment = new DrinkListFragment();

			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

			return true;
		} else if (position == 1) {
			Fragment fragment = new BottleListFragment();

			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

			return true;
		} else if (position == 2) {
			Fragment fragment = new BeverageListFragment();

			getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

			return true;
		} else {
			return false;
		}
	}
}
