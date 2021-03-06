package uofs.robotics.bartender.fragments;

import java.util.Set;

import uofs.robotics.bartender.BartenderApplication;
import uofs.robotics.bartender.R;
import uofs.robotics.bartender.protocol.Message;
import uofs.robotics.bartender.services.BluetoothService;
import uofs.robotics.bartender.services.BluetoothServiceReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothDeviceListFragment extends Fragment implements OnClickListener, BluetoothServiceReceiver {

	private TextView newDevicesText, pairedDevicesText, statusText;

	private BluetoothAdapter bluetoothAdapter;
	private ArrayAdapter<String> pairedDevicesAdapter;
	private ArrayAdapter<String> newDevicesAdapter;
	private BluetoothBroadcastReciever bluetoothBroadcastReciever;
	private BluetoothService bluetoothService;
	
	public static BluetoothDeviceListFragment newInstance() {
		return new BluetoothDeviceListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the fragment
		View view = inflater.inflate(R.layout.fragment_bluetooth_device_list_fragment, container, false);

		// Init our list adapters
		pairedDevicesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_device);
		newDevicesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_device);

		// Init our scan button
		Button scanButton = (Button) view.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(this);

		// Pull out our text views
		newDevicesText = (TextView) view.findViewById(R.id.text_new_devices);
		pairedDevicesText = (TextView) view.findViewById(R.id.text_paired_devices);
		statusText = (TextView) view.findViewById(R.id.text_status);

		updateStatus(bluetoothService.getState());

		// Set the list adapters
		ListView newDevicesList = (ListView) view.findViewById(R.id.list_new_devices);
		newDevicesList.setAdapter(newDevicesAdapter);
		newDevicesList.setOnItemClickListener(deviceClickListener);

		ListView pairedDevicesList = (ListView) view.findViewById(R.id.list_paired_devices);
		pairedDevicesList.setAdapter(pairedDevicesAdapter);
		pairedDevicesList.setOnItemClickListener(deviceClickListener);

		// Get the default bluetooth adapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Get the paired devices
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

		// Add them to our list
		if (pairedDevices.size() > 0) {
			pairedDevicesText.setVisibility(View.VISIBLE);

			for (BluetoothDevice device : pairedDevices) {
				pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bluetoothBroadcastReciever = new BluetoothBroadcastReciever();

		// Register when a Bluetooth device is found
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(bluetoothBroadcastReciever, filter);

		// Register when we are done the discovery process
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		getActivity().registerReceiver(bluetoothBroadcastReciever, filter);

		// Register for state updates
		bluetoothService = ((BartenderApplication) getActivity().getApplication()).getBluetoothService();
		bluetoothService.registerReciever(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Make sure that we are not in discovery mode
		bluetoothAdapter.cancelDiscovery();

		// Unregister our receiver
		getActivity().unregisterReceiver(bluetoothBroadcastReciever);

		// Unregister for state changes
		bluetoothService.unregisterReciever(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_scan) {
			newDevicesText.setVisibility(View.VISIBLE);

			// Make sure we are not currently discovering
			if (bluetoothAdapter.isDiscovering()) {
				bluetoothAdapter.cancelDiscovery();
			}

			// Ridding spinners
			getActivity().setProgressBarIndeterminateVisibility(true);

			// Start the discovery
			bluetoothAdapter.startDiscovery();
		}
	}
	
	@Override
	public void dataReceived(byte[] data, int bytesRead) {
		// We don't care
	}
	
	@Override
	public void messageRecieved(Message message) {
		// We don't care
	}

	@Override
	public void stateChange(int oldState, final int newState) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateStatus(newState);
			}
		});
	}

	private void updateStatus(int status) {
		if (BluetoothService.STATE_NONE == status) {
			statusText.setText("None");
		} else if (BluetoothService.STATE_LISTENING == status) {
			statusText.setText("Listening");
		} else if (BluetoothService.STATE_CONNECTING == status) {
			statusText.setText("Connecting");
		} else {
			statusText.setText("Connected");
		}
	}

	private OnItemClickListener deviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View view, int arg2, long arg3) {

			// No more discovery we are going to connect
			bluetoothAdapter.cancelDiscovery();

			// Get the selected MAC address
			String info = ((TextView) view).getText().toString();
			String macAddress = info.substring(info.length() - 17);

			// Get the device from the MAC address
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
			BluetoothDevice current = bluetoothService.getDevice();

			if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
				// Start up the connection
				bluetoothService.connect(device);
			} else {
				// They are equal, so disconnect
				if (current != null && current.getAddress().equals(device.getAddress())) {
					bluetoothService.stop();
				} else {
					bluetoothService.connect(device);
				}
			}
		}
	};

	/**
	 * Our broadcast receiver that will handle the broadcast from the Bluetooth
	 * API
	 * 
	 * @author Stefan Bossbaly
	 * 
	 */
	private final class BluetoothBroadcastReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// Found a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// Make sure it isn't a paired device
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					newDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
				}
				// Discovery process is over
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

				// Stop ridding spinners
				getActivity().setProgressBarIndeterminateVisibility(false);

				// No devices were found, alert the user
				if (newDevicesAdapter.getCount() == 0) {
					// TODO do something
				}
			}
		}
	}
}
