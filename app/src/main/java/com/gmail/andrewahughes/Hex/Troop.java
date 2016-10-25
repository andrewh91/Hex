package com.gmail.andrewahughes.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Image;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Environment;

public class Troop {

	List<Destination> destination = new ArrayList<Destination>();
	List<Integer> targets = new ArrayList<Integer>();
	PointF position, prevPos,offSet,offSet2;
	boolean alive = true,targetAcquired=false;
	float speed,fireTimer=5,fireDelay ,fireDelayReset;
	float range=1, closestEnemy=1;
	double length;
	PointF direction;
	Rect rectangle,rangeRect;
	String text;
	String id;
	Paint paint;
	Image image;
	int margin=5,target,colour = 255;
	List<BulletTrace> bulletTraceList = new ArrayList<BulletTrace>();
	int weaponType;
	int ammo,maxAmmo;
	float bulletsThisFrame;
	float fractionalBullets = 0;
	float reloadTimer =0, autoReloadTimer=0,reloadThreshold, autoReloadThreashold;
	public Troop()
	{
		image = Assets.menu;
		rectangle = new Rect(500,300,500+image.getWidth(),300+image.getHeight());
	}
	public Troop(int posX,int posY)
	{

		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		text = new String();
		position=new PointF(posX, posY);
		rangeRect = new Rect(posX-((int)range),posY-((int)range),posX+((int)range),posY+((int)range));
		prevPos=new PointF(posX, posY);
		offSet = new PointF(posX,posY);
		offSet2 =new PointF(0,0);
		speed = 5;
		image = Assets.menu;
		rectangle = new Rect(posX-margin,posY-margin,posX+image.getWidth()+margin,posY+image.getHeight()+margin);
		targets.add(0);

	}
	public Troop(String troopId,int troopSpeed,int troopWeaponType, int maxammo,float tReload, float tAutoReload,int tDelayBetweenShots, int tRange)
	{
		id=troopId;
		ammo=maxammo;
		maxAmmo=maxammo;
		 reloadThreshold = tReload;
		 autoReloadThreashold = tAutoReload;
		fireDelay=tDelayBetweenShots;
		fireDelayReset=tDelayBetweenShots;
		speed = troopSpeed;
		weaponType = troopWeaponType;
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		text = new String();
		position=new PointF(0,0);
		prevPos=new PointF(0,0);
		offSet = new PointF(0,0);
		offSet2 =new PointF(0,0);
		image = Assets.menu;
		rectangle = new Rect(0,0,0,0);
		range = tRange;
		closestEnemy=range*range;
		rangeRect = new Rect((int)position.x-((int)range),(int)position.y-((int)range),(int)position.x+((int)range),(int)position.y+((int)range));

	}

