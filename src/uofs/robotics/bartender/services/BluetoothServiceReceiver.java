package uofs.robotics.bartender.services;

import uofs.robotics.bartender.protocol.Message;

public interface BluetoothServiceReceiver {
	void stateChange(int oldState, int newState);
	void dataReceived(byte[] data, int bytesRead);
	void messageRecieved(Message message);
}
