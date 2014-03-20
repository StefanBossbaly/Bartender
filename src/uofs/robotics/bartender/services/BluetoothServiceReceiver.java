package uofs.robotics.bartender.services;

public interface BluetoothServiceReceiver {
	void stateChange(int oldState, int newState);
	void dataReceived(byte[] data, int bytesRead);
}
