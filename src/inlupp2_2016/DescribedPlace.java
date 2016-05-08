package inlupp2_2016;

import javafx.geometry.Pos;

import java.awt.*;

public class DescribedPlace extends Place {
	String description;

	//Iochmed att kategori sätts som None ifall ingen väljs används aldirg den här konstruktorn.
	public DescribedPlace (String name, Position position, String description) {
		super(name, position);
		this.description=description;
	}

	public DescribedPlace(String name, Position position, String description, String category){
		super(name, position, category);
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

	protected void visa (Graphics g){ } //End visa()

	protected void markera (Graphics g){};
}
