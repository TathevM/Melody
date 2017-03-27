package com.team_red.melody.filemanager;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MPEG {

    private long mLength;  // in seconds
    private long mAudioBytes;
    private long mDileSize;

    private String mFilename;
    private String mVersion;
    private String mLayer;
    private boolean mProtection;
    private String mBitrate;
    private String mFrequency;
    private boolean mPadding;
    private boolean mPrivate;
    private String mChannelMode;
    private String mModeExtension;
    private boolean mCopyRight;
    private boolean mOriginal;
    private String mEmphasis;
    private int mFrameSize;

    private int mHeaderPosition;

    private byte[] br;

    public MPEG(String filename) {
        initComponents();
        mFilename = filename;
    }

    public MPEG() {
        this("");
    }

    private void initComponents(){
        this.mAudioBytes = 0;
        this.mFilename = "";
        this.mVersion = "";
        this.mLayer = "";
        this.mProtection = false;
        this.mBitrate = "";
        this.mFrequency = "";
        this.mPadding = false;
        this.mPrivate = false;
        this.mChannelMode = "";
        this.mModeExtension = "";
        this.mCopyRight = false;
        this.mOriginal = false;
        this.mEmphasis = "";
        this.mHeaderPosition = 0;
        this.mFrameSize = 0;
    }

    public int calculateFrameSize(){
        if(mFrameSize == 0)
            this.mFrameSize = (144 * 160000) / (44100);
        return mFrameSize;
    }

    public int getHeaderPosition() {
        return mHeaderPosition;
    }

    public void read(File f){
        try {
            //File f = new File(COMPOSITION_JSON_DIR + mFilename);
            br = new byte[(int) f.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(br);

            dis.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] headerBytes = findHeader();
    }

    private byte[] findHeader(){
        int position = 0;
        byte thisByte = br[position];
        while (position < br.length)
        {
            if(thisByte == -1){
                boolean[] thatByte = BitReader.toBitBool(br[position + 1]);
                if(thatByte[0] && thatByte[1] && thatByte[2]){
                    //we found the sync
                    this.mHeaderPosition = position -1;
                    byte[] retByte = new byte[4];
                    retByte[0] = thisByte;
                    retByte[1] = br[position + 1];
                    retByte[2] = br[position + 2];
                    retByte[3] = br[position + 3];
                    return retByte;
                }
                else {
                    position ++;
                    thisByte = br[position];
                }
            }
            else {
                position++;
                thisByte = br[position];
            }
        }
        return null;
    }
}
