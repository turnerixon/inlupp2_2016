package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;

import java.awt.*;

/**
 * Created by k.Fredrik.Eriksson on 2016-06-23.
 */


public class NonePlace extends Place {
    final int[] xes = {0, 25, 50};
    final int[] yes = {0, 50, 0};

    public NonePlace(String name, Position position) {super(name, position, Category.Undefined); }

    protected void visa(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillPolygon(xes, yes, 3);
    }

    protected void markera (Graphics g){
        System.out.println("NonePlaces markera-metod");
    }

} //End Class NonePlace