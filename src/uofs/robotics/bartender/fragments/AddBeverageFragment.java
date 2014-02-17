package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBeverageFragment extends Fragment {

	private EditText editName;
	private Spinner spinnerType;

	public AddBeverageFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_beverage, container,
				false);

		editName = (EditText) view.findViewById(R.id.edit_beverage_name);
		spinnerType = (Spinner) view.findViewById(R.id.spinner_beverage_type);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.beverage_types,
				android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerType.setAdapter(adapter);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
