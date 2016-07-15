package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;
import inlupp2_2016.places.Place;

import java.awt.*;

public class NamedPlace extends Place {
	//private String farg;

	public NamedPlace(String name, Position position){
		super(name, position, Category.Undefined);
	}


	public String toString () {
		return super.toString();
	}

	protected void visa (Graphics g){ }

	protected void markera (Graphics g){ };

	protected void utfallning (Graphics g){ };
}
