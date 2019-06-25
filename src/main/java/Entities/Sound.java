package Entities;

import Engine.FileLoader;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound implements FileLoader,Runnable {
    private AudioInputStream wavSound;
    private float volumeDown;
    private int waitTime;


    public Sound(String name,float volume, int wait) {
        String pathFile = generateAbsolutePath("/src/main/models/sounds/",name);

        try {
            wavSound = AudioSystem.getAudioInputStream(new File(pathFile));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        volumeDown = volume;
        waitTime = wait;
    }


    public void playWAV(){
        Clip clip = null;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }


        try {
            clip.open(wavSound);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-volumeDown); // Reduce volume by 10 decibels.
        clip.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        playWAV();
    }
}