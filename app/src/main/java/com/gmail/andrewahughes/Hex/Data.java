package com.gmail.andrewahughes.Hex;

import android.graphics.Point;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.gmail.andrewahughes.framework.FileIO;

public class Data {

    // Create variables that will hold the values you want to save in your game.
    // Default values:

    public static boolean soundEnabled = true;
    public static int[] highscores = new int[] { 1951, 800, 120, 75, 3};
    public static int currentLevel = 0;

    public static int startDelay;
    public static int noOfEnemies;
    public static int enemyType;
    public static int pathType;
    public static int delay;

    public static int initialTroopNo;
    public static int initialTroopRectX;
    public static int initialTroopRectY;
    public static int initialTroopRectX2;
    public static int initialTroopRectY2;

    public static int posX;
    public static int posY;

    public static List<Path> pathList = new ArrayList<Path>();
    public static int pathID;
    public static int pathSize;
    public static int x;
    public static int y;

    public static List<Enemy> enemies = new ArrayList<Enemy>();
    public static int enemyId;
    public static int enemyMaxHealth;
    public static int enemyArmour;
    public static int enemySpeed;
    public static int enemyColour;


    public static List<Troop> troops = new ArrayList<Troop>();//list of the troops to be used
    public static String troopId;
    public static int troopSpeed;
    public static int troopWeaponType;


    public static List<Weapon> weaponList = new ArrayList<Weapon>();


    public static List<EnemyType> enemyTypeList = new ArrayList<EnemyType>();


    public static int ammoType;
    public static int damage;
    public static int penetration;
    public static int splash;
    public static int cone;
    public static int delayBetweenShots;
    public static int ammo;
    public static int reload;
    public static int autoReload;
    public static int range;

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {

            // Writes a file called .savedata to the SD Card
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile("wave.txt")));

            out.write(Integer.toString(1));
            out.write("\n");
            out.write(Integer.toString(500));
            out.write("\n");
            out.write(Integer.toString(1));
            out.write("\n");
            //x pos
            out.write(Integer.toString(1000));
            out.write("\n");
            //y pos
            out.write(Integer.toString(300));
            out.write("\n");

            out.write(Integer.toString(1));
            out.write("\n");
            out.write(Integer.toString(500));
            out.write("\n");
            out.write(Integer.toString(1));
            out.write("\n");
            //x pos
            out.write(Integer.toString(1000));
            out.write("\n");
            //y pos
            out.write(Integer.toString(300));
            out.write("\n");
