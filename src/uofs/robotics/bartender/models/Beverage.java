package uofs.robotics.bartender.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "beverages")
public class Beverage extends Model {

	public static final String TYPE_VODKA = "vodka";
	public static final String TYPE_WISKEY = "wiskey";
	public static final String TYPE_GIN = "gin";
	public static final String TYPE_RUM = "rum";

	@Column(name = "name", notNull = true, unique = true)
	private String name;

	@Column(name = "type", notNull = true)
	private String type;

	public Beverage() {

	}

	public Beverage(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<BeverageDrink> getBottleDrinks() {
		return getMany(BeverageDrink.class, "beverage");
	}

	public List<Bottle> getBottles() {
		return getMany(Bottle.class, "bottle");
	}

	public static List<Beverage> getAll() {
		return new Select().from(Beverage.class).orderBy("name DESC").execute();
	}
}
