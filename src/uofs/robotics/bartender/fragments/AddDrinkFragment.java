package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Drink;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddDrinkFragment extends Fragment implements OnClickListener {

	private EditText name;
	private Button addDrink;

	public AddDrinkFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_add_drink, container, false);

		name = (EditText) view.findViewById(R.id.edit_drink_name);
		addDrink = (Button) view.findViewById(R.id.button_add_drink);

		addDrink.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_add_drink) {
			Drink drink = new Drink();
			drink.setName(name.getText().toString());

			drink.save();

			// We are done here
			getActivity().finish();
		}
	}
}
