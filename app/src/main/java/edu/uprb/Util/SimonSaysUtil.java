package edu.uprb.Util;

import java.util.Random;

/**
 * Created by Fluffy on 12/8/2018.
 */

public class SimonSaysUtil {

    private SimonSaysUtil(){ }

    public static int[] generateCombination(int playerCtr){

        Random random = new Random();

        int[] combination = new int[playerCtr];

        for(int i = 0; i < combination.length; i++){
            combination[i] = random.nextInt(playerCtr + 1);
        }

        return combination;
    }
}
