package inlupp2_2016;

public class NamedPlace extends Place{
	//private String farg;

	public NamedPlace(String name){
		super(name);
	}

	public NamedPlace(String name, String category){
		super(name, category);
	}


	@Override
	public String getName() {
		return super.getName();
	}

	public String getCategory(){
		return super.getCategory();
	}


	public String toString () {
		return getName() + " " + getCategory();
	}
}
