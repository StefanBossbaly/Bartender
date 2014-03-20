package uofs.robotics.bartender.services;

public interface StateChangeReceiver {
	void stateChange(int oldState, int newState);
}
