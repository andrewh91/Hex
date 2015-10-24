package com.gmail.andrewahughes.TroopTD;

import android.graphics.Point;
import android.util.Log;

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

    public static int posX;
    public static int posY;

    public static List<Path> pathList = new ArrayList<Path>();
    public static int pathID;
    public static int pathSize;
    public static int x;
    public static int y;

    public static List<Enemy> enemies = new ArrayList<Enemy>();
    public static int enemyMaxHealth;
    public static int enemySpeed;
    public static int enemyDamageModel;



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
            out.write(Integer.toString(1000));
            out.write("\n");
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
            int i = 0;

            String line=in.readLine();
            while(line!=null)
            {
                pathType=Integer.parseInt(line);
                line =in.readLine();
                delay=Integer.parseInt(line);
                line =in.readLine();
                enemyType=Integer.parseInt(line);
                line =in.readLine();


                posX=Integer.parseInt(line);
                line =in.readLine();
                posY=Integer.parseInt(line);
              //  line =in.readLine();
                enemies.add(new Enemy(pathType,delay,enemyType,posX,posY));
                //load path .txt and enemy.txt if necessary
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
                j++;

                for(int i=0;i>pathSize;i++) {
                    while (line != null) {
                        x = Integer.parseInt(line);
                        line = in.readLine();
                        y = Integer.parseInt(line);
                        line = in.readLine();
                        pathList.get(j).pointList.add(new Point(x, y));
                    }
                }
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


            List<EnemyType> enemyTypeList = new ArrayList<EnemyType>();
            String line=in.readLine();
            while(line!=null)
            {
                enemyMaxHealth = Integer.parseInt(line);
                line = in.readLine();
                enemySpeed = Integer.parseInt(line);
                line = in.readLine();
                enemyDamageModel = Integer.parseInt(line);
                line = in.readLine();
                enemyTypeList.add(new EnemyType(enemyMaxHealth, enemySpeed, enemyDamageModel));

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
 
