package inlupp2_2016.places;

import inlupp2_2016.Position;

import java.awt.*;

/**
 * Created by k.Fredrik.Eriksson on 2016-06-23.
 */

public class TrainPlace extends Place {
    final int[] xes = {0, 25, 50};
    final int[] yes = {0, 50, 0};

    public TrainPlace(String name, Position position, String category) {super(name, position, "Tåg"); }

    protected void visa(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillPolygon(xes, yes, 3);
    }

    protected void markera (Graphics g){ }

} //End Class TrainPlace
