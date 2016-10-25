package com.gmail.andrewahughes.Hex;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by andy on 20/10/2016.
 */
public class SymbolsDB {
    List<int[]> setOf6Symbols = new ArrayList<int[]>();//holds a list of 31 symbols, never make changes to this list

    //holds a list of 32 numbers from 0 - 31, the first 31 numbers will refer to the list position of values in the setOf6Symbols list, 32nd number will be used to indicate the ehx has no symbol
    List<Integer> listPos1 = new ArrayList<Integer>();

    //this will be an empty list, but values from listPos1 will be moved here, this will help distribute symbols fairly.
    List<Integer> listPos2 = new ArrayList<Integer>();
    public SymbolsDB() {
    //hardcode 31 sets of 6 numbers, these will determine which symbols to use on a hex
        setOf6Symbols.add(new int[]{1,	2,	3,	4,	5,	26});
        setOf6Symbols.add(new int[]{6,	7,	8,	9,	10,	26});
        setOf6Symbols.add(new int[]{11,12,	13,	14,	15,	26});
        setOf6Symbols.add(new int[]{16,17,	18,	19,	20,	26});
        setOf6Symbols.add(new int[]{21,22,	23,	24,	25,	26});
        setOf6Symbols.add(new int[]{1,	7,	13,	19,	25,	27});
        setOf6Symbols.add(new int[]{5,	6,	12,	18,	24,	27});
        setOf6Symbols.add(new int[]{4,	10,	11,	17,	23,	27});
        setOf6Symbols.add(new int[]{3,	9,	15,	16,	22,	27});
        setOf6Symbols.add(new int[]{2,	8,	14,	20,	21,	27});
        setOf6Symbols.add(new int[]{1,	9,	12,	20,	23,	28});
        setOf6Symbols.add(new int[]{3,	6,	14,	17,	25,	28});
        setOf6Symbols.add(new int[]{5,	8,	11,	19,	22,	28});
        setOf6Symbols.add(new int[]{2,	10,	13,	16,	24,	28});
        setOf6Symbols.add(new int[]{4,	7,	15,	18,	21,	28});
        setOf6Symbols.add(new int[]{1,	8,	15,	17,	24,	29});
        setOf6Symbols.add(new int[]{4,	6,	13,	20,	22,	29});
        setOf6Symbols.add(new int[]{2,	9,	11,	18,	25,	29});
        setOf6Symbols.add(new int[]{5,	7,	14,	16,	23,	29});
        setOf6Symbols.add(new int[]{3,	10,	12,	19,	21,	29});
        setOf6Symbols.add(new int[]{1,	10,	14,	18,	22,	30});
        setOf6Symbols.add(new int[]{2,	6,	15,	19,	23,	30});
        setOf6Symbols.add(new int[]{3,	7,	11,	20,	24,	30});
        setOf6Symbols.add(new int[]{4,	8,	12,	16,	25,	30});
        setOf6Symbols.add(new int[]{5,	9,	13,	17,	21,	30});
        setOf6Symbols.add(new int[]{1,	6,	11,	16,	21,	31});
        setOf6Symbols.add(new int[]{2,	7,	12,	17,	22,	31});
        setOf6Symbols.add(new int[]{3,	8,	13,	18,	23,	31});
        setOf6Symbols.add(new int[]{4,	9,	14,	19,	24,	31});
        setOf6Symbols.add(new int[]{5,	10,	15,	20,	25,	31});
        setOf6Symbols.add(new int[]{26,27,	28,	29,	30,	31});

        for (int i =0;i<31;i++)
        {
            listPos1.add(i);//add the numbers 1 to 30 to the first list,
        }
    }
    public int[] getSetOf6Symbols(int i)//using the setId from the method below, we can return a set of 6 symbols
    {
        return setOf6Symbols.get(i);
    }

    public int getSetID(List<Integer> excludedSets )
    {
        Random random = new Random();
        int setID;
        //temporarily remove the numbers defined in excludedSets, excluded sets might include the value 31 which will suggest ...
        //...one of the hexes doesn't have a set id, the listPos does not have this value but this should not cause an issue
        listPos1.removeAll(excludedSets);
        if(listPos1.size()>0)//if the list has at least one option
        {
            int j = random.nextInt(listPos1.size());//get a random number between (inclusive)0 and (exclusive)however many values are in the list
            setID = listPos1.get(j);//get the value of a random number in the first list.
            listPos1.remove(Integer.valueOf(setID));//remove that value from the list... (you need to use Integer.valueOf(setID) in case it gets confused between the index and the value of the element)
            listPos2.add(Integer.valueOf(setID));//... and add it to the second list
        }
        //if so many symbols are excluded so that the list is now completely empty (apart from the value '31'), then use the other list instead
        else
        {
            listPos2.removeAll(excludedSets);//temporarily remove the numbers defined in excludedSets from the second list
            //if(listPos2.size()>0)//if the list has at least one option ... it must have at least one option if the first list was not big enough
            {
                int j = random.nextInt(listPos2.size());//get a random number between (inclusive)0 and (exclusive)however many values are in the list minus 1
                setID = listPos2.get(j);//get the value of a random number in the first list.
                listPos2.remove(Integer.valueOf(setID));//remove that value from the list... (you need to use Integer.valueOf(setID) in case it gets confused between the index and the value of the element)
                listPos1.add(Integer.valueOf(setID));//... and add it to the first list
            }
            for (int k = 0; k<excludedSets.size();k++)//make sure we fill the first list up again, but only the numbers below 31
            {
                if (excludedSets.get(k)<31)
                {
                    listPos2.add(excludedSets.get(k));
                    listPos2.removeAll(listPos1);
                }
            }
        }
        //make sure we fill the first list up again, this should happen regardless of whether we ended up using the first list or not
        for (int l = 0; l<excludedSets.size();l++)//make sure we fill the first list up again, but only the numbers below 31
        {
            if (excludedSets.get(l)<31)//don't add the number 31 to the list
            {//the intention is to add the numbers that we excluded temporarily back into the list
                listPos1.add(excludedSets.get(l));//we might add numbers back in that were moved to the other list because they've been used before
                listPos1.removeAll(listPos2);//the other ist holds a record of which numbers we don't want in the list, so remove all the values of that
            }
        }
        return setID;// the set id will be a number from 0 to 30 with which we can look up 6 symbols ids
    }
}
