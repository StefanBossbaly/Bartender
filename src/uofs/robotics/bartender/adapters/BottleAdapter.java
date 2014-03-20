package uofs.robotics.bartender.adapters;

import java.util.List;

import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Bottle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BottleAdapter extends BaseAdapter {

	private List<Bottle> bottles;
	private Context context;

	public BottleAdapter(Context context, List<Bottle> bottles) {
		super();

		this.context = context;
		this.bottles = bottles;
	}

	private Bottle findBySlot(int slot) {
		for (Bottle bottle : this.bottles) {
			if (bottle.getSlot() == slot) {
				return bottle;
			}
		}

		return null;
	}

	@Override
	public int getCount() {
		// TODO replace this with a config value
		return 12;
	}

	@Override
	public Object getItem(int position) {
		return findBySlot(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Bottle bottle = (Bottle) getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(R.layout.list_item_bottle, null);
		}

		// Lookup view for data population
		TextView name = (TextView) convertView.findViewById(R.id.text_drink_name);
		TextView slot = (TextView) convertView.findViewById(R.id.text_bottle_slot);

		// Populate the data into the template view using the data object
		if (bottle != null) {
			name.setText(bottle.getBeverage().getName());
			slot.setText(String.valueOf(bottle.getSlot()));
		} else {
			name.setText("No Bottle");
			slot.setText(String.valueOf(position));
		}

		// Return the completed view to render on screen
		return convertView;
	}

}
