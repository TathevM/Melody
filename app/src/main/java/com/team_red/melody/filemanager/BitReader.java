package com.team_red.melody.filemanager;

class BitReader {

    static boolean[] toBitBool(byte newByte){

        // get the value of the byte


        // make a bool array for the bits.
        // 0 == false
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
