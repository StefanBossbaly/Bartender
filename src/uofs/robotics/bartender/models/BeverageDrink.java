package uofs.robotics.bartender.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "beverage_drink")
public class BeverageDrink extends Model {

	@Column(name = "drink")
	private Drink drink;

	@Column(name = "beverage")
	private Beverage beverage;

	@Column(name = "amount")
	private double amount;

	public BeverageDrink() {

	}

	public BeverageDrink(Beverage beverage, Drink drink, double amount) {
		this.beverage = beverage;
		this.drink = drink;
		this.amount = amount;
	}

	public Drink getDrink() {
		return drink;
	}

	public void setDrink(Drink drink) {
		this.drink = drink;
	}

	public Beverage getBeverage() {
		return beverage;
	}

	public void setBeverage(Beverage beverage) {
		this.beverage = beverage;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
