package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;
import javafx.geometry.Pos;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by k.fredrik.eriksson on 2016-09-06.
 */
public class NamedPlaceTest {
    int x = 400;
    int y = 42;
    String name = "TestPlace";
    Position pos = new Position(x, y);
    Category cat = Category.Buss;
    NamedPlace namedPlace;

    @Before
    public void createNamedPlace (){
        namedPlace = new NamedPlace(name, pos, cat);
    }

    @Test
    public void getName() throws Exception {

        assertEquals("TestPlace", namedPlace.getName());

    }

    @Ignore //Ska klura ut hur man testar detta
    public void getPrintableInfo() throws Exception {
        String msg = namedPlace.getPrintableInfo();
        assertEquals("Named,Buss,375,-8,TestPlace", msg);

    }
}