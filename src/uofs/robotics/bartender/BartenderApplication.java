package uofs.robotics.bartender;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class BartenderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
	
}
