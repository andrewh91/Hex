package com.gmail.andrewahughes.Hex;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * Created by andy on 10/10/2016.
 */
public class Hexes {

    Rect rect;//position of the hex
    int setID =31;//number from 0 to 30 indicates which set of 6 symbols this hex uses, the number 31 indicates that it has no set of symbols
    Integer[] symbolIDs;
    Image image;
    int hexEdgeLength;//length of one flat edge, this *2 is the widest diameter of the hex
    int hexHeight;//height of the hex ( assuming the point is facing right) this * 2 is the smallest diameter of the hex
    Paint paint;
    Paint paint1;
    int textSize=120;
    String text1;
    String text2;
    double angle;
    int touchedSymbol=6;


    Point[] hPRCorner = new Point[6];//store the points of the corners of the hex
    Point[] hPDCorner = new Point[6];//store the points of the corners of the hex
    public Hexes(Point pos,int newHexEdgeLength,String text, Point camera, float zoom)
    {
        hexEdgeLength = newHexEdgeLength;
        hexHeight =(int)(hexEdgeLength*1.7320508);

        rect = new Rect(pos.x,pos.y,pos.x+hexEdgeLength*2,pos.y+(int)(hexEdgeLength*1.7320508));

        setHexCornersPointDown(hexEdgeLength, camera, zoom, rect);
        setHexCornersPointRight((int) (hexEdgeLength * 0.60), camera, zoom, rect);//this will be symbol  pos
        text1=text;
        text2="setID ";
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint1 = new Paint();
        paint1.setTextAlign(Paint.Align.CENTER);
        paint1.setTextSize(30);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.WHITE);
    }
    public void setHexCornersPointRight(int length, Point camera, float zoom, Rect rect) {
        int height = (int) (length * 1.7320508);

        hPRCorner[0] = drawHexHelper(new Point((int) (length / 2), -height / 2), camera, zoom, rect);
        hPRCorner[1] = drawHexHelper(new Point((int) (length), (int) (0)), camera, zoom, rect);
        hPRCorner[2] = drawHexHelper(new Point((int) (length / 2), (int) (height / 2)), camera, zoom, rect);
        hPRCorner[3] = drawHexHelper(new Point((int) (-length / 2), (int) (height / 2)), camera, zoom, rect);
        hPRCorner[4] = drawHexHelper(new Point((int) (-length), (int) (0)), camera, zoom, rect);
        hPRCorner[5] = drawHexHelper(new Point((int) (-length / 2), (int) (-height / 2)), camera, zoom, rect);
    }

    public void drawHexPointRight(Graphics graphics)
    {

        graphics.drawLine(hPRCorner[0].x,hPRCorner[0].y,hPRCorner[1].x,hPRCorner[1].y,Color.argb(255,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPRCorner[1].x,hPRCorner[1].y,hPRCorner[2].x,hPRCorner[2].y,Color.argb(255,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPRCorner[2].x, hPRCorner[2].y, hPRCorner[3].x, hPRCorner[3].y, Color.argb(255, 0, 0, 0),hexEdgeLength/50);
        graphics.drawLine(hPRCorner[3].x, hPRCorner[3].y, hPRCorner[4].x, hPRCorner[4].y, Color.argb(255, 0, 0, 0),hexEdgeLength/50);
        graphics.drawLine(hPRCorner[4].x, hPRCorner[4].y, hPRCorner[5].x, hPRCorner[5].y, Color.argb(255, 0, 0, 0),hexEdgeLength/50);
        graphics.drawLine(hPRCorner[5].x, hPRCorner[5].y, hPRCorner[0].x, hPRCorner[0].y, Color.argb(255, 0, 0, 0),hexEdgeLength/50);
    }
    public void setHexCornersPointDown(int length, Point camera, float zoom, Rect rect) {
        int height = (int) (length * 1.7320508);

        hPDCorner[0] = drawHexHelper(new Point((int) (0), -length), camera, zoom, rect);
        hPDCorner[1] = drawHexHelper(new Point((int) (height / 2), (int) (-length / 2)), camera, zoom, rect);
        hPDCorner[2] = drawHexHelper(new Point((int) (height / 2), (int) (length / 2)), camera, zoom, rect);
        hPDCorner[3] = drawHexHelper(new Point((int) (0), (int) (length)), camera, zoom, rect);
        hPDCorner[4] = drawHexHelper(new Point((int) (-height / 2), (int) (length / 2)), camera, zoom, rect);
        hPDCorner[5] = drawHexHelper(new Point((int) (-height / 2), (int) (-length / 2)), camera, zoom, rect);

    }
    public void drawHexPointDown(Graphics graphics)
    {
        graphics.drawLine(hPDCorner[0].x, hPDCorner[0].y, hPDCorner[1].x, hPDCorner[1].y,Color.argb(100,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPDCorner[1].x, hPDCorner[1].y, hPDCorner[2].x, hPDCorner[2].y,Color.argb(100,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPDCorner[2].x, hPDCorner[2].y, hPDCorner[3].x, hPDCorner[3].y,Color.argb(100,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPDCorner[3].x, hPDCorner[3].y, hPDCorner[4].x, hPDCorner[4].y,Color.argb(100,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPDCorner[4].x, hPDCorner[4].y, hPDCorner[5].x, hPDCorner[5].y,Color.argb(100,0,0,0),hexEdgeLength/50);
        graphics.drawLine(hPDCorner[5].x, hPDCorner[5].y, hPDCorner[0].x, hPDCorner[0].y,Color.argb(100,0,0,0),hexEdgeLength/50);

    }
    public Point[] getHexCornersPointDown()
    {
        return hPDCorner;
    }
    public Point drawHexHelper(Point coords,Point camera, float zoom, Rect rect)
    {
        Point point = new Point((int)((coords.x+rect.centerX())*zoom+camera.x),(int)((coords.y+rect.centerY())*zoom+camera.y));
        return point;
    }
    public void setSetID(int id)
    {
        setID = id;
    }
    public int getSetID()
    {
        return setID;
    }
    public void setSymbolIDs(int [] array)
    {
        List<Integer> randomisedList = new ArrayList<Integer>();
        for(int i = 0;i<array.length;i++) {
            randomisedList.add(array[i]);
        }
        Random random= new Random();
        Collections.shuffle(randomisedList,random);
        symbolIDs = new Integer[6];
        for(int i = 0;i<array.length;i++) {
            symbolIDs[i]=randomisedList.get(i);
        }
    }
    public Integer[] getSymbolIDs()
    {
        return symbolIDs;
    }

    public int touchEvent(int touchX,int touchY)
    {
        if(distanceBetween(new Point(touchX,touchY),new Point(rect.centerX(),rect.centerY()))<hexEdgeLength*hexEdgeLength)
        {//if the touch point is approximately within the boundaries of the hex
            double x,y;
            x=touchX-rect.centerX();
            y=touchY-rect.centerY();
            angle = Math.atan2(y,x);
            if (angle <-Math.PI*5/6)
            {
                touchedSymbol=4;
            }
            else if (angle <-Math.PI/2)
            {
                touchedSymbol=5;
            }
            else if (angle <-Math.PI/6)
            {
                touchedSymbol=0;
            }
            else if (angle <Math.PI/6)
            {
                touchedSymbol=1;
            }
            else if (angle <Math.PI/2)
            {
                touchedSymbol=2;
            }
            else if (angle <Math.PI*5/6)
            {
                touchedSymbol=3;
            }
            else
            {
                touchedSymbol=4;
            }

        }

        return touchedSymbol;
    }
    private float distanceBetween(Point p1,Point p2)
    {
        float a,b;
        a =p1.x-p2.x;
        b =p1.y-p2.y;
        float c = a*a+b*b;//it's more efficient to not bother finding the square root, therefore this doesn't given the distance between points, but it can still be used to try and compare different distances to see whih is smaller / larger
        return c;
    }
    public void paint(Graphics graphics, Point camera, float zoom) {

        //graphics.drawRect(new Rect((int) (rect.left * zoom + camera.x), (int) (rect.top * zoom + camera.y),(int) (rect.right * zoom + camera.x), (int) (rect.bottom * zoom + camera.y)), Color.argb(100, 255, 255, 0));
        drawHexPointDown(graphics);
        //setHexCornersPointDown((int) (hexEdgeLength * .60), graphics, camera, zoom, rect);
        //graphics.drawString(text1+angle+" "+touchedSymbol,(int) (rect.left * zoom + camera.x),(int) (rect.top * zoom + camera.y)+textSize,paint);
        //graphics.drawString(text2+setID,(int) (rect.left * zoom + camera.x),(int) (rect.top * zoom + camera.y)+textSize*2,paint);

        for(int i = 0;i < symbolIDs.length;i++)
        {
            graphics.drawString(symbolIDs[i]+"", hPRCorner[i].x, hPRCorner[i].y,paint);
            //graphics.drawString(""+touchedSymbol+" angle "+angle,hPRCorner[i].x,hPRCorner[i].y+100,paint1);
        }
    }
}
