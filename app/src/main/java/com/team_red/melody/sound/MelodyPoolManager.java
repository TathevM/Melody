package com.team_red.melody.sound;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;

import java.util.ArrayList;

public class MelodyPoolManager {

    private static MelodyPoolManager instance;
    private ArrayList<Integer> sounds1;
    private ArrayList<Integer> sounds2;
    private SoundPool soundPool;
    private ArrayList<SoundSampleEntity> loadedSounds1;
    private ArrayList<SoundSampleEntity> loadedSounds2;
    private boolean isPlaySound;

    private Handler h;
    private int handlerCounter;
    private int delay = 300;
    private Runnable mRunnable;

    public boolean isPlaySound() {
        return isPlaySound;
    }

    public void setPlaySound(boolean playSound) {
        isPlaySound = playSound;
    }

    public synchronized static MelodyPoolManager getInstance() {
        return instance;
    }

    public static void CreateInstance() {
        if (instance == null) {
            instance = new MelodyPoolManager();
        }
    }

    public ArrayList<Integer> getSounds2() {
        return sounds2;
    }

    public void setSounds2(ArrayList<Integer> sounds2) {
        this.sounds2 = sounds2;
    }

    public ArrayList<Integer> getSounds1() {
        return sounds1;
    }

    public void setSounds1(ArrayList<Integer> sounds1) {
        this.sounds1 = sounds1;
    }

    public void InitializeMelodyPool(Activity activity, final IMelodyPoolLoaded callback) throws Exception {
        if (sounds1 == null || sounds1.size() == 0) {
            throw new Exception("Sounds not set");
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        }else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                SoundSampleEntity entity = getEntity(sampleId);
                if (entity != null) {
                    entity.setLoaded(status == 0);
                }

                if (sampleId == loadedSounds1.size()) {
                    callback.onSuccess();
                }
            }
        });
        int length = sounds1.size();
        loadedSounds1 = new ArrayList<>();
        int index;
        for (index = 0; index < length; index++) {
            loadedSounds1.add(index , new SoundSampleEntity(0, false));
        }
        index = 0;
        for (SoundSampleEntity entry : loadedSounds1) {
            entry.setSampleId(soundPool.load(activity , sounds1.get(index) , 1 ));
            index++;
        }
    }

    public interface IMelodyPoolLoaded {
        void onSuccess();
    }

    public interface IMelodyPoolPlaybackFinished{
        void onFinishPlayBack();
    }

//    private int maxSampleId() {
//        int sampleId = 0;
//        for (SoundSampleEntity entry : loadedSounds1) {
//            sampleId = entry.getSampleId() > sampleId ? entry.getSampleId() : sampleId;
//        }
//        return sampleId;
//    }

    private SoundSampleEntity getEntity(int sampleId) {
        for (SoundSampleEntity entry : loadedSounds1) {
            if (entry.getSampleId() == sampleId) {
                return entry;
            }
        }
        return null;
    }

//    public void playSound(int resourceId) {
//        if (isPlaySound()) {
//            SoundSampleEntity entity = hashMap.get(resourceId);
//            if (entity.getSampleId() > 0 && entity.isLoaded()) {
//                soundPool.play(entity.getSampleId(), .99f, .99f, 1, 0, 1f);
//            }
//        }
//    }

    public void playMelody(){
        h = new Handler();
        handlerCounter = 0;
        h.post(new Runnable() {
            @Override
            public void run() {
                if (isPlaySound() && handlerCounter < loadedSounds1.size()){
                    SoundSampleEntity entity = loadedSounds1.get(handlerCounter);
                    if (entity.getSampleId() > 0 && entity.isLoaded()) {
                        soundPool.play(entity.getSampleId(), .99f, .99f, 1, 0, 1f);
                    }
                    mRunnable = this;
                    h.postDelayed(mRunnable , delay);
                    handlerCounter++;
                }
                else {
                    stop();
                    release();
                }
            }
        });
    }

    public void playMelody(final IMelodyPoolPlaybackFinished callback){
        h = new Handler();
        handlerCounter = 0;
        h.post(new Runnable() {
            @Override
            public void run() {
                if (isPlaySound() && handlerCounter < loadedSounds1.size()){
                    SoundSampleEntity entity = loadedSounds1.get(handlerCounter);
                    if (entity.getSampleId() > 0 && entity.isLoaded()) {
                        soundPool.play(entity.getSampleId(), .99f, .99f, 1, 0, 1f);
                    }
                    mRunnable = this;
                    h.postDelayed(mRunnable , delay);
                    handlerCounter++;
                }
                else {
                    stop();
                    release();
                    callback.onFinishPlayBack();
                }
            }
        });
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
        }
    }

    public void stop() {
        if (soundPool != null) {
            for (SoundSampleEntity entry : loadedSounds1) {
                soundPool.stop(entry.getSampleId());
            }
            isPlaySound = false;
            handlerCounter = 0;
        }
    }

    private class SoundSampleEntity {
        private int sampleId;
        private boolean isLoaded;

        SoundSampleEntity(int sampleId, boolean isLoaded) {
            this.isLoaded = isLoaded;
            this.sampleId = sampleId;
        }

        int getSampleId() {
            return sampleId;
        }

        void setSampleId(int sampleId) {
            this.sampleId = sampleId;
        }

        boolean isLoaded() {
            return isLoaded;
        }

        void setLoaded(boolean isLoaded) {
            this.isLoaded = isLoaded;
        }
    }
}