	public void setPos(int posX,int posY){

		position=new PointF(posX, posY);
		prevPos=new PointF(posX, posY);
		offSet = new PointF(posX,posY);
		offSet2 =new PointF(0,0);

		rectangle = new Rect(posX-margin,posY-margin,posX+image.getWidth()+margin,posY+image.getHeight()+margin);
		rangeRect = new Rect(posX-((int)range),posY-((int)range),posX+((int)range),posY+((int)range));

	}
	public String getText() {
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard, "file.txt");
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}


	public void fire(int targetPosX,int targetPosY )
	{
		if(colour<50)
		{
			colour = 255;
		}
		else
		{
			colour *=0.90;
		}

		bulletTraceList.add(new BulletTrace(targetPosX,targetPosY, (int)position.x,(int)position.y));
	}
	public void addDestination(int posX,int posY)
	{
		destination.add(new Destination(posX, posY));//set destination coordinates
		setDirection();
	}
	public void editDestination( int destID, float positionX, float positionY){
		destination.get(destID).changeDest(positionX, positionY);
		setDirection();
	}
	public void setDirection()
	{
		if(destination.size()>0){
			direction = new PointF(destination.get(0).pointF.x-position.x,destination.get(0).pointF.y-position.y);//set the direction vector to head in 
			length = Math.sqrt((direction.x*direction.x)+(direction.y*direction.y));//find the length of the vector

			if(length!=0) {
				direction.x = (float) (direction.x / length);//normalise the direction
				direction.y = (float) (direction.y / length);
			}
		}
	}
	public void moveTo(float dt)
	{
		
		position.x+= direction.x*speed*dt;//increase position by direction
		position.y+= direction.y*speed*dt;
		offSet2.x+= direction.x*speed*dt;
		offSet2.y+= direction.y*speed*dt;

		if(direction.x<0)//if the direction is to the left...
		{
			if(position.x<destination.get(0).pointF.x)//...and we go further left than the destination ...
			{
				position.x=destination.get(0).pointF.x;//...then stop!
				direction.x=0;//set direction x to zero, if y is zero too then destination will be deleted
			}
		}
		else if(direction.x>0)//if the direction is to the right...
		{
			if(position.x>destination.get(0).pointF.x)//...and we go further left than it ...
			{
				position.x=destination.get(0).pointF.x;//...then stop!
				direction.x=0;
			}
		}
		if(direction.y<0)
		{
			if(position.y<destination.get(0).pointF.y)
			{
				position.y=destination.get(0).pointF.y;
				direction.y=0;
			}
		}
		else if(direction.y>0)
		{
			if(position.y>destination.get(0).pointF.y)
			{
				position.y=destination.get(0).pointF.y;
				direction.y=0;
			}
		}

		if(destination.get(0).rectangle.intersect(rectangle))//if the troop has reached the destination 
		{
			destination.remove(0);
				setDirection();
			
		}
		
		updateRect((int)position.x,(int)position.y);

		
	}
	public void updateRect(int x,int y)
	{
		rectangle.left = x-margin;
		rectangle.top = y-margin;
		rectangle.right = x+image.getWidth()+margin;
		rectangle.bottom = y+image.getHeight()+margin;
		rangeRect.left = x-((int)range);
		rangeRect.top = y-((int)range);
		rangeRect.right = x+((int)range);
		rangeRect.bottom = y+((int)range);
	}

	public void removeDirection()
	{
		destination.remove(0);
	}

	public void paint(Graphics graphics,Point camera,float zoom)
	{
		graphics.drawCircle(position.x*zoom+camera.x,position.y*zoom+camera.y,range*zoom,paint);

		//text = "timer"+fireTimer+" target "+targetAcquired+" "+target;
		//text=getText()+"1";
		//graphics.drawString(text, (int)(position.x*zoom+camera.x), (int)(position.y*zoom+camera.y), paint);
		graphics.drawString("ammo "+ammo+" fire delay "+fireDelay, (int)(position.x*zoom+camera.x), (int)(position.y*zoom+camera.y), paint);
        int len = destination.size();
        for (int i = 0; i < len; i++) {
			graphics.drawRect(new Rect((int)(destination.get(i).rectangle.left*zoom+camera.x),(int)(destination.get(i).rectangle.top*zoom+camera.y),
					(int)(destination.get(i).rectangle.right*zoom+camera.x),(int)(destination.get(i).rectangle.bottom*zoom+camera.y)), Color.argb(100,0,255,0));
        }
		for (int j = 0 ; j < bulletTraceList.size();j++)
		{
			graphics.drawLine((int)(bulletTraceList.get(j).troopPos.x*zoom+camera.x),(int)(bulletTraceList.get(j).troopPos.y*zoom+camera.y),(int)(bulletTraceList.get(j).targetPos.x*zoom+camera.x),(int)(bulletTraceList.get(j).targetPos.y*zoom+camera.y),Color.argb((int)(bulletTraceList.get(j).timer/bulletTraceList.get(j).MAX_TIMER)*255,255,255,0));
		}
	}
}
