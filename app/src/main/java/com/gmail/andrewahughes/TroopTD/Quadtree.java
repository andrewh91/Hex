package com.gmail.andrewahughes.TroopTD;

/**
 * Created by andy on 26/02/2016.
 * http://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374
 */

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;
import android.graphics.PointF;

import com.gmail.andrewahughes.framework.Graphics;

public class Quadtree {

    private int MAX_OBJECTS = 2;
    private int MAX_LEVELS = 5;

    private int level;
    //private List objects;
    private List<Rect> objects = new ArrayList<Rect>();
    public List<Integer> ids = new ArrayList<Integer>();
    public Rect bounds;
    private Quadtree[] nodes;

    /*
     * Constructor
     */
    public Quadtree(int pLevel, Rect pBounds) {
        level = pLevel;
        objects = new ArrayList();
        ids = new ArrayList();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    /*
     * Clears the quadtree
     */
    public void clear() {
        objects.clear();
        ids.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    /*
 * Determine which node the object belongs to. -1 means
 * object cannot completely fit within a child node and is part
 * of the parent node
 */
    /*
 * Splits the node into 4 subnodes
 */
    private void split() {
        int subWidth = (int)(bounds.width()/2);
        int subHeight = (int)(bounds.height() / 2);
        int x = bounds.left;
        int y = bounds.top;

        /*
        nodes[0] = new Quadtree(level+1, new Rect(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level+1, new Rect(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level+1, new Rect(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level+1, new Rect(x + subWidth, y + subHeight, subWidth, subHeight));
        */

        nodes[0] = new Quadtree(level+1, new Rect(x + subWidth, y,              x + subWidth + subWidth,    y + subHeight));
        nodes[1] = new Quadtree(level+1, new Rect(x,            y,              x + subWidth,               y + subHeight));
        nodes[2] = new Quadtree(level+1, new Rect(x,            y + subHeight,  x + subWidth,               y + subHeight + subHeight));
        nodes[3] = new Quadtree(level+1, new Rect(x + subWidth, y + subHeight,  x + subWidth + subWidth,    y + subHeight + subHeight));
    }

    /*private int getIndex(Rect pRect) {
        int index = -1;
        //double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        //double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
        double verticalMidpoint = bounds.centerX();
        double horizontalMidpoint = bounds.centerY();

        // Object can completely fit within the top quadrants
        //boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        boolean topQuadrant = (pRect.top< horizontalMidpoint && pRect.top + pRect.height() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRect.top > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        //if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
        if (pRect.right < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            }
            else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        //else if (pRect.getX() > verticalMidpoint) {
        else if (pRect.left > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            }
            else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }*/

    private int getIndex(Rect pRect) {
        int index = -1;
        //double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        //double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
        double verticalMidpoint = bounds.centerX();
        double horizontalMidpoint = bounds.centerY();

        // Object can completely fit within the top quadrants
        //boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        boolean topQuadrant = (pRect.top< horizontalMidpoint && pRect.top + pRect.height() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRect.top > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        //if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
        if (pRect.right < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            }
            else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        //else if (pRect.getX() > verticalMidpoint) {
        else if (pRect.left > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            }
            else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    /*
 * Insert the object into the quadtree. If the node
 * exceeds the capacity, it will split and add all
 * objects to their corresponding nodes.
 */
    public void insert(Rect pRect,int pIds) {
        if (nodes[0] != null) {
            int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect,pIds);

                return;
            }
        }

        objects.add(pRect);
        ids.add(pIds);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i), ids.remove(i));
                    //ids.remove(i);
                }
                else {
                    i++;
                }
            }
        }
    }
    /*
 * Return all objects that could collide with the given object
 */
    public List retrieve(List returnObjects, Rect pRect,List returnIds) {
        int index = getIndex(pRect);

        if(nodes[0]!=null)
        {
            if(index!=-1)
            {
                nodes[index].retrieve(returnObjects, pRect,returnIds);
            }
            else
            {
                for(int i =0;i<nodes.length;i++)
                {
                    nodes[i].retrieve(returnObjects, pRect,returnIds);
                }
            }
        }
        returnObjects.addAll(objects);

        returnIds.addAll(ids);
        return returnObjects;
    }
    public void paint(Graphics graphics, Point camera, float zoom) {

        for (int i = 0; i < nodes.length; i++) {

            if (nodes[i] != null) {
                nodes[i].paint(graphics,camera,zoom);
                graphics.drawRectBorder(new PointF(nodes[i].bounds.centerX(), nodes[i].bounds.centerY()), nodes[i].bounds.width(), nodes[i].bounds.height(), zoom, camera, Color.argb(100, 255, 0, 0));
            }
        }
    }
}

