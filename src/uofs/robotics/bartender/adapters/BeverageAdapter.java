package uofs.robotics.bartender.adapters;

import java.util.List;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Beverage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BeverageAdapter extends ArrayAdapter<Beverage> {

	public BeverageAdapter(Context context, List<Beverage> beverages) {
		super(context, R.layout.list_item_beverage, beverages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Beverage beverage = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_beverage, null);
		}

		// Lookup view for data population
		TextView name = (TextView) convertView
				.findViewById(R.id.text_beverage_name);
		TextView type = (TextView) convertView
				.findViewById(R.id.text_beverage_type);

		// Populate the data into the template view using the data object
		name.setText(beverage.getName());
		type.setText(beverage.getType());

		// Return the completed view to render on screen
		return convertView;
	}
}
