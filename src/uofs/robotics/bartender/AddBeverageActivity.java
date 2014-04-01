package uofs.robotics.bartender;

import uofs.robotics.bartender.fragments.AddBeverageFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class AddBeverageActivity extends FragmentActivity {

	public static final String PARAM_BEVERAGE_ID = "beverage_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_beverage);

		// Get the passed parameters
		Bundle args = getIntent().getExtras();

		if (args != null && args.containsKey(PARAM_BEVERAGE_ID)) {
			long id = args.getLong(PARAM_BEVERAGE_ID);
			Fragment fragment = AddBeverageFragment.newInstance(id);
			getSupportFragmentManager().beginTransaction().add(R.id.main_view, fragment).commit();
		} else {
			Fragment fragment = AddBeverageFragment.newInstance();
			getSupportFragmentManager().beginTransaction().add(R.id.main_view, fragment).commit();
		}
	}
}
