package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {

    // songs
    public static int MAIN_MENU = 0;

    //effects
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int PUNCH = 2;
    public static int UPPERCUT = 3;
    public static int ROUNDKICK = 4;
    public static int SPINKICK = 5;
    public static int ADDPUNCH = 6;
    public static int GOTHIT = 7;

    public static int LEVEL_COMPLETE = 8;
    public static int GAMEOVER = 9;

    private Clip[] songs, sfx;
    private int currentSongID;
    private float volume = 1f;
    private boolean muteSong, muteEffect;

    private Random random = new Random(); // for randomizing attack sounds


    public AudioPlayer() {
        loadSongs();
        loadSoundEffects();
        playSong(MAIN_MENU);
    }

    private void loadSongs() {

        // songs are in audio/songs
        String[] names = {"menu", "level1", "level2", "level3", "turningjapanese", "japaneseteaceremony", "biginjapan"};
        songs = new Clip[names.length];


        for (int i = 0; i < songs.length ; i++) {
            songs[i] = getClip(names[i]);
        }

    }

    private void loadSoundEffects() {

        String[] names = {"die", "jump", "punch", "addpunch", "uppercut", "roundkick", "spinkick", "hiss", "levelcomplete", "gameover"};
        sfx = new Clip[names.length];

        for (int i = 0; i < sfx.length; i++) {
            sfx[i] = getClip(names[i]);
        }

        updateEffectsVolume();

    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateMusicVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongID].isActive())    // is a song being played right now? stop it
            songs[currentSongID].stop();
    }

    public void setSongForLevel(int levelIndex) {

        int randomSong = random.nextInt(1, songs.length);
//        if (levelIndex % 2 == 0)    // even levels have this song
//            playSong(LEVEL_2);
//        else
        playSong(randomSong);
    }


    public void playLevelCompleteSound() {
        stopSong();
        playSoundEffect(LEVEL_COMPLETE);
    }


    public void playRandomAttackSFX() {
        int start = 2;
        start += random.nextInt(4);
        playSoundEffect(start);
    }

    public void playSoundEffect(int effect) {
        sfx[effect].setMicrosecondPosition(0); // reset position to start from beginning of sound effect
        sfx[effect].start();
    }

    public void stopSoundEffects() {
        for (Clip sound : sfx) {
            sound.stop();
        }
    }

    public void playSong(int song) {

        // is a song being played right now? stop it
        stopSong();

        // update ID to the new songID and call volume update (it might have been changed before)
        currentSongID = song;
        updateMusicVolume();

        songs[currentSongID].setMicrosecondPosition(0);
        songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleMusicMute() {
        this.muteSong = !muteSong; // flip the value of mute (t/f)
        for (Clip clip : songs) {
            BooleanControl muteUnmute = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            muteUnmute.setValue(muteSong);
        }
    }

    public void toggleEffectsMute() {
        this.muteEffect = !muteEffect; // flip the value of mute (t/f)
        for (Clip clip : sfx) {
            BooleanControl muteUnmute = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            muteUnmute.setValue(muteEffect);
        }
        if (!muteEffect) {
            playSoundEffect(JUMP);
        }

    }

    private void updateMusicVolume() {

        FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
        // this is just a very complicated way of setting the new volume, gainControl doesn't have a min like 0 and a max like 1
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float newGainValue = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(newGainValue);
    }

    private void updateEffectsVolume() {
        // we wanna update all effects, not just 1
        for (Clip clip : sfx) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // this is just a very complicated way of setting the new volume, gainControl doesn't have a min like 0 and a max like 1
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float newGainValue = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(newGainValue);
        }
    }


}
