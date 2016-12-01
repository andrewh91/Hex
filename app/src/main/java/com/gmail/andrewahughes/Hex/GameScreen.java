package com.gmail.andrewahughes.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

import com.gmail.andrewahughes.framework.Game;
import com.gmail.andrewahughes.framework.Graphics;
import com.gmail.andrewahughes.framework.Screen;
import com.gmail.andrewahughes.framework.Input.TouchEvent;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	int gameMode=1;
	Map map;
	SymbolsDB symbolsDB = new SymbolsDB();
	List<Hexes> hexes = new ArrayList<Hexes>();
	List<Point> hexPositions = new ArrayList<Point>();
	int symbolToCompare =6;
	int hexToCompare =2;
	String matchResult = "false";
	int score[]= new int[]{0,0};
	int latestScorer=0;
	// Variable Setup
	// You would create game objects here.

	int livesLeft = 1;
	Paint paint;
	Paint blackText;
	String text;
	String text1;
	String text2;
	Bullet bullet;
	Command command;
	EnemyUpdate enemyUpdate;
	Quadtree quad = new Quadtree(0, new Rect(0,0,1280,800));
	boolean commandState = true;
	boolean cameraMode = false;
	boolean disableControl = false;//variable to stop input commands while moving camera
	//boolean cameraMode = true;
	int no = 0;
	Point cameraOrigin;
	Point cameraDrag;
	PointF zoomOrigin, zoomDrag, zoomDrag2, finger1, finger2;
	float zoomScaleInitial = 1, zoomPinchDistanceInitial, zoomPinchDistance,
			zoomIncrease=1, zoomScale = 1,zoomScale2=1;

	//Button selectBtn;//don't need cos marquee select can also tap select


	// Rect b,c;
	public GameScreen(Game game) {
		super(game);


		cameraOrigin = new Point();
		cameraDrag = new Point(0, 0);
		finger1 = new PointF(0, 0);
		finger2 = new PointF(0, 0);

		zoomOrigin = new PointF(1,1); //zoomDrag, zoomDrag2, finger1, finger2;
		//selectBtn = new Button(1200, 10, "Select", "Marquee Select", true);
		//cameraBtn = new Button(1200, 90, "Camera", "No camera", true);
		// b = new Rect(100,100,100,100);


		// Initialize game objects here

		map = new Map(new Rect(50,50,1230,750));
		if(gameMode==1){//game mode singles
			map.positionHexesSingle();
		}
		else if(gameMode==3) {
			map.calculateTotalHexes(60);//argument hex edge size
			map.positionHexes();
		}

		hexPositions=map.getHexPosition();

		for(int i =0; i<hexPositions.size();i++)
		{
			hexes.add(new Hexes(hexPositions.get(i), map.getHexEdgeSize(), "hex " + i + "",cameraDrag,zoomScale));
		}
		for(int j =0; j<hexPositions.size();j++)
		{
			hexes.get(j).setSetID(symbolsDB.getSetID(getAdjacentSymbolID(j,hexes,map.getTotalHexes())));
			hexes.get(j).setSymbolIDs(symbolsDB.getSetOf6Symbols(hexes.get(j).getSetID()));
		}


		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.setTextSize(20);

		text = new String();
		text1 = new String();
		text2 = new String();

		bullet = new Bullet();
		enemyUpdate = new EnemyUpdate();
		// c = new Rect();

		//Data.save(game.getFileIO());
		Data.loadWeapon(game.getFileIO());
		Data.loadTroop(game.getFileIO());
		Data.loadPath(game.getFileIO());
		Data.load(game.getFileIO());
		Data.loadEnemyType(game.getFileIO());

		command = new Command();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		// We have four separate update methods in this example.
		// Depending on the state of the game, we call different update methods.
		// Refer to Unit 3's code. We did a similar thing without separating the
		// update methods.

		if (state == GameState.Ready)
			updateReady(touchEvents);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {

		// This example starts with a "Ready" screen.
		// When the user touches the screen, the game begins.
		// state now becomes GameState.Running.
		// Now the updateRunning() method will be called!

		if (touchEvents.size() > 0)
			state = GameState.Running;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		//Data.load(game.getFileIO());
		command.update(deltaTime);
		enemyUpdate.update(deltaTime);
		troopInteractionUpdate(Data.troops,Data.enemies,Data.weaponList,deltaTime);
		//interaction between enemy and troop
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			//pointerPos = "touch "+event.x+","+event.y+"zoomOrigin "+zoomOrigin+"zoomDrag"+zoomDrag+"zoomScale"+zoomScale+"distinit"+zoomPinchDistanceInitial+"dist"+zoomPinchDistance;

			//pointerPos = "f1 "+finger1+"f2 "+finger2+"pointer "+event.pointer+"type "+event.type+"initial dist "+zoomPinchDistanceInitial+"dist "+zoomPinchDistance+"scale "+zoomScale+"increase "+zoomIncrease;
			//pointerPos="pos "+event.x+" "+event.y+" fin "+finger1+" fin2 "+finger2+" dist init "+zoomPinchDistanceInitial+" dist "+zoomPinchDistance+" "+zoomIncrease+" "+zoomScale+" "+zoomOrigin;
			//pointerPos="dist "+zoomPinchDistance+" distinit "+zoomPinchDistanceInitial+" z1 "+zoomScale+" z2 "+zoomScale2+" p1 "+finger1+" p2 "+finger2;
			/*text="offset "+command.troops.get(0).offSet+"\t offset2 "+command.troops.get(0).offSet2+" \tpos "+command.troops.get(0).position;
			text1="offset "+command.troops.get(1).offSet+"\t offset2 "+command.troops.get(1).offSet2+"\t pos "+command.troops.get(1).position;
			text2=" origin "+zoomOrigin+" zoom "+zoomScale+"  Pos"+command.troops.get(0).position+" zoomincrease " +zoomIncrease;
			*/
			//text="disabled? "+disableControl+ " pointers "+event.pointer+"marquee alive "+command.marqueeAlive;

			if (event.type == TouchEvent.TOUCH_DOWN) {
				// button logic
				// if we touch a button do nothing, but if touch up event is
				// also on button then press button
				/*if (selectBtn.rectangle.contains(event.x, event.y)) {

				} else if (cameraBtn.rectangle.contains(event.x, event.y)) {

				} else */// if no button is pressed
				if (event.pointer>0) //if two fingers down
				{
					command.marqueeAlive=false;//stop drawing marquee
				}
				else
				{

			if(disableControl==false){
					/*command.startMarquee((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));//*/
			}
					/*
					 * if(no<3) { command.createTroop(event.x,event.y); no++; }
					 * else {
					 */
					// }

				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {

				if(event.x<=hexes.get(0).rect.right)//the the touch point is left of the right edge of the first hex
				{
					symbolToCompare = hexes.get(0).touchEvent(event.x,event.y);
					hexToCompare=0;
				}
				else//the the touch point is right of the first hex
				{
					symbolToCompare = hexes.get(1).touchEvent(event.x,event.y);
					hexToCompare=1;
				}
				compareMatch( symbolToCompare,hexToCompare  );
				// button logic
				// if we touch a button do nothing, but if touch up event is
				// also on button then press button
				/*if (selectBtn.rectangle.contains(event.x, event.y)) {
					selectBtn.toggle();
					command.toggleSelState();
				} else if (cameraBtn.rectangle.contains(event.x, event.y)) {
					cameraBtn.toggle();
					if (cameraMode) {
						cameraMode = false;
					} else if (cameraMode == false) {
						cameraMode = true;
					}
				} else*/ {
					if(disableControl==false)
					{
						command.evaluateTouch((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
					}
				}
				zoomScaleInitial=zoomScale;

				cameraMode=false;
				if(event.noOfPointers==1)//if you lift the last finger up, we no longer control the camera and so ...
				{
					disableControl=false;//allow touch controls
				}


			}
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				// c.left=event.x;
				// c.right=event.x;
				// c.top=event.y;
				// c.bottom=event.y;
				cameraControl1(event);

				if (event.pointer>0) {
					disableControl=true;//if more than one finger down disable controls since we are moving the camera

						command.marqueeAlive=false;//stop drawing marquee

					if(cameraMode==false)
					{
						cameraControlInitiate(event);
					}
					cameraControl(event);
				} else {

					command.updateMarquee((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
				}
			}

		}

		// 2. Check miscellaneous events like death:

		if (livesLeft == 0) {
			state = GameState.GameOver;
		}

		// 3. Call individual update() methods here.
		// This is where all the game updates happen.
		// For example, robot.update();
	}
	private void compareMatch(int symbol,int hex)
	{
		int otherHex= (hex+1)%2;
		if(symbol<6)
		{

			if(Arrays.asList(hexes.get(otherHex).symbolIDs).contains(Integer.valueOf(hexes.get(hex).symbolIDs[symbol])))
			{
				matchResult = "true";
				score[hex]++;
				latestScorer=hex;

				for(int j =0; j<hexPositions.size();j++)
				{
					hexes.get(j).setSetID(symbolsDB.getSetID(getAdjacentSymbolID(j,hexes,map.getTotalHexes())));
					hexes.get(j).setSymbolIDs(symbolsDB.getSetOf6Symbols(hexes.get(j).getSetID()));
				}
			}
			else
			{
				matchResult = "false";
			}
		}
	}
	private void troopInteractionUpdate(List<Troop>troop,List<Enemy>enemy ,List<Weapon> weapons,float deltaTime) {
		//collision detection quad tree////////////////////
		quad.clear();
		//add all objects
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).alive) {
				quad.insert(enemy.get(i).rectangle,i);
			}
		}
		List<Rect> returnObjects = new ArrayList();//this is a list of objects that are potentially in range of the troop
		List<Integer> a = new ArrayList();//ths gives the position of the returned enemy's index number from it's own enemy list rather than the index of it's position in the returned objects list
		List<Integer> firingList = new ArrayList();//a list of objects to fire at for multi hit weapons like shotguns
		//float cone = 0.3926991f;//22.5 degrees = 0.3926991 radians
		for (int i = 0; i < troop.size(); i++) {
			if (troop.get(i).alive) {
				if(troop.get(i).ammo<1){
					troop.get(i).reloadTimer+=deltaTime;//for each troop that is alive and has no bullets, increase the reload timer
				}
				else if(troop.get(i).fireDelay>0)
				{
					troop.get(i).fireDelay-=deltaTime;//for each troop that is alive and has at least one bullet and the fire delay is above 0 then reduce the fire delay
				}

				if(troop.get(i).reloadTimer>troop.get(i).reloadThreshold)
				{
					troop.get(i).ammo=troop.get(i).maxAmmo;
					troop.get(i).reloadTimer=0;//for each troop that is alive and the reload timer reaches the reload threshold, reload ammo and reset reload timer
				}
				if (!troop.get(i).targetAcquired) {//if no target acquired
					if (troop.get(i).autoReloadTimer > troop.get(i).autoReloadThreashold && troop.get(i).ammo < troop.get(i).maxAmmo)//automatically reload if you are waiting around with no target for long enough
					{
						troop.get(i).ammo = troop.get(i).maxAmmo;
						troop.get(i).autoReloadTimer = 0;
					} else {
						troop.get(i).autoReloadTimer += deltaTime;
					}

					if(troop.get(i).fireDelay<=0)
					{//if no target acqyuired, and we have  a bullet ready to fire, then look for a target
						returnObjects.clear();
						a.clear();
						quad.retrieve(returnObjects, troop.get(i).rangeRect, a);//check for potential targets

						if (weapons.get(troop.get(i).weaponType).ammoType==0)//normal bullet target acquiring logic
						{
							for (int j = 0; j < returnObjects.size(); j++) {//for each potential target
								// Run collision detection algorithm between objects
								if (distanceBetween(troop.get(i).position, new PointF(enemy.get(a.get(j)).position.x, enemy.get(a.get(j)).position.y)) < troop.get(i).closestEnemy)
								{//find the closest enemy out of all potential targets

									troop.get(i).target = a.get(j);//set the target as the closest target
									troop.get(i).closestEnemy = distanceBetween(troop.get(i).position, new PointF(enemy.get(a.get(j)).position.x, enemy.get(a.get(j)).position.y));//set the variable for the closest enemy
									troop.get(i).targetAcquired = true;

								}

							}//end of for each return object
						}//end of ammo type 0
						else if(weapons.get(troop.get(i).weaponType).ammoType==1)//shotgun target acquiring logic
						{

						}//end of ammo type 1
					}//end of if fireDelay<=0
				}//end of it no target acquired
				if (troop.get(i).targetAcquired){//if target IS acquired
					troop.get(i).autoReloadTimer=0;//acquiring a target will reset the autoreload timer

					if (troop.get(i).ammo >0) {//if we have any bullets
						//if (troop.get(i).fireTimer < 0) {
						//troop.get(i).fireTimer = weapons.get(troop.get(i).weaponType).delayBetweenShots;
						if (weapons.get(troop.get(i).weaponType).ammoType==0)//normal bullet firing logic
						{
							int wholeBulletsThisFrame;
							troop.get(i).bulletsThisFrame = deltaTime / troop.get(i).fireDelayReset + troop.get(i).fractionalBullets;//logic for handling weapons firing faster than the frame rate
							wholeBulletsThisFrame = (int) troop.get(i).bulletsThisFrame;
							troop.get(i).fractionalBullets = troop.get(i).bulletsThisFrame - (float) wholeBulletsThisFrame;
							if(troop.get(i).fireDelay<0)
							{
								wholeBulletsThisFrame+=1;//if one bullet was already ready to fire then increment whole bullets by one, so that it's at least one
								troop.get(i).fireDelay=troop.get(i).fireDelayReset;//reset the fire delay
							}
							for (int j = wholeBulletsThisFrame; j > 0; j--) {//for eah bullet
								if (enemy.get(troop.get(i).target).alive) {//fire at the target
									troop.get(i).fire((int) enemy.get(troop.get(i).target).position.x, (int) enemy.get(troop.get(i).target).position.y);
									enemy.get(troop.get(i).target).hit(weapons.get(troop.get(i).weaponType).damage);
									troop.get(i).ammo--;
								}
							}

							//}

						}//end of normal bullet firing
						else if (weapons.get(troop.get(i).weaponType).ammoType==1)//shotgun bullet firing logic
						{

						}//end of shotgun firing
						if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range * troop.get(i).range ||
						enemy.get(troop.get(i).target).health <= 0) {//check if target has exited range
							troop.get(i).targetAcquired = false;
							troop.get(i).closestEnemy = troop.get(i).range * troop.get(i).range;
						}
					}//end of if we have any bullets
				}
			}
		}
	}

	private float distanceBetween(PointF p1,PointF p2)
	{
		float a,b;
		a =p1.x-p2.x;
		b =p1.y-p2.y;
		float c = a*a+b*b;//it's more efficient to not bother finding the square root, therefore this doesn't given the distance between points, but it can still be used to try and compare different distances to see whih is smaller / larger
		return c;
	}
	public List<Integer> getAdjacentSymbolID(int i,List<Hexes> h, Point totalHexes)//the argument should use totalHexes from map class, .x is the number of columns, .y is the number of rows
	{
		List<Integer> array = new ArrayList<Integer>();
		if(i %totalHexes.y!=0)//i%totalHexes.y!=0 should check if we are at the top hex in a column ( if we are at the top hex, then obviously there is no hex further up from this
		{
			array.add(h.get(i-1).getSetID());//adds the setID of the hex directly above the current hex

			//this one depends on the parity of the column, is it odd or even?
			//if we are not at the top of the map
			if(i +totalHexes.y-1+  (2*(((i -1)/totalHexes.y)%2))<totalHexes.x*totalHexes.y)//totalHexes.x*totalHexes.y is the total number of hexes in the map, this tells us if we are at the right edge of the map
			{
				array.add(h.get(i +totalHexes.y-1+  (2*(((i -1)/totalHexes.y)%2))).getSetID());
			}
		}
		if((i+totalHexes.y) <=totalHexes.x*totalHexes.y-1)//totalHexes.x*totalHexes.y-1 is the total number of hexes in the map
		{//i+totalHexes.y should reference a hex to the right of the current hex, if that hex's number is more than the total number of hexes then we must have beenat the right edge to begin with
			array.add(h.get(i+totalHexes.y).getSetID() );//add one of the hexes to the right of the current hex, it's either the one above and to the right of below and to the right
		}
		if(i %(totalHexes.y)!=totalHexes.y-1)// i %(totalHexes.y)!=totalHexes.y-1 should check if we are at the bottom hex in a column
		{
			array.add(h.get(i+1).getSetID());//add the hex below the current hex
			//if we're not at the bottom of the map
			if(i-totalHexes.y-1+(2*(((int)(i-1)/totalHexes.y)%2))  >0)//this tells us if we are at the left edge of the map, ((int)(i)/totalHexes.y) is basically the column number, we mod 2 that to find if it's odd or even
			{
				array.add(h.get(i -totalHexes.y-1+  (2*(((i -1)/totalHexes.y)%2))).getSetID());//-1+  (2*(((setID-1)/totalHexes.y)%2)) this subtracts 2 if the column is odd, does nothing if even
			}
		}
		if(i -totalHexes.y>0)//this tells us if we are at the left edge of the map
		{
			array.add(h.get(i -totalHexes.y).getSetID());//add the hex to the left of the current hex
		}


		Set<Integer> removeDuplicates = new HashSet<Integer>();//use a set to remove duplicates
		removeDuplicates.addAll(array);
		array.clear();
		array.addAll(removeDuplicates);

		return array;
	}
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				state = GameState.Running;
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 300 && event.x < 980 && event.y > 100
						&& event.y < 500) {
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	@Override
	public void paint(float deltaTime) {
		// First draw the game elements.

		// Example:
		// g.drawImage(Assets.background, 0, 0);
		// g.drawImage(Assets.character, characterX, characterY);

		Graphics g = game.getGraphics();
			// g.drawRect(0, 0, 1280, 800, Color.argb(255, 153, 217,
		// 234));//cornflower blue :)
		//g.drawARGB(255, 153, 217, 234);// another way to draw a blue background
		g.drawARGB(255, 153, 217*latestScorer, 234*latestScorer);// another way to draw a blue background

		for (int i = 0; i<hexPositions.size();i++)
		{
			hexes.get(i).paint(g, cameraDrag,zoomScale);
		}
		//g.drawRect(new Rect((int)(map.rect.left*zoomScale+cameraDrag.x),(int)(map.rect.top*zoomScale+cameraDrag.y),
		//		(int)(map.rect.right*zoomScale+cameraDrag.x),(int)(map.rect.bottom*zoomScale+cameraDrag.y)),Color.argb(100,255,100,100));

		//command.paint(g, cameraDrag, zoomScale);
		//enemyUpdate.paint(g, cameraDrag, zoomScale);
		//quad.paint(g,cameraDrag, zoomScale);

		g.drawString(text, 10, 30, blackText);
		g.drawString(text1, 10, 60, blackText);
		g.drawString(matchResult+ " player 1 score; "+score[0]+" player 2 score; "+score[1],50,750,blackText);
		//g.drawString(" "+ Data.highscores[0]+"  "+ Data.currentLevel, 50, 90, blackText);
		// Secondly, draw the UI above the game elements.
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();

	}

	private void nullify() {

		// Set all variables to null. You will be recreating them in the
		// constructor.
		paint = null;

		// Call garbage collector to clean up memory.
		System.gc();
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap each side of the screen to move in that direction.",
				640, 300, paint);

	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();

		command.drawMarquee(g, cameraDrag,zoomScale);
		//selectBtn.paint(g, paint);
		//cameraBtn.paint(g, paint);
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(100, 0, 0, 0);
		g.drawString("Tap to resume", 640, 600, paint);

	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString("GAME OVER.", 640, 300, paint);

	}

	@Override
	public void pause() {
		if (state == GameState.Running) {
			state = GameState.Paused;
		}

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		// pause();
		command.commandStateToggle();
		enemyUpdate.commandStateToggle();
		//Data.currentLevel++;
	}
	public void cameraControlInitiate(TouchEvent event)
	{
			//zoomOrigin = new PointF(event.x, event.y);
	
		cameraMode=true;
		if(event.pointer==0)
			{
				finger1=new PointF(event.x,event.y);
			}
		if(event.pointer==1)
		{
			finger2=new PointF(event.x,event.y);
		}

		cameraOrigin = new Point((int)(finger1.x+finger2.x)/2 - cameraDrag.x,(int) (finger1.y+finger2.y)/2
				- cameraDrag.y);
		zoomOrigin=new PointF((finger1.x+finger2.x)/2,(finger1.y+finger2.y)/2);
		zoomPinchDistanceInitial=0;
		if(event.pointer>0)
		{

			zoomScale2=1;
			zoomPinchDistance = 		(float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
			zoomPinchDistanceInitial = 	(float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
		}
		
	}
	public void cameraControl1(TouchEvent event)
	{/*
		if(event.pointer==0)
		{
			finger1 = new PointF(event.x,event.y);
		}//*/
	}
	public void cameraControl(TouchEvent event)
	{/*
	if(event.pointer==1)
	{
		finger2 = new PointF(event.x,event.y);
	}
	if(event.pointer>0)
	{
		zoomPinchDistance = (float) Math.sqrt(((finger1.x-finger2.x)*(finger1.x-finger2.x))+((finger1.y-finger2.y)*(finger1.y-finger2.y)));//find the length of the vector
		if(zoomPinchDistanceInitial!=0)
		{

			if(zoomScale>=10)//zoomscale is the total zoom
			{
				zoomScale=10;
			}
			else
			{
				zoomIncrease=zoomScaleInitial*(zoomPinchDistance/zoomPinchDistanceInitial);			
				zoomScale = zoomIncrease;
				zoomScale2=zoomPinchDistance/zoomPinchDistanceInitial;
			}
			if(zoomScale2>=10)//zoomscale2is the current zoom increase of this gesture
			{
				zoomScale2=10;
			}
		}
	}
	
	cameraDrag = new Point(	(int) ((finger1.x+finger2.x)/2 - cameraOrigin.x),
							(int) ((finger1.y+finger2.y)/2 - cameraOrigin.y));
	//*/
	}
}