package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Beverage;
import uofs.robotics.bartender.models.Bottle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddBottleFragment extends Fragment implements OnClickListener {

	private static final String BOTTLE_ID = "bottle_id";
	private static final String SLOT = "slot";

	private Button saveButton;
	private TextView slotText;
	private Spinner beverageSpinner;

	private int slot = -1;
	private long bottleID = -1;

	public static AddBottleFragment newInstance(int slot) {
		AddBottleFragment fragment = new AddBottleFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(SLOT, slot);
		fragment.setArguments(bundle);

		return fragment;
	}

	public static AddBottleFragment newInstance(long id) {
		AddBottleFragment fragment = new AddBottleFragment();

		Bundle bundle = new Bundle();
		bundle.putLong(BOTTLE_ID, id);
		fragment.setArguments(bundle);

		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_bottle, container, false);

		slotText = (TextView) view.findViewById(R.id.text_slot);

		saveButton = (Button) view.findViewById(R.id.button_save);
		saveButton.setOnClickListener(this);

		beverageSpinner = (Spinner) view.findViewById(R.id.spinner_beverage);

		// Set up the spinner
		ArrayAdapter<Beverage> adapter = new ArrayAdapter<Beverage>(getActivity(), android.R.layout.simple_spinner_item, Beverage.getAll());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		beverageSpinner.setAdapter(adapter);

		// See if we are editing
		Bundle args = getArguments();

		if (args != null && args.containsKey(BOTTLE_ID)) {
			bottleID = args.getLong(BOTTLE_ID);
			Bottle bottle = Bottle.getById(bottleID);
			slotText.setText(String.valueOf(bottle.getSlot()));

			for (int i = 0; i < adapter.getCount(); i++) {
				Beverage beverage = adapter.getItem(i);
				
				if (bottle.getBeverage() != null && bottle.getBeverage().equals(beverage)) {
					beverageSpinner.setSelection(i);
					break;
				}
			}

		} else if (args != null && args.containsKey(SLOT)) {
			slot = args.getInt(SLOT);
			slotText.setText(String.valueOf(slot));
		} else {
			throw new RuntimeException("AddBottleFragment does not have any parameters");
		}

		return view;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_save) {
			if (bottleID != -1) {
				Bottle bottle = Bottle.getById(bottleID);
				Beverage beverage = (Beverage) beverageSpinner.getSelectedItem();

				bottle.setBeverage(beverage);
				bottle.save();
				getActivity().finish();
			} else {
				Bottle bottle = new Bottle();

				Beverage beverage = (Beverage) beverageSpinner.getSelectedItem();
				bottle.setBeverage(beverage);

				bottle.setSlot(slot);
				bottle.setCapacity(30);
				bottle.setTaken(0);

				if (bottle.save() == -1) {
					Toast.makeText(getActivity(), "Bottle was not saved", Toast.LENGTH_SHORT).show();
				} else {
					getActivity().finish();
				}
			}
		}
	}
}
