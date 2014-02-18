package uofs.robotics.bartender.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "drinks")
public class Drink extends Model {

	@Column(name = "name")
	private String name;
	
	public Drink() 
	{
		super();
	}
	
	public Drink (String name)
	{
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Drink> getAll() {
		return new Select().from(Drink.class).orderBy("name DESC").execute();
	}
}
