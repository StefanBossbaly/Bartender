package uofs.robotics.bartender.services;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.util.Log;
import uofs.robotics.bartender.models.BeverageDrink;
import uofs.robotics.bartender.models.Bottle;
import uofs.robotics.bartender.models.Drink;
import uofs.robotics.bartender.protocol.Message;
import uofs.robotics.bartender.protocol.Protocol;

public class ConnectionService {
	public static final int STATUS_NONE = 0;
	public static final int STATUS_PROCESSING = 1;
	public static final int STATUS_SUCCESS = 2;
	public static final int STATUS_FAIL = 3;

	private BluetoothService bluetoothService;
	private OrderThread orderThread;
	private int status;

	public ConnectionService(BluetoothService service) {
		orderThread = null;
		bluetoothService = service;
		setStatus(STATUS_NONE);
	}

	public synchronized void order(Drink drink) {
		if (getStatus() == STATUS_PROCESSING) {
			throw new RuntimeException("Drink is already being ordered");
		}

		orderThread = new OrderThread(drink);
		orderThread.start();
	}

	private synchronized void setStatus(int status) {
		this.status = status;
	}

	public synchronized int getStatus() {
		return this.status;
	}

	private class OrderThread extends Thread implements BluetoothServiceReceiver {

		private static final String TAG = "OrderThread";

		private Drink drink;

		private CountDownLatch moveSignal;
		private CountDownLatch pourSignal;

		public OrderThread(Drink drink) {
			this.drink = drink;
			this.moveSignal = null;
			this.pourSignal = null;
		}

		@Override
		public void run() {
			Log.d(TAG, "Thread is running");

			// We want to register for messages
			bluetoothService.registerReciever(this);

			// Get the list of ingredients
			List<BeverageDrink> ingredients = drink.getBeverageDrink();

			Log.d(TAG, "Fullfilling order with size of " + ingredients.size());

			for (BeverageDrink ingredient : ingredients) {
				// Get the list of bottles
				List<Bottle> bottles = Bottle.getByBeverage(ingredient.getBeverage());

				// If we don't have a bottle we can't make the drink
				if (bottles.size() == 0) {
					bluetoothService.unregisterReciever(this);
					setStatus(STATUS_FAIL);
					return;
				}

				Bottle target = getClosestBottle(bottles);
				
				Log.d(TAG, "Target location is " + (target.getSlot() + 1));

				// Prepare the countdown latch
				moveSignal = new CountDownLatch(1);

				// Move to the slot
				Log.d(TAG, "Sending move command");
				bluetoothService.send(Message.buildMoveCommand(target.getSlot() + 1));

				// Wait for the operation to finish
				try {
					moveSignal.await();
				} catch (InterruptedException e) {
					bluetoothService.unregisterReciever(this);
					setStatus(STATUS_FAIL);
					return;
				}

				pourSignal = new CountDownLatch(1);

				Log.d(TAG, "Sending pour command");
				bluetoothService.send(Message.buildPourCommand((int) ingredient.getAmount()));

				// Wait for the operation to finish
				try {
					pourSignal.await();
				} catch (InterruptedException e) {
					bluetoothService.unregisterReciever(this);
					setStatus(STATUS_FAIL);
					return;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					bluetoothService.unregisterReciever(this);
					setStatus(STATUS_FAIL);
					return;
				}
			}

			// Go home
			bluetoothService.send(Message.buildMoveCommand(0));

			// No more updates
			bluetoothService.unregisterReciever(this);

			// Hip hip horay
			setStatus(STATUS_SUCCESS);

			// We are done
			Log.d(TAG, "Thread completed successfully");
		}

		private Bottle getClosestBottle(List<Bottle> bottles) {
			// If we have more than one bottle of
			if (bottles.size() > 1) {
				// TODO implement getting the closest bottle
				return bottles.get(0);
			} else {
				return bottles.get(0);
			}
		}

		@Override
		public void stateChange(int oldState, int newState) {
			// TODO make more robus by adding dc when we change state
		}

		@Override
		public void dataReceived(byte[] data, int bytesRead) {
			// Don't care
		}

		@Override
		public void messageRecieved(Message message) {
			// Sure that it's a valid response with the status command
			if (message.isResponse() && message.getResponseCode() == Protocol.RSP_COMPLETE && message.getCommand() == Protocol.CMD_MOVE) {
				if (moveSignal != null)
					moveSignal.countDown();
			} else if (message.isResponse() && message.getResponseCode() == Protocol.RSP_COMPLETE && message.getCommand() == Protocol.CMD_POUR) {
				if (pourSignal != null)
					pourSignal.countDown();
			}
		}
	}
}
