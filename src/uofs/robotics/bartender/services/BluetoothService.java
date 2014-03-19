package uofs.robotics.bartender.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BluetoothService extends Service {

	private static final String TAG = "BluetoothService";

	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;
	
	
	private BluetoothAdapter bluetoothAdapter;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Service created");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (bluetoothAdapter != null)
		{
			
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	

}
