package com.gmail.andrewahughes.Hex;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 10/10/2016.
 */
public class Hexes {

    Rect rect;//position of the hex
    Point[] symbolPos;//position of the 6 symbols within the hex
    Image image;
    int hexEdgeLength;//length of one flat edge, this *2 is the widest diameter of the hex
    int hexHeight;//height of the hex ( assuming the point is facing right) this * 2 is the smallest diameter of the hex
    public Hexes(Point pos,int newHexEdgeLength)
    {
        hexEdgeLength = newHexEdgeLength;
        hexHeight =(int)(hexEdgeLength*1.7320508);

        rect = new Rect(pos.x,pos.y,pos.x+hexEdgeLength*2,pos.y+(int)(hexEdgeLength*1.7320508));
    }
    public void drawHexPointRight(int length, Graphics graphics, Point camera, float zoom, Rect rect){
        int height = (int)(length*1.7320508);
        Point[] corner = new Point[6];

        corner[0] = drawHexHelper(new Point((int)(length/2),-height/2),camera,zoom,rect );
        corner[1] = drawHexHelper(new Point((int)(length),(int)(0)),camera,zoom,rect );
        corner[2] = drawHexHelper(new Point((int)(length/2),(int)(height/2)),camera,zoom,rect );
        corner[3] = drawHexHelper(new Point((int)(-length/2),(int)(height/2)),camera,zoom,rect );
        corner[4] = drawHexHelper(new Point((int)(-length),(int)(0)),camera,zoom,rect );
        corner[5] = drawHexHelper(new Point((int)(-length/2),(int)(-height/2)),camera,zoom,rect );

        graphics.drawLine(corner[0].x,corner[0].y,corner[1].x,corner[1].y,Color.argb(100,0,0,0));
        graphics.drawLine(corner[1].x,corner[1].y,corner[2].x,corner[2].y,Color.argb(100,0,0,0));
        graphics.drawLine(corner[2].x,corner[2].y,corner[3].x,corner[3].y,Color.argb(100,0,0,0));
        graphics.drawLine(corner[3].x,corner[3].y,corner[4].x,corner[4].y,Color.argb(100,0,0,0));
        graphics.drawLine(corner[4].x,corner[4].y,corner[5].x,corner[5].y,Color.argb(100,0,0,0));
        graphics.drawLine(corner[5].x,corner[5].y,corner[0].x,corner[0].y,Color.argb(100,0,0,0));
    }
    public void setHexCornersPointDown(int length, /*Graphics graphics, */Point camera, float zoom, Rect rect)
    {
        int height = (int)(length*1.7320508);

        symbolPos[0] = drawHexHelper(new Point((int)(0),-length),camera,zoom,rect );
        symbolPos[1] = drawHexHelper(new Point((int)(height/2),(int)(-length/2)),camera,zoom,rect );
        symbolPos[2] = drawHexHelper(new Point((int)(height/2),(int)(length/2)),camera,zoom,rect );
        symbolPos[3] = drawHexHelper(new Point((int)(0),(int)(length)),camera,zoom,rect );
        symbolPos[4] = drawHexHelper(new Point((int)(-height/2),(int)(length/2)),camera,zoom,rect );
        symbolPos[5] = drawHexHelper(new Point((int)(-height/2),(int)(-length/2)),camera,zoom,rect );

        /*//don't actually need to draw this
        graphics.drawLine(symbolPos[0].x,symbolPos[0].y,symbolPos[1].x,symbolPos[1].y,Color.argb(100,0,0,0));
        graphics.drawLine(symbolPos[1].x,symbolPos[1].y,symbolPos[2].x,symbolPos[2].y,Color.argb(100,0,0,0));
        graphics.drawLine(symbolPos[2].x,symbolPos[2].y,symbolPos[3].x,symbolPos[3].y,Color.argb(100,0,0,0));
        graphics.drawLine(symbolPos[3].x,symbolPos[3].y,symbolPos[4].x,symbolPos[4].y,Color.argb(100,0,0,0));
        graphics.drawLine(symbolPos[4].x,symbolPos[4].y,symbolPos[5].x,symbolPos[5].y,Color.argb(100,0,0,0));
        graphics.drawLine(symbolPos[5].x,symbolPos[5].y,symbolPos[0].x,symbolPos[0].y,Color.argb(100,0,0,0));*/

    }

    public Point drawHexHelper(Point coords,Point camera, float zoom, Rect rect)
    {
        Point point = new Point((int)((coords.x+rect.centerX())*zoom+camera.x),(int)((coords.y+rect.centerY())*zoom+camera.y));
        return point;
    }
    public void paint(Graphics graphics,Point camera, float zoom)
    {
        graphics.drawRect(new Rect((int) (rect.left * zoom + camera.x), (int) (rect.top * zoom + camera.y),
                (int) (rect.right * zoom + camera.x), (int) (rect.bottom * zoom + camera.y)), Color.argb(100, 255, 255, 0));
        drawHexPointRight(hexEdgeLength, graphics, camera, zoom, rect);
        setHexCornersPointDown((int)(hexEdgeLength*.60), camera, zoom, rect);
    }
}