/*
            // Line by line ("\n" creates a new line), write the value of each variable to the file.
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");

            out.write(Integer.toString(currentLevel));
            out.write("\n");

            // Uses a for loop to save 5 numbers to the save file.
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }*/

            // This section handles errors in file management!

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            // Reads file called Save Data
            in = new BufferedReader(new InputStreamReader(
                    files.readFile("wave.txt")));

            // Loads values from the file and replaces default values.
            //soundEnabled = Boolean.parseBoolean(in.readLine());
            //currentLevel = Integer.parseInt(in.readLine());

            //startDelay = Integer.parseInt(in.readLine());
            //noOfEnemies = Integer.parseInt(in.readLine());
            enemies.clear();

            String line=in.readLine();
            initialTroopNo=Integer.parseInt(line);
            line =in.readLine();
            initialTroopRectX=Integer.parseInt(line);//left
            line =in.readLine();
            initialTroopRectY=Integer.parseInt(line);//top
            line =in.readLine();
            initialTroopRectX2=Integer.parseInt(line);//right
            line =in.readLine();
            initialTroopRectY2=Integer.parseInt(line);//bottom
            line =in.readLine();
            while(line!=null)
            {
                pathType=Integer.parseInt(line);
                line =in.readLine();
                delay=Integer.parseInt(line);
                line =in.readLine();
                enemyType=Integer.parseInt(line);


                posX=pathList.get(pathType).pointList.get(0).x;
                posY=pathList.get(pathType).pointList.get(0).y;
                enemies.add(new Enemy(pathType,delay,enemyType,posX,posY));
                //load path .txt and enemy.txt if necessary

                line =in.readLine();
            }
            // Uses for loop to load 5 numbers as high score.
            /*for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }*/

        } catch (IOException e) {
            // Catches errors. Default values are used.
        } catch (NumberFormatException e) {
            // Catches errors. Default values are used.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void loadPath(FileIO files) {
        BufferedReader in = null;
        try {
            // Reads file called Save Data
            in = new BufferedReader(new InputStreamReader(
                    files.readFile("path.txt")));

            // Loads values from the file and replaces default values.
            //soundEnabled = Boolean.parseBoolean(in.readLine());
            //currentLevel = Integer.parseInt(in.readLine());


            String line=in.readLine();
            int j=0;
            while(line!=null)
            {
                pathID = Integer.parseInt(line);
                line = in.readLine();
                pathSize = Integer.parseInt(line);
                line = in.readLine();
                pathList.add(new Path(pathID));

                for(int i=0;i<pathSize;i++) {

                        x = Integer.parseInt(line);
                        line = in.readLine();
                        y = Integer.parseInt(line);
                        pathList.get(j).pointList.add(new Point(x, y));
                        line = in.readLine();

                }
                j++;
            }
        } catch (IOException e) {
            // Catches errors. Default values are used.
        } catch (NumberFormatException e) {
            // Catches errors. Default values are used.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
    public static void loadEnemyType(FileIO files) {
        BufferedReader in = null;
        try {
            // Reads file called Save Data
            in = new BufferedReader(new InputStreamReader(
                    files.readFile("enemy.txt")));

            // Loads values from the file and replaces default values.
            //soundEnabled = Boolean.parseBoolean(in.readLine());
            //currentLevel = Integer.parseInt(in.readLine());


            String line=in.readLine();
            while(line!=null)
            {
                enemyId = Integer.parseInt(line);
                line = in.readLine();
                enemyMaxHealth = Integer.parseInt(line);
                line = in.readLine();
                enemyArmour = Integer.parseInt(line);
                line = in.readLine();
                enemySpeed = Integer.parseInt(line);
                line = in.readLine();
                enemyColour = Integer.parseInt(line);
                line = in.readLine();
                enemyTypeList.add(new EnemyType(enemyMaxHealth,enemyArmour, enemySpeed, enemyColour));

            }
        } catch (IOException e) {
            // Catches errors. Default values are used.
        } catch (NumberFormatException e) {
            // Catches errors. Default values are used.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }


    public static void loadTroop(FileIO files) {
        BufferedReader in = null;
        try {
            // Reads file called Save Data
            in = new BufferedReader(new InputStreamReader(
                    files.readFile("troop.txt")));

            // Loads values from the file and replaces default values.
            //soundEnabled = Boolean.parseBoolean(in.readLine());
            //currentLevel = Integer.parseInt(in.readLine());


            String line=in.readLine();
            while(line!=null)
            {
                troopId =line;
                line = in.readLine();
                troopSpeed = Integer.parseInt(line);
                line = in.readLine();
                troopWeaponType = Integer.parseInt(line);
                line = in.readLine();

                troops.add(new Troop(troopId,troopSpeed, troopWeaponType,weaponList.get(troopWeaponType).ammo,weaponList.get(troopWeaponType).reload,weaponList.get(troopWeaponType).autoReload,weaponList.get(troopWeaponType).delayBetweenShots,weaponList.get(troopWeaponType).range));

            }
        } catch (IOException e) {
            // Catches errors. Default values are used.
        } catch (NumberFormatException e) {
            // Catches errors. Default values are used.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void loadWeapon(FileIO files) {
        BufferedReader in = null;
        try {
            // Reads file called Save Data
            in = new BufferedReader(new InputStreamReader(
                    files.readFile("weapon.txt")));

            // Loads values from the file and replaces default values.
            //soundEnabled = Boolean.parseBoolean(in.readLine());
            //currentLevel = Integer.parseInt(in.readLine());


            String line=in.readLine();
            int j=0;
            while(line!=null)
            {

                ammoType = Integer.parseInt(line);//0 for normal bullet, 1 for shotgun,
                line = in.readLine();
                damage = Integer.parseInt(line);
                line = in.readLine();
                penetration = Integer.parseInt(line);
                line = in.readLine();
                splash = Integer.parseInt(line);//only works for explosive ammo types
                line = in.readLine();
                cone = Integer.parseInt(line);//only works if shotgun ammo type
                line = in.readLine();
                delayBetweenShots = Integer.parseInt(line);
                line = in.readLine();
                ammo = Integer.parseInt(line);
                line = in.readLine();
                reload = Integer.parseInt(line);
                line = in.readLine();
                autoReload = Integer.parseInt(line);
                line = in.readLine();
                range = Integer.parseInt(line);
                line = in.readLine();



                weaponList.add(new Weapon(ammoType,damage, penetration, splash,cone, delayBetweenShots, ammo,reload, autoReload,range));
            }
        } catch (IOException e) {
            // Catches errors. Default values are used.
        } catch (NumberFormatException e) {
            // Catches errors. Default values are used.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
    // Use this method to add 5 numbers to the high score.
    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highscores[i] < score) {
                for (int j = 4; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }
}
 
