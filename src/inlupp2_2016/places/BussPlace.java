package inlupp2_2016.places;

import inlupp2_2016.Position;

import java.awt.*;

/**
 * Created by k.Fredrik.Eriksson on 2016-06-23.
 */
public class BussPlace extends Place {
    final int[] xes = {0, 25, 50};
    final int[] yes = {0, 50, 0};

    public BussPlace(String name, Position position, String category) {
        super(name, position, "Buss");
    }

    protected void visa(Graphics g) {
        g.setColor(Color.RED);
        g.fillPolygon(xes, yes, 3);
    } //End visa)()

    protected void markera(Graphics g) {

    }
} //End BussPlace
