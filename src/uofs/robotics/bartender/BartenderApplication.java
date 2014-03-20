package uofs.robotics.bartender;

import uofs.robotics.bartender.services.BluetoothService;
import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class BartenderApplication extends Application {

	private BluetoothService service;

	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
		service = new BluetoothService();
		service.start();
	}

	public BluetoothService getBluetoothService() {
		return service;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
		service.stop();
	}
}
