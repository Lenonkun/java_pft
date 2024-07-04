package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SquareTests {

    @Test
    public void testPoint() {
        Point p1 = new Point(2, 2);
        Point p2 = new Point(2, 2);
        Double d = p1.distanceClass(p2);

        Assert.assertEquals(d, 0.0);
    }

    @Test
    public void testRun(){
        Point p1 = new Point (2,2);
        Point p2 = new Point (2,2);
        Assert.assertEquals (Run.distance(p1,p2),0.0);
    }
}
