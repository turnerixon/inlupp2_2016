package inlupp2_2016;

import java.awt.*;

public class NamedPlace extends Place{
	//private String farg;

	public NamedPlace(String name, Position position){
		super(name, position);
	}

	public NamedPlace(String name, Position position, String category){
		super(name, position, category);
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

	protected void visa (Graphics g){
	}

}
