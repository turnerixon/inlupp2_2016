package inlupp2_2016;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by k.fredrik.eriksson on 2016-09-06.
 */
public class PositionTest {
    private Position testPosition = new Position(240, 30);
    @Test
    public void getY() throws Exception {
         assertEquals(30, testPosition.getY());

    }

    @Test
    public void getX() throws Exception {
        assertEquals(240, testPosition.getX());
    }

    @Test
    public void getPostion() throws Exception {
        Position testThePosition = new Position(testPosition.getX(), testPosition.getY());
        assertEquals(testThePosition.getPosition(), testPosition.getPosition());

    }

}