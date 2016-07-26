package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;
import inlupp2_2016.places.Place;
import sun.security.krb5.internal.crypto.Des;

import java.awt.*;

public class DescribedPlace extends NamedPlace {
    String description;

    public DescribedPlace(String name, String description, Position position, Category category) {
        super(name, position, category);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {

        return getName() + " " + getDescription();
    }

    @Override
    public String getPrintableInfo() {
        return "Described," + getCategory().name() + "," + getX() + "," + getY() + "," + getName() + "," + getDescription()+"\n ";
    }
}
