package com.gmail.andrewahughes.Hex;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.gmail.andrewahughes.framework.Graphics;

public class Command {// this class will contain all the methods to interact
						// with the gameplay, e.g. control troop movement

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

	boolean commandState, selectMode, movementMode, editMode;
	int destX, destY;
	int marqueeOriginX, marqueeOriginY;
	List<Troop> troops = new ArrayList<Troop>();
	//List<Troop> enemies = new ArrayList<Troop>();
	List<Integer> troopSelected = new ArrayList<Integer>();
	// int troopSelected=0;//might need an array instead
	int destinationSelected = 0;
	Rect marqueeRect = new Rect();
	boolean marqueeAlive=false;

	public Command() {
		commandState = true;// movement is allowed - not paused

		/* make some random troops
		for (int i = 0; i < 10; i++) {
			Random n;
			n = new Random();
			troops.add(new Troop((int) (1280 * n.nextDouble()), (int) (800 * n
					.nextDouble())));
		}
		/*for(int j=0;j<10;j++)
		{
			enemies.add(new Troop(0,400));
		}*/
		//create troops based on wave data text file
		for (int i = 0; i < Data.initialTroopNo; i++) {
			Data.troops.get(i).setPos(Data.initialTroopRectX,Data.initialTroopRectY);
		}
		Rect r = new Rect(Data.initialTroopRectX,Data.initialTroopRectY,Data.initialTroopRectX2,Data.initialTroopRectY2);
		List<Integer> allTroops = new ArrayList<Integer>();
		for(int j=0;j<Data.initialTroopNo;j++)
		{
			allTroops.add(j);
		}
		directTo(allTroops,r);
	}

	public void evaluateTouch(int positionX, int positionY) {
		int len = Data.troops.size();
		if (state == interactionState.select) {
			 if (selState == selectionState.marquee&&getMarqueeSize(positionX, positionY)>25) {   //if in marquee select mode and we draw a big enough rect
				if(marqueeAlive)
				{
					finishMarquee(positionX, positionY);
				}
				troopSelected.clear();
				for (int i = 0; i < len; i++) {
					
					if (marqueeRect.contains(Data.troops.get(i).rectangle.centerX(),Data.troops.get(i).rectangle.centerY())) {
						state = interactionState.direct;
						troopSelected.add(i);
					}
				}
			}
			 else if (getMarqueeSize(positionX, positionY)<=25) {
				for (int i = 0; i < len; i++) {

					if (Data.troops.get(i).rectangle.contains(positionX, positionY)) {
						state = interactionState.direct;
						troopSelected.clear();
						troopSelected.add(i);
						break;
					}
					for (int j = 0; j < Data.troops.get(i).destination.size(); j++) {
						if (Data.troops.get(i).destination.get(j).rectangle
								.contains(positionX, positionY)) {
							destinationSelected = j;
							troopSelected.clear();
							troopSelected.add(i);
							state = interactionState.edit;
						}
					}
				}
			} 
		}

		else if (state == interactionState.direct) {
			if(marqueeAlive)
			{
				finishMarquee(positionX, positionY);
			}
			directTo(troopSelected,marqueeRect);
			state = interactionState.select;
		} else if (state == interactionState.edit) {
			for (int i = 0; i < troopSelected.size(); i++) {
				Data.troops.get(troopSelected.get(i)).editDestination(
						destinationSelected, positionX, positionY);
			}
			state = interactionState.select;
		}

	}

