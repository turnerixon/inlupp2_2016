package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;
import javafx.geometry.Pos;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import sun.font.CompositeStrike;

import static org.junit.Assert.*;

/**
 * Created by k.fredrik.eriksson on 2016-09-06.
 */
public class NamedPlaceTest {
    int valixX = 400;
    int valixY = 42;
    int invalidX = -12;
    int invalidY = -20;
    String name = "TestPlace";
    Position validPos = new Position(valixX, valixY);
    Position INVALID_POSITION = new Position(invalidX, invalidY);
    Category cat = Category.Buss;
    NamedPlace namedPlace;

    @Before
    public void createNamedPlace (){
        namedPlace = new NamedPlace(name, validPos, cat);
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

    @Test (expected = IllegalArgumentException.class)
    public void testTooSmallPosition()  {
        new NamedPlace(name, INVALID_POSITION, cat);
    }
}