package uofs.robotics.bartender;

import uofs.robotics.bartender.fragments.AddBottleFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class AddBottleActivity extends FragmentActivity {

	public static final String PARAM_SLOT = "slot";
	public static final String PARAM_BOTTLE_ID = "bottle_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bottle);

		// Get the passed parameters
		Bundle args = getIntent().getExtras();

		if (args != null && args.containsKey(PARAM_BOTTLE_ID)) {
			long id = args.getLong(PARAM_BOTTLE_ID);
			Fragment fragment = AddBottleFragment.newInstance(id);
			getSupportFragmentManager().beginTransaction().add(R.id.main_view, fragment).commit();
		} else if (args != null && args.containsKey(PARAM_SLOT)) {
			int slot = args.getInt(PARAM_SLOT);
			Fragment fragment = AddBottleFragment.newInstance(slot);
			getSupportFragmentManager().beginTransaction().add(R.id.main_view, fragment).commit();
		} else {
			throw new RuntimeException("AddBottleActivity has no parameters");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_bottle, menu);
		return true;
	}

}
