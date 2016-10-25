package com.gmail.andrewahughes.Hex;

import android.graphics.Point;

import com.gmail.andrewahughes.framework.Graphics;

/**
 * Created by andy on 07/05/2016.
 */
public class BulletTrace {

    Point targetPos;
    Point troopPos;
    float timer;
    final float MAX_TIMER = 50;

    public BulletTrace(int targetPosX, int targetPosY, int troopPosX, int troopPosY)
    {
        targetPos=new Point(targetPosX,targetPosY);
        troopPos=new Point(troopPosX,troopPosY);
        timer=MAX_TIMER;
    }
    public boolean update(float deltaTime)
    {
        timer-=deltaTime;
        if(timer<=0)
        {
            return true;
        }
        return false;
    }
    public void paint(Graphics graphics,Point camera,float zoom)
    {

    }
}
