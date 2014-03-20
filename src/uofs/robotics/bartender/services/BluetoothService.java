package uofs.robotics.bartender.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothService {

	// Our log tag
	private static final String TAG = "BluetoothService";

	// Seems to be the UUID that is compatible with the device
	private static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// States
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTENING = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	private final BluetoothAdapter bluetoothAdapter;

	private ConnectThread connectThread;
	private ConnectedThread connectedThread;
	private int state;

	public BluetoothService() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		state = STATE_NONE;

		Log.i(TAG, "Service initialized");
	}

	private synchronized void setState(int newState) {
		state = newState;
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized void start() {
		Log.d(TAG, "start()");

		// Make sure that we are not connecting
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Make sure there wasn't a connected device
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		setState(STATE_NONE);
	}

	public synchronized BluetoothDevice getDevice() {
		if (getState() == STATE_CONNECTED) {
			return connectedThread.socket.getRemoteDevice();
		}

		return null;
	}

	public synchronized void connect(BluetoothDevice device) {
		Log.d(TAG, "connect()");

		// Make sure that we are not connecting
		if (getState() == STATE_CONNECTING) {
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}

		// Make sure there wasn't a thread that already exists
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Start connecting
		connectThread = new ConnectThread(device);
		connectThread.start();

		setState(STATE_CONNECTING);
	}

	public synchronized void connected(BluetoothDevice device, BluetoothSocket socket) {
		Log.d(TAG, "connected()");

		// Make sure that we are not connecting
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Make sure there wasn't a thread that already exists
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		connectedThread = new ConnectedThread(socket);
		connectedThread.start();

		setState(STATE_CONNECTED);
	}

	public synchronized void stop() {
		Log.d(TAG, "stop()");

		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Make sure there wasn't a thread that already exists
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		setState(STATE_NONE);
	}

	private void connectionFailed() {
		setState(STATE_NONE);

	}

	private void connectionLost() {
		setState(STATE_NONE);
	}

	private class ConnectThread extends Thread {
		private final BluetoothDevice device;
		private final BluetoothSocket socket;

		public ConnectThread(BluetoothDevice device) {
			this.device = device;

			// Temp socket pointer
			BluetoothSocket tempSocket = null;

			try {
				tempSocket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Can not create socket to device", e);
			}

			socket = tempSocket;
		}

		@Override
		public void run() {
			Log.i(TAG, "ConnectThread started and running");

			// Cancel Discovery
			bluetoothAdapter.cancelDiscovery();

			Log.i(TAG, "Connecting to device " + socket.getRemoteDevice().getAddress());

			// Try and connect to the device
			try {
				socket.connect();
			} catch (IOException e) {
				// We failed
				connectionFailed();
				try {
					Log.e(TAG, "Error trying to conect() socket ... atempting to close", e);

					socket.close();
				} catch (IOException e2) {
					Log.e(TAG, "Can not close() socket. Just dying ...", e2);
				}

				// Just die
				return;
			}

			// Reset connection thread atomically
			synchronized (BluetoothService.this) {
				connectThread = null;
			}

			// Start up the connected thread to handle connections
			connected(device, socket);
		}

		public void cancel() {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.e(TAG, "Trying to close() socket", e);
				}
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket socket;
		private final InputStream inputStream;
		private final OutputStream outputStream;

		public ConnectedThread(BluetoothSocket socket) {
			this.socket = socket;

			// Our temp pointers
			InputStream tempIn = null;
			OutputStream tempOut = null;

			// Try to open the streams
			try {
				tempIn = this.socket.getInputStream();
				tempOut = this.socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "Could not open streams from socket", e);
			}

			inputStream = tempIn;
			outputStream = tempOut;
		}

		@Override
		public void run() {

			byte[] buffer = new byte[32];
			int bytes = -1;

			while (true) {
				// See if we have some input
				try {
					if (inputStream.available() > 0) {

						bytes = inputStream.read(buffer);

						// TODO call handler
						String s = "";

						for (int i = 0; i < bytes; i++) {
							s += Character.toString((char) buffer[i]);
						}

						Log.d(TAG, "Recieved input bytes read " + bytes + " buffer containted " + s);
					} else {
						// We don't have anything to do so give up CPU time
						Thread.yield();
					}
				} catch (IOException e) {
					Log.e(TAG, "Connection lost with bluetooth device.", e);

					// Oh no
					connectionLost();

					// Try to clean up the connections
					try {
						inputStream.close();
						outputStream.close();
						socket.close();
					} catch (IOException e2) {
						Log.e(TAG, "Exception occured while trying to close disconnected socket", e2);
					}

					// Exit out of the loop
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				outputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write() throw and exception", e);
			}
		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() on socket threw exception", e);
			}
		}
	}
}
