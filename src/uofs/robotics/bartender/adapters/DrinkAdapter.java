package uofs.robotics.bartender.adapters;

import java.util.List;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Drink;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DrinkAdapter extends ArrayAdapter<Drink> {

    public DrinkAdapter(Context context, List<Drink> users) {
		super(context, R.layout.list_item_drink, users);
	}

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Drink drink = getItem(position);    
        
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_drink, null);
        }
        
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.text_drink_name);
        
        // Populate the data into the template view using the data object
        name.setText(drink.getName());
        
        // Return the completed view to render on screen
        return convertView;
    }
}
