package com.gmail.andrewahughes.TroopTD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

		// Initialize game objects here

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
		cameraOrigin = new Point();
		cameraDrag = new Point(0, 0);
		finger1 = new PointF(0, 0);
		finger2 = new PointF(0, 0);

		zoomOrigin = new PointF(1,1); //zoomDrag, zoomDrag2, finger1, finger2;
		//selectBtn = new Button(1200, 10, "Select", "Marquee Select", true);
		//cameraBtn = new Button(1200, 90, "Camera", "No camera", true);
		// b = new Rect(100,100,100,100);
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
					command.startMarquee((int)((event.x - cameraDrag.x)/zoomScale), (int)((event.y
							- cameraDrag.y)/zoomScale));
			}
					/*
					 * if(no<3) { command.createTroop(event.x,event.y); no++; }
					 * else {
					 */
					// }

				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {

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

	private void troopInteractionUpdate(List<Troop>troop,List<Enemy>enemy ,List<Weapon> weapons,float deltaTime) {
		//collision detection quad tree////////////////////
		quad.clear();
		//add all objects
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).alive) {
				quad.insert(enemy.get(i).rectangle,i);
			}
		}
		List<Rect> returnObjects = new ArrayList();
		List<Integer> a = new ArrayList();

		List<PointF> antiClockwise = new ArrayList();//This is not a point like a cartesian point, i just need 2 variables, stores a list of enemies within shotgun cone of fire and that enemy's angle relative to the primary target from the troop
		List<PointF> clockwise = new ArrayList();//same as above
		List<Integer> firingList = new ArrayList();//a list of objects to fire at
		List<Integer> tempFiringList = new ArrayList();//a second list so we can compare lists and see which one has more objects
		float cone = 0.3926991f;//22.5 degrees
		for (int i = 0; i < troop.size(); i++) {
			if (troop.get(i).alive) {
				if(troop.get(i).ammo<1){
					troop.get(i).reloadTimer+=deltaTime;
				}
				if(troop.get(i).reloadTimer>troop.get(i).reloadThreshold)
				{
					troop.get(i).ammo=troop.get(i).maxAmmo;
					troop.get(i).reloadTimer=0;
				}
				if (!troop.get(i).targetAcquired) {//if no target acquired
					if(troop.get(i).autoReloadTimer >troop.get(i).autoReloadThreashold&&troop.get(i).ammo<troop.get(i).maxAmmo)//automatically reload if you are waiting around with no target for long enough
					{
						troop.get(i).ammo = troop.get(i).maxAmmo;
						troop.get(i).autoReloadTimer=0;
					}
					else
					{
						troop.get(i).autoReloadTimer +=deltaTime;
					}
					returnObjects.clear();
					a.clear();
					quad.retrieve(returnObjects, troop.get(i).rangeRect,a);//check for potential targets
					for (int j = 0; j < returnObjects.size(); j++) {//for each potential target
						// Run collision detection algorithm between objects
						if (distanceBetween(troop.get(i).position, new PointF(returnObjects.get(j).centerX(),returnObjects.get(j).centerY())) < troop.get(i).closestEnemy) {//find the closest enemy out of all potential targets

							troop.get(i).target =a.get(j);
							troop.get(i).closestEnemy = distanceBetween(troop.get(i).position, new PointF(returnObjects.get(j).centerX(),returnObjects.get(j).centerY()));
							troop.get(i).targetAcquired = true;
						}
						if (true)//shotgun
						{

							if (distanceBetween(troop.get(i).position, new PointF(returnObjects.get(j).centerX(),returnObjects.get(j).centerY())) < troop.get(i).range*troop.get(i).range) //find all enemies in range
								{
									if (!troop.get(i).targets.contains(a.get(j))) {
										troop.get(i).targets.add(a.get(j));//the return objects list and the 'a' list are shared among all troops, it can be wiped and by one troop when the other was still using it, best to store a copy of it for each troop, like how we store the target variable
									}
								}
						}
					}
				}
				if (troop.get(i).targetAcquired){//if target IS acquired
					if (troop.get(i).ammo >0) {//if we have any bullets
						//if (troop.get(i).fireTimer < 0) {
						//troop.get(i).fireTimer = weapons.get(troop.get(i).weaponType).delayBetweenShots;

						//shotgun
						if(true) {//if we're using a shotgun
							//check angle between line drawn from primary target to the troop and line drawn from troop to
							// .east of the troop
							float primaryAngle = (float)Math.atan2(enemy.get(troop.get(i).target).position.y-troop.get(i).position.y,enemy.get(troop.get(i).target).position.x-troop.get(i).position.x);//atan2 calculates the angle between vector facing east from the origin and the given position, subtract the troop position to get the angle of the enemy from the troop
							//this will be used to store the angle between the primary angle and each enemy in range from the troop
							float enemyAngle = 0;

							firingList.clear();
							tempFiringList.clear();
							antiClockwise.clear();
							clockwise.clear();
							for(int j = 0; j < troop.get(i).targets.size(); j++) {							//check one cone of fire either side of the target
								if (enemy.get(troop.get(i).targets.get(j)).health>0&& distanceBetween(troop.get(i).position, new PointF(enemy.get(troop.get(i).targets.get(j)).position.x,enemy.get(troop.get(i).targets.get(j)).position.y)) < troop.get(i).range*troop.get(i).range) {//find objects in range
									enemyAngle= (float)Math.atan2(enemy.get(troop.get(i).targets.get(j)).position.y-troop.get(i).position.y,enemy.get(troop.get(i).targets.get(j)).position.x-troop.get(i).position.x);//work out the enemy's angle from east of the troop
									if (enemyAngle-primaryAngle<0&&enemyAngle-primaryAngle>=-cone)//if enemy is within 22.5 degree cone (0.3926991 radians) anticlockwise
									{
										if(!antiClockwise.contains(new PointF((float)troop.get(i).targets.get(j),enemyAngle))) {
											antiClockwise.add(new PointF((float) troop.get(i).targets.get(j), enemyAngle));//add the number of that enemy to the anticlockwise list and that enemy's angle from east
										}
									}
									else if(enemyAngle-primaryAngle>=0&&enemyAngle-primaryAngle<cone)//if enemy is within 22.5 degree cone (0.3926991 radians) clockwise
									{
										if(!clockwise.contains(new PointF((float)troop.get(i).targets.get(j), enemyAngle))) {
											clockwise.add(new PointF((float) troop.get(i).targets.get(j), enemyAngle));
										}
									}
								}
								else//if target is not in range
								{
									troop.get(i).targets.remove(j);
								}
							}
							//check which cone of fire has fewer targets, select this
							//for each target in the selected cone of fire...
							//... count targets in a cone of fire starting from this target, add all the targets from the best cone of fire to the target list
							if (antiClockwise.size()==0)
							{
								//fire at clockwise
								for (int o = 0;o<clockwise.size();o++)
								{
									firingList.add((int)clockwise.get(o).x);
								}
								//shoot at firing list
								{
									troop.get(i).bulletsThisFrame = deltaTime / weapons.get(troop.get(i).weaponType).delayBetweenShots + troop.get(i).fractionalBullets;//logic for handling weapons firing faster than the frame rate
									int wholeBulletsThisFrame = (int) troop.get(i).bulletsThisFrame;
									troop.get(i).fractionalBullets = troop.get(i).bulletsThisFrame - (float) wholeBulletsThisFrame;
									for (int j = wholeBulletsThisFrame; j > 0; j--) {//for eah bullet
										for(int k =0;k<firingList.size();k++){//hit each enemy in firing list
											if (enemy.get(firingList.get(k)).health>0) {
												troop.get(i).fire((int) enemy.get(firingList.get(k)).position.x, (int) enemy.get(firingList.get(k)).position.y);
												enemy.get(firingList.get(k)).hit(weapons.get(troop.get(i).weaponType).damage);
											}

										}
										troop.get(i).ammo--;
									}
									firingList.clear();
								}


								if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range * troop.get(i).range ||
										enemy.get(troop.get(i).target).health <= 0) {//check if target has exited range

									troop.get(i).targetAcquired = false;
									troop.get(i).closestEnemy = troop.get(i).range * troop.get(i).range;
								}

							}
							else if (clockwise.size()==0)
							{
								//fire at anti clockwise
								for (int o = 0;o<antiClockwise.size();o++)
								{
									firingList.add((int)antiClockwise.get(o).x);
								}
								//shoot at firing list
								{
									troop.get(i).bulletsThisFrame = deltaTime / weapons.get(troop.get(i).weaponType).delayBetweenShots + troop.get(i).fractionalBullets;//logic for handling weapons firing faster than the frame rate
									int wholeBulletsThisFrame = (int) troop.get(i).bulletsThisFrame;
									troop.get(i).fractionalBullets = troop.get(i).bulletsThisFrame - (float) wholeBulletsThisFrame;
									for (int j = wholeBulletsThisFrame; j > 0; j--) {//for eah bullet
										for(int k =0;k<firingList.size();k++){//hit each enemy in firing list
											if (enemy.get(firingList.get(k)).health>0) {
												troop.get(i).fire((int) enemy.get(firingList.get(k)).position.x, (int) enemy.get(firingList.get(k)).position.y);
												enemy.get(firingList.get(k)).hit(weapons.get(troop.get(i).weaponType).damage);
											}
										}
										troop.get(i).ammo--;
									}
									firingList.clear();
									tempFiringList.clear();
								}


								if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range * troop.get(i).range ||
										enemy.get(troop.get(i).target).health <= 0) {//check if target has exited range

									troop.get(i).targetAcquired = false;
									troop.get(i).closestEnemy = troop.get(i).range * troop.get(i).range;
								}
							}
							else if (antiClockwise.size()<=clockwise.size())//if anti clockwise list has fewer objects or the number of objects is the same as the other list then proceed
							{
								Collections.sort(antiClockwise, new Comparator<PointF>() {//sort the anticlockwise list by the y value, which is the angle of the enemy,
									@Override
									public int compare(PointF p1, PointF p2) {
										return Float.compare(p1.y,p2.y);
									}
								});

								Collections.sort(clockwise, new Comparator<PointF>() {//sort the clockwise list by the y value, which is the angle of the enemy,
									@Override
									public int compare(PointF p1, PointF p2) {
										return Float.compare(p1.y,p2.y);
									}
								});

								for (int k = antiClockwise.size()-1;k>0;k--)//for all the objects in this list - starting witht the last - most clockwise object
								{
									for (int l = 0; l < clockwise.size();l++)//for all the objects in the other list - starting with the first, anticlockwise most object
									{
										if(clockwise.get(l).y<antiClockwise.get(k).y+cone*2)//if the anlge of the object in the clockwise list is less than the angle of the object in the anticlockwise list plus the firing cone, then ...
										{//the enemy in the clockwise list (l) is in range, add it and all other more anticlockwise enemies in the clockwise list (between l=0 and l)to the firing list. also add the enemy from the anticlockwise list (k) and any enemies more clockwise than this enemy that are in the anticlockwise list (between k and k = anticlckwise.size)

											for(int m = l; m > 0;m--)//for all values equal to l and lower - for the current object and all more anticlockwise objects in the clockwise list
											{
												tempFiringList.add((int)clockwise.get(m).x);//add to firing list, remember the x value of the objects in clockwise list is the enemy number, make sure to cast it to int
											}
											for (int n = k;n<antiClockwise.size()-1;n++)//for all values eual to k and higher - for the current enemy in anticlockwise list and all objects more clockwise than it in the anticlockwise list
											{
												tempFiringList.add((int)antiClockwise.get(n).x); //add those to the same list,
											}
										}
										else//if the current object in the clockwise list is not in the cone then no point checking any of the other objects in the clockwise list as they are all more clockwise than the current one
										{
											break;
										}
									}
									if(tempFiringList.size()>firingList.size())//if the tempfiring list is biggest so far then set the firng list to match the temp firing list
									{
										firingList=tempFiringList;
										tempFiringList.clear();
									}
								}
							}
							else
							{
								//similar but for clockwise
								Collections.sort(antiClockwise, new Comparator<PointF>() {//sort the anticlockwise list by the y value, which is the angle of the enemy,
									@Override
									public int compare(PointF p1, PointF p2) {
										return Float.compare(p1.y,p2.y);
									}
								});

								Collections.sort(clockwise, new Comparator<PointF>() {//sort the clockwise list by the y value, which is the angle of the enemy,
									@Override
									public int compare(PointF p1, PointF p2) {
										return Float.compare(p1.y,p2.y);
									}
								});

								for (int k = 0;k<clockwise.size();k++)//for all the objects in this list - starting witht the first - most anticlockwise object
								{
									for (int l = antiClockwise.size()-1; l >0;l--)//for all the objects in the other list - starting with the last, clockwise most object
									{
										if(antiClockwise.get(l).y>clockwise.get(k).y-cone*2)//if the anlge of the object in the anticlockwise list is more than the angle of the object in the clockwise list minus the firing cone, then ...
										{//the enemy in the anticlockwise list (l) is in range, add it and all other more clockwise enemies in the anticlockwise list to the firing list. also add the enemy from the clockwise list (k) and any enemies more anticlockwise than this enemy that are in the clockwise list

											for(int m = l; m<antiClockwise.size()-1;m++)//for all values equal to l and hgher - for the current object and all more clockwise objects in the anticlockwise list
											{
												tempFiringList.add((int)antiClockwise.get(m).x);//add to firing list, remember the x value of the objects in anticlockwise list is the enemy number, make sure to cast it to int
											}
											for (int n = k;n>0;n--)//for all values equal to k and lower - for the current enemy in clockwise list and all objects more anticlockwise than it in the clockwise list
											{
												tempFiringList.add((int)clockwise.get(n).x); //add those to the same list,
											}
										}
										else//if the current object in the anticlockwise list is not in the cone then no point checking any of the other objects in the anticlockwise list as they are all more anticlockwise than the current one
										{
											break;
										}
									}
									if(tempFiringList.size()>firingList.size())//if the tempfiring list is biggest so far then set the firng list to match the temp firing list
									{
										firingList=tempFiringList;
									}
								}
							}
							//shoot at firing list
							{
								troop.get(i).bulletsThisFrame = deltaTime / weapons.get(troop.get(i).weaponType).delayBetweenShots + troop.get(i).fractionalBullets;//logic for handling weapons firing faster than the frame rate
								int wholeBulletsThisFrame = (int) troop.get(i).bulletsThisFrame;
								troop.get(i).fractionalBullets = troop.get(i).bulletsThisFrame - (float) wholeBulletsThisFrame;
								for (int j = wholeBulletsThisFrame; j > 0; j--) {//for eah bullet
									for(int k =0;k<firingList.size();k++){//hit each enemy in firing list
										if (enemy.get(firingList.get(k)).health>0) {
											troop.get(i).fire((int) enemy.get(firingList.get(k)).position.x, (int) enemy.get(firingList.get(k)).position.y);
											enemy.get(firingList.get(k)).hit(weapons.get(troop.get(i).weaponType).damage);
										}
									}
										troop.get(i).ammo--;//subtract ammo for each whole bullet
								}
								firingList.clear();//clear firing list after all whole bullets are deal with
								tempFiringList.clear();
							}


								if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range * troop.get(i).range ||
										enemy.get(troop.get(i).target).health <= 0) {//check if target has exited range

									troop.get(i).targetAcquired = false;
									troop.get(i).closestEnemy = troop.get(i).range * troop.get(i).range;
								}
						}

						//end of shotgun logic
						else
						{

							troop.get(i).bulletsThisFrame = deltaTime / weapons.get(troop.get(i).weaponType).delayBetweenShots + troop.get(i).fractionalBullets;//logic for handling weapons firing faster than the frame rate
							int wholeBulletsThisFrame = (int) troop.get(i).bulletsThisFrame;
							troop.get(i).fractionalBullets = troop.get(i).bulletsThisFrame - (float) wholeBulletsThisFrame;
							for (int j = wholeBulletsThisFrame; j > 0; j--) {//for eah bullet
								if (enemy.get(troop.get(i).target).alive) {//fire at the target
									troop.get(i).fire((int) enemy.get(troop.get(i).target).position.x, (int) enemy.get(troop.get(i).target).position.y);
									enemy.get(troop.get(i).target).hit(weapons.get(troop.get(i).weaponType).damage);
									troop.get(i).ammo--;
								}
							}

							//}
							if (distanceBetween(troop.get(i).position, enemy.get(troop.get(i).target).position) > troop.get(i).range * troop.get(i).range ||
									enemy.get(troop.get(i).target).health <= 0) {//check if target has exited range
								troop.get(i).targetAcquired = false;
								troop.get(i).closestEnemy = troop.get(i).range * troop.get(i).range;
							}
						}
					}
				}
			}
		}
	}

	private float distanceBetween(PointF p1,PointF p2)
	{
		float a,b;
		a =p1.x-p2.x;
		b =p1.y-p2.y;
		float c = a*a+b*b;
		return c;
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
		g.drawARGB(255, 153, 217, 234);// another way to draw a blue background

		command.paint(g, cameraDrag, zoomScale);
		enemyUpdate.paint(g, cameraDrag, zoomScale);
		quad.paint(g,cameraDrag,zoomScale);

		g.drawString(text, 10, 30, blackText);
		g.drawString(text1, 10, 60, blackText);
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
	{
		if(event.pointer==0)
		{
			finger1 = new PointF(event.x,event.y);
		}
	}
	public void cameraControl(TouchEvent event)
	{
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
	}
}