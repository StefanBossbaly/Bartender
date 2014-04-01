package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Beverage;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBeverageFragment extends Fragment implements OnClickListener {

	private EditText editName;
	private Spinner spinnerType;
	private Button buttonAdd;
	private long beverageID;

	private static final String BEVERAGE_ID = "beverage_id";

	public static AddBeverageFragment newInstance() {
		return new AddBeverageFragment();
	}

	public static AddBeverageFragment newInstance(long id) {
		AddBeverageFragment fragment = new AddBeverageFragment();

		// Put in the arguments
		Bundle args = new Bundle();
		args.putLong(BEVERAGE_ID, id);
		fragment.setArguments(args);

		return fragment;
	}
	
	@Override
	@SuppressLint("DefaultLocale")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_beverage, container, false);

		// Get the XML objects
		editName = (EditText) view.findViewById(R.id.edit_beverage_name);
		spinnerType = (Spinner) view.findViewById(R.id.spinner_beverage_type);
		buttonAdd = (Button) view.findViewById(R.id.button_add_beverage);

		// Set up the spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.beverage_types, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerType.setAdapter(adapter);

		// Set up the button
		buttonAdd.setOnClickListener(this);

		// See if we are editing
		Bundle args = getArguments();

		if (args != null && args.containsKey(BEVERAGE_ID)) {
			beverageID = args.getLong(BEVERAGE_ID);
			Beverage beverage = Beverage.getById(beverageID);

			editName.setText(beverage.getName());
			for (int i = 0; i < spinnerType.getAdapter().getCount(); i++) {
				String current = spinnerType.getAdapter().getItem(i).toString();

				if (current.toLowerCase().equals(beverage.getType().toLowerCase())) {
					spinnerType.setSelection(i);
					break;
				}
			}

			buttonAdd.setText(R.string.button_update_beverage);
		} else {
			beverageID = -1;
		}

		return view;
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.button_add_beverage) {
			// Get the values out of the widgets
			String name = editName.getText().toString();
			String type = spinnerType.getSelectedItem().toString();

			// Create the beverage and save it
			if (beverageID == -1) {
				Beverage beverage = new Beverage(name, type);
				beverage.save();
				Log.d("Bartender", "Beverage added with an id of " + beverage.getId());
			} else {
				Beverage beverage = Beverage.getById(beverageID);
				beverage.setName(name);
				beverage.setType(type);
				beverage.save();
				Log.d("Bartender", "Beverage updated with an id of " + beverage.getId());
			}

			// We are done here
			getActivity().finish();
		}
	}
}
