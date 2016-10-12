package com.gmail.andrewahughes.Hex;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 11/10/2016.
 * The map is the play area, it contains the hexes and methods to calculate how many hexes can fit on the map, and what postion to place them in
 */
public class Map {

    Rect rect;
    Point totalHexes;
    List<Point> hexPositions = new ArrayList<Point>();
    int hexEdgeSize;
    public Map(Rect newRect)
    {
        rect = newRect;
    }

    public Point calculateTotalHexes (int newHexEdgeSize) {
        hexEdgeSize= newHexEdgeSize;
        int totalHexesAcrossWidth = (int) (rect.width() / (1.5 * hexEdgeSize));//works out how many hexagons we could fit across the width of the map
        int totalHexesAcrossHeight = (int) (rect.height() / (1.7320508 * hexEdgeSize));//works out how many hexagons we could fit across the height of the map
        totalHexes = new Point(totalHexesAcrossWidth, totalHexesAcrossHeight);
        return totalHexes;
    }
    public Point getTotalHexes(){
        return totalHexes;
    }
    public void positionHexes()//creates points at which the hexes should be placed to fit them evenly in the map
    {
        for(int x =0; x<totalHexes.x;x++)
        {
            for(int y =0; y<totalHexes.y;y++)
            {
                //Position defined by 1+(1.5a*x),1+(1.7320508a*y)+(0.866025404a*mod(x,2) )
                hexPositions.add(new Point((int)(rect.left+(1.5*hexEdgeSize*x)),(int)(rect.top+(1.7320508*hexEdgeSize*y)+(0.866025404*hexEdgeSize*(x%2)))));
            }
        }
    }
    public List<Point> getHexPosition(){return hexPositions;}
}

