package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;
import inlupp2_2016.places.Place;

import java.awt.*;

public class NamedPlace extends Place {

    String name;

    public NamedPlace(String name, Position position, Category category) {
        super(position, category);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NamedPlace: " + getName();
    }

    @Override
    public String getPrintableInfo() {
        return "Named," + getCategory().name() + "," + getX() + "," + getY() + "," + getName();
    }
}
