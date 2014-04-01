package uofs.robotics.bartender;

import uofs.robotics.bartender.fragments.DrinkInfoFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class DrinkInfoActivity extends FragmentActivity {

	public static final String PARAM_DRINK_ID = "drink_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drink_info);

		// Get the passed parameters
		Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();

		if (args != null && args.containsKey(PARAM_DRINK_ID)) {
			long id = args.getLong(PARAM_DRINK_ID);
			Fragment fragment = DrinkInfoFragment.newInstance(id);
			manager.beginTransaction().add(R.id.main_view, fragment).commit();
		} else {
			Fragment fragment = DrinkInfoFragment.newInstance();
			manager.beginTransaction().add(R.id.main_view, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drink_info, menu);
		return true;
	}

}
