package uofs.robotics.bartender;

import uofs.robotics.bartender.services.BluetoothService;
import uofs.robotics.bartender.services.ConnectionService;
import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class BartenderApplication extends Application {

	private BluetoothService service;
	private ConnectionService connectionService;

	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
		service = new BluetoothService();
		service.start();
		connectionService = new ConnectionService(service);
	}

	public BluetoothService getBluetoothService() {
		return service;
	}

	public ConnectionService getConnectionService() {
		return this.connectionService;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
		service.stop();
	}
}
