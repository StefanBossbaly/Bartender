package uofs.robotics.bartender.adapters;

import java.util.ArrayList;

import uofs.robotics.bartender.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class IngredientAdapter extends ArrayAdapter<IngredientListItem> {

	public IngredientAdapter(Context context) {
		super(context, R.layout.list_item_ingredient, new ArrayList<IngredientListItem>());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		IngredientListItem item = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ingredient, null);
		}

		// Handle it
		item.setupView(getContext(), convertView);

		// Return the completed view to render on screen
		return convertView;
	}
}
