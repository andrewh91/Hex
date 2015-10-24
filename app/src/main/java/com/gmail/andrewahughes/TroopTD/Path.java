package com.gmail.andrewahughes.TroopTD;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 30/08/2015.
 */
public class Path {

    int ID;

    List<Point> pointList = new ArrayList<Point>();

    public Path(int i)
    {
        ID=i;
    }
    public void addPath(Point p, int i)
    {
        pointList.add(p);
        ID=i;
    }
}
