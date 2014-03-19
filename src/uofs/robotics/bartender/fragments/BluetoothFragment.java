package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.R;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BluetoothFragment extends Fragment {

	public BluetoothFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_drink, container,
				false);
		return view;
	}
	

	@Override
	public void onResume() {
		super.onResume();
		
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// Make sure we have bluetooth adapter
		if (bluetoothAdapter == null)
		{
			Toast.makeText(getActivity(), "Device does not support bluetooth!", Toast.LENGTH_LONG).show();
			getActivity().finish();
			return;
		}
		
		// Make sure it is enabled
		if (!bluetoothAdapter.isEnabled())
		{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

	}
	
	

}