	public void directTo(List<Integer> troop, Rect rect) {
		// troops.get(troop).setDirection(x, y);//adds a new destination to the
		// troop
		//for (int i = 0; i < troop.size(); i++) {
		//	troops.get(troop.get(i)).addDestination(x, y);
		//}
		int noOfTroops = troop.size();
		int width = rect.right-rect.left;
		if(width==0)
		{
			width=1;
		}
		int height = rect.bottom-rect.top;
		if(height==0)
		{
			height=1;
		}
		double distBetweenTroops = Math.sqrt((width*height))/Math.sqrt(noOfTroops);
		int columns = (int) Math.floor( (width/distBetweenTroops));
		if(columns<1)
		{
			columns=1;
		}
		int rows =(int) Math.floor(height/distBetweenTroops);
		
		if(rows<1)
		{
			rows=1;
		}
		int difference = noOfTroops-(rows*columns);
		if(difference>0)
		{
			if(Math.ceil((double)difference/(double)rows)*rows<Math.ceil((double)difference/(double)columns)*columns)
			{
				columns=(int) (columns+Math.ceil((double)difference/(double)rows));
			}
			else
			{
				rows=(int)(rows+Math.ceil((double)difference/(double)columns));
			}
		}
		else
		{
			if(columns==1)
			{
				rows=rows+difference;
			}
			else if(rows==1)
			{
				columns=columns+difference;
			}
		}
		
		int k = 0;
		
		for(int i = 0; i<columns;i++)
		{
			for(int j = 0; j < rows;j++)
			{
				
				if(k<noOfTroops)
				{
					if(columns<=1&&rows<=1||noOfTroops<=1)
					{
						Data.troops.get(troop.get(k)).addDestination(rect.left+(int)((float)width/2),rect.top+(int)((float)height/2));
					}
					else if(columns<=1)
					{
						Data.troops.get(troop.get(k)).addDestination(rect.left+(int)((float)width/2),rect.top+(int)((float)height*((float)j/(float)(rows-1))));

					}
					else if(rows<=1)
					{
						Data.troops.get(troop.get(k)).addDestination(rect.left+(int)((float)width*((float)i/(float)(columns-1))),rect.top+(int)((float)height/2));

					}
					else if(columns>1&&rows>1)
					{
						Data.troops.get(troop.get(k)).addDestination(rect.left+(int)((float)width*((float)i/(float)(columns-1))),rect.top+(int)((float)height*((float)j/(float)(rows-1))));
					}
					k++;
				}
				else
				{
					break;
				}
			}
		}
	}
		

	public void update(float dt) {

		int len = Data.troops.size();

		if (commandState) {
			for (int i = 0; i < len; i++) {
				Data.troops.get(i).fireTimer-=dt;
				if (Data.troops.get(i).destination.size() > 0) {//move troops
					Data.troops.get(i).moveTo(dt);
				}
				for (int j = 0 ; j <Data.troops.get(i).bulletTraceList.size();j++)
				{
					if(Data.troops.get(i).bulletTraceList.get(j).update(dt))
					{
						Data.troops.get(i).bulletTraceList.remove(j);
					}
				}
			}
			/*for(int j = 0;j<enemies.size();j++)
			{
				if(enemies.get(j).destination.size()>0)
				{
					enemies.get(j).moveTo(dt);
				}
				else
				{
					a*=-1;
					enemies.get(j).addDestination(640+a, 400);
				}
			}*/
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

	public void createTroop() {
		Data.troops.add(new Troop());
	}

	public void createTroop(int positionX, int positionY) {
		Data.troops.add(new Troop(positionX, positionY));
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
		int len = Data.troops.size();
		for (int i = 0; i < len; i++) {
			graphics.drawScaledImage(Data.troops.get(i).image,(int) (Data.troops.get(i).position.x*zoom+camera.x),(int) (  Data.troops.get(i).position.y*zoom+camera.y),Data.troops.get(i).rectangle.width(),Data.troops.get(i).rectangle.height(),zoom);
					
			graphics.drawRect(new Rect((int) (Data.troops.get(i).rectangle.left * zoom + camera.x),
					(int) (Data.troops.get(i).rectangle.top * zoom + camera.y),
					(int) (Data.troops.get(i).rectangle.right * zoom + camera.x),
					(int) (Data.troops.get(i).rectangle.bottom * zoom + camera.y)), Color.argb(100,
					Data.troops.get(i).colour, 0, 0));
			graphics.drawRect(new Rect((int)(Data.troops.get(i).rangeRect.left*zoom+camera.x),(int)(Data.troops.get(i).rangeRect.top*zoom+camera.y),
					(int)(Data.troops.get(i).rangeRect.right*zoom+camera.x),(int)(Data.troops.get(i).rangeRect.bottom*zoom+camera.y)), Color.argb(100,0,0,255));

			Data.troops.get(i).paint(graphics, camera,zoom);
		}
		/*for(int j = 0; j < enemies.size();j++){
			graphics.drawScaledImage(enemies.get(j).image,(int) (enemies.get(j).position.x*zoom+camera.x),(int) (  enemies.get(j).position.y*zoom+camera.y),enemies.get(j).rectangle.width(),enemies.get(j).rectangle.height(),zoom);
			
			graphics.drawRect(new Rect((int)(enemies.get(j).rectangle.left*zoom + camera.x),
					(int)(enemies.get(j).rectangle.top*zoom + camera.y),
					(int)(enemies.get(j).rectangle.right*zoom + camera.x),
					(int)(enemies.get(j).rectangle.bottom*zoom + camera.y)), Color.argb(100,
					255, 0, 0));
			enemies.get(j).paint(graphics, camera);
		}*/
	}
}
