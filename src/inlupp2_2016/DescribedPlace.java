package inlupp2_2016;

public class DescribedPlace extends Place {
	String description;

	public DescribedPlace (String name, String description) {
		super(name);
		this.description=description;
	}

	public DescribedPlace(String name, String description, String category){
		super(name, category);
		this.description=description;

	}

	@Override
	public String getName() {
		return super.getName();
	}

	public String getDescription(){
		return description;
	}
	public String toString(){
		return super.toString() + " " +getDescription(); 
	}
	
}
