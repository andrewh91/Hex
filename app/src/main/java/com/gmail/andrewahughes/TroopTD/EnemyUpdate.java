package com.gmail.andrewahughes.TroopTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.gmail.andrewahughes.TroopTD.GameScreen.GameState;
import com.gmail.andrewahughes.framework.Graphics;
public class EnemyUpdate {


	enum interactionState {
		select, // select troops or destinations
		direct, // direct troops
		edit// edit directions for troops
	}

	enum selectionState {
		marquee, // draw a rectangle to select mode
	}

	interactionState state = interactionState.select;
	selectionState selState = selectionState.marquee;

	Paint paint;
	String text;
	float timer;
	int score=0;
	
	boolean commandState, selectMode, movementMode, editMode;
	int destX, destY;
	int marqueeOriginX, marqueeOriginY;
	// int troopSelected=0;//might need an array instead
	int destinationSelected = 0;
	Rect marqueeRect = new Rect();
	boolean marqueeAlive=false;
	PointF enemyBase = new PointF(400,400);
	

	
	public EnemyUpdate() {
		commandState = true;// movement is allowed - not paused
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);

		text = new String();
		timer=0;
		// make some random enemies
		/*for(int j=0;j<5;j++)
		{
			enemies.add(new Enemy(100,100));
		}*/

	}



	public void update(float dt) {
		int len = Data.enemies.size();
		if (commandState) {
			for (int j = 0; j < len; j++) {
				if (Data.enemies.get(j).destination.size() > 0) {
					Data.enemies.get(j).moveTo(dt);
					if (Data.enemies.get(j).score()==1&&Data.enemies.get(j).alive)
					{
						score++;
						Data.enemies.get(j).alive=false;
					}
				}
			}
			if (len>0)
			{
				text = "destination size " + Data.enemies.get(0).destination.size()+" alive? "+ Data.enemies.get(0).alive+" health? "+ Data.enemies.get(0).health+ " score "+score;
			}
			//text = "pos "+enemies.get(0).position;

			//automatically move enemies


			timer+=dt;

				for (int j = 0; j < Data.enemies.size(); j++) {
					if (Data.enemies.get(j).spawned == false) {
						//enemies.get(j).position=enemyBase;
						if(timer>Data.enemies.get(j).delayTimer)
						{
							timer=0;
							for (int k = 0; k < Data.pathList.get(Data.enemies.get(j).pathType).pointList.size(); k++) //for each point in the enemies' paths' pointlist ...
							{
								Data.enemies.get(j).addDestination(Data.pathList.get(Data.enemies.get(j).pathType).pointList.get(k).x, Data.pathList.get(Data.enemies.get(j).pathType).pointList.get(k).y);
							}
							Data.enemies.get(j).alive = true;
							Data.enemies.get(j).spawned = true;
							Data.enemies.get(j).health = Data.enemyTypeList.get(Data.enemies.get(j).enemyType).maxHealth;
							Data.enemies.get(j).armour = Data.enemyTypeList.get(Data.enemies.get(j).enemyType).armour;
							Data.enemies.get(j).speed = Data.enemyTypeList.get(Data.enemies.get(j).enemyType).speed;
							Data.enemies.get(j).colour = Data.enemyTypeList.get(Data.enemies.get(j).enemyType).colour;
						}
					}
				}


		}

	}
	public void startMarquee(int x, int y) {
		marqueeRect.left = x;
		marqueeRect.top = y;
		marqueeAlive=true;
	}

	public int getMarqueeSize(int x,int y)
	{
		int largestSize,width,height; 
		width = marqueeRect.left-x;
		if(width<0)
			width=width*-1;
		height = marqueeRect.top-y;
		if(height<0)
			height=height*-1;
		if(width>height)
			largestSize=width;
		else
			largestSize=height;
		return largestSize;
	}
	public void finishMarquee(int x, int y) {
		if(x>marqueeRect.left)
		{
			marqueeRect.right = x;
		}
		else
		{
			marqueeRect.right=marqueeRect.left;
			marqueeRect.left=x;
		}
		if(y>marqueeRect.top)
		{
			marqueeRect.bottom = y;
		}
		else
		{
			marqueeRect.bottom=marqueeRect.top;
			marqueeRect.top=y;
		}
	}

	public void updateMarquee(int x, int y) {
		if (selState == selectionState.marquee) {
			marqueeRect.right = x;
			marqueeRect.bottom = y;
		}
	}



	public Rect getMarqueeRect() {
		return marqueeRect;
	}


	public void commandStateFalse() {
		commandState = false;
	}

	public void commandStateTrue() {
		commandState = true;
	}

	public boolean getCommandState() {
		return commandState;
	}

	public void commandStateToggle() {
		if (commandState == false) {
			commandState = true;
		} else if (commandState == true) {
			commandState = false;
		}
	}

	public void drawMarquee(Graphics g,Point cameraDrag,float zoom) {
		if(marqueeAlive)
		{
			if (selState == selectionState.marquee) {
				g.drawRect((int)(getMarqueeRect().left*zoom+cameraDrag.x),(int)(getMarqueeRect().top*zoom+cameraDrag.y),
						(int)(getMarqueeRect().right*zoom+cameraDrag.x),(int)(getMarqueeRect().bottom*zoom+cameraDrag.y), Color.argb(50, 50, 50, 255));
			}
		}
	}

	public void paint(Graphics graphics, Point camera, float zoom) {

		graphics.drawString(text, 20, 750, paint);
		for(int j = 0; j < Data.enemies.size();j++){
			if(Data.enemies.get(j).alive)
			{
				graphics.drawScaledImage(Data.enemies.get(j).image, (int) (Data.enemies.get(j).position.x * zoom + camera.x), (int) (Data.enemies.get(j).position.y * zoom + camera.y),Data.enemies.get(j).rectangle.width(), Data.enemies.get(j).rectangle.height(), zoom);
			}
			graphics.drawRect(new Rect((int)(Data.enemies.get(j).rectangle.left*zoom + camera.x),
					(int)(Data.enemies.get(j).rectangle.top*zoom + camera.y),
					(int)(Data.enemies.get(j).rectangle.right*zoom + camera.x),
					(int)(Data.enemies.get(j).rectangle.bottom*zoom + camera.y)), Color.argb(100,
					Data.enemies.get(j).colour, 0, 0));
			Data.enemies.get(j).paint(graphics, camera);
		}
	}
}
