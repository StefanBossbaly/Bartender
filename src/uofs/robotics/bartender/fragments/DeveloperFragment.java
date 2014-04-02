package uofs.robotics.bartender.fragments;

import uofs.robotics.bartender.BartenderApplication;
import uofs.robotics.bartender.R;
import uofs.robotics.bartender.protocol.Message;
import uofs.robotics.bartender.services.BluetoothService;
import uofs.robotics.bartender.services.BluetoothServiceReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DeveloperFragment extends Fragment implements OnClickListener {

	private Button stopButton, pourButton, statusButton;
	private TextView locationText;
	private EditText shotAmountEdit;
	private SeekBar locationSeek;

	private BluetoothService bluetoothService;

	public static DeveloperFragment newInstance() {
		return new DeveloperFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_developer, container, false);

		locationSeek = (SeekBar) view.findViewById(R.id.seek_location);
		locationText = (TextView) view.findViewById(R.id.text_location);

		locationSeek.setMax(12);
		locationSeek.setProgress(0);
		locationText.setText(String.valueOf(locationSeek.getProgress()));
		locationSeek.setOnSeekBarChangeListener(seekBarListener);

		pourButton = (Button) view.findViewById(R.id.button_pour);
		pourButton.setOnClickListener(this);
		shotAmountEdit = (EditText) view.findViewById(R.id.edit_shot_amount);

		stopButton = (Button) view.findViewById(R.id.button_stop);
		stopButton.setOnClickListener(this);

		statusButton = (Button) view.findViewById(R.id.button_status);
		statusButton.setOnClickListener(this);

		if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			setViews(true);
		} else {
			setViews(false);
		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bluetoothService = ((BartenderApplication) getActivity().getApplication()).getBluetoothService();
		bluetoothService.registerReciever(serviceReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Unregister for state changes
		bluetoothService.unregisterReciever(serviceReceiver);
	}

	private void setViews(boolean enabled) {
		locationSeek.setEnabled(enabled);
		pourButton.setEnabled(enabled);
		stopButton.setEnabled(enabled);
		statusButton.setEnabled(enabled);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();

		if (id == R.id.button_pour) {
			String amount = shotAmountEdit.getText().toString();

			try {
				int shotAmount = Integer.valueOf(amount);
				Message message = Message.buildPourCommand(shotAmount);
				bluetoothService.send(message);
			} catch (NumberFormatException e) {
				// Tell the user he/she is an idiot
				Toast.makeText(getActivity(), "Inputed text is not a number jackass", Toast.LENGTH_LONG).show();
			}
		} else if (id == R.id.button_stop) {
			Message message = Message.buildStopCommand();
			bluetoothService.send(message);
		} else if (id == R.id.button_status) {
			Message message = Message.buildStatusCommand();
			bluetoothService.send(message);
		}
	}

	private BluetoothServiceReceiver serviceReceiver = new BluetoothServiceReceiver() {

		@Override
		public void stateChange(int oldState, final int newState) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (newState != BluetoothService.STATE_CONNECTED) {
						setViews(false);
					} else {
						setViews(true);
					}
				}
			});
		}

		@Override
		public void dataReceived(byte[] data, int bytesRead) {
			// Don't care
		}

		@Override
		public void messageRecieved(Message message) {
			// TODO Auto-generated method stub
		}
	};

	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			locationText.setText(String.valueOf(progress));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Not used
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (bluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
				int location = seekBar.getProgress();

				Message message = Message.buildMoveCommand(location);
				bluetoothService.send(message);
			}
		}
	};
}
