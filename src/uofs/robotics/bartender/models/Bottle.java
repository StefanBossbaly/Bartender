package uofs.robotics.bartender.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "bottles")
public class Bottle extends Model {

	@Column(name = "beverage")
	private Beverage beverage;

	@Column(name = "slot")
	private int slot;

	@Column(name = "capacity")
	private int capacity;

	@Column(name = "taken")
	private int taken;

	public Beverage getBeverage() {
		return this.beverage;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getTaken() {
		return this.taken;
	}

	public void setTaken(int taken) {
		this.taken = taken;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public boolean pour(int shots) {
		if ((this.capacity - this.taken) > shots) {
			return false;
		}

		this.taken += shots;

		return true;
	}

	public static List<Bottle> getAll() {
		return new Select().from(Bottle.class).orderBy("slot ASC").execute();
	}
}
