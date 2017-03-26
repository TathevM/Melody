package com.team_red.melody.filemanager;

/**
 * Created by Havayi on 23-Mar-17.
 */

class BitReader {

    static boolean[] toBitBool(byte newByte){

        // get the value of hte byte


        // make a bool array for the bits.
        // 0 == falseu
        // 1 == true; duh
        boolean[] myByte = new boolean[8];
        for (int i = 0; i < 8; i++)
        {
            // ANDing against each bit to set the bool value
            if ((newByte & (1 << (7 - i))) != 0)
            {
                myByte[i] = true;
            }
        }
        return myByte;
    }
}
