package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.BartenderApplication;
import uofs.robotics.bartender.R;
import uofs.robotics.bartender.models.Drink;
import uofs.robotics.bartender.services.BluetoothService;
import uofs.robotics.bartender.services.BluetoothServiceReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkInfoFragment extends Fragment implements BluetoothServiceReceiver, OnClickListener {

	
	public static final String PARAM_DRINK_ID = "DRINK_ID";

	private static final String TAG = "DrinkInfoFragment";
	
	private BluetoothService bluetoothService;

	private TextView drinkText;
	private ListView ingredientsList;
	private Button orderButton;

	private long drinkId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getActivity().getIntent().getExtras();
		
		// Get the paramater
		if (bundle.containsKey(PARAM_DRINK_ID)) {
			drinkId = bundle.getLong(PARAM_DRINK_ID);
			Log.d(TAG, "Recieved parameter drink id with value of " + drinkId);
		} else {
			throw new RuntimeException("Drink ID was not provided");
		}

		bluetoothService = ((BartenderApplication) getActivity().getApplication()).getBluetoothService();
		bluetoothService.registerReciever(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_drink_info, container, false);

		drinkText = (TextView) view.findViewById(R.id.text_drink_name);
		Drink drink = Drink.getById(drinkId);
		drinkText.setText(drink.getName());
		
		ingredientsList = (ListView) view.findViewById(R.id.list_ingredients);

		orderButton = (Button) view.findViewById(R.id.button_order);
		orderButton.setOnClickListener(this);
		setOrderButtonState(bluetoothService.getState());

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		bluetoothService.unregisterReciever(this);
	}

	public void setOrderButtonState(int state) {
		if (state == BluetoothService.STATE_CONNECTED) {
			orderButton.setEnabled(true);
		} else {
			orderButton.setEnabled(false);
		}
	}

	@Override
	public void stateChange(int oldState, final int newState) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setOrderButtonState(newState);
			}
		});
	}

	@Override
	public void dataReceived(byte[] data, int bytesRead) {
		// Don't care
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_order) {
			Toast.makeText(getActivity(), "Comming soon", Toast.LENGTH_SHORT).show();
		}
	}
}
