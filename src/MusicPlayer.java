package src;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code MusicPlayer} class manages all audios played throughout the game
 * including sound effects and background music.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Background music playback with looping</li>
 *   <li>Sound effect management with caching</li>
 *   <li>Independent volume control for music and SFX</li>
 *   <li>Audio resource loading from classpath</li>
 * </ul>
 *
 * <p>This class integrates {@link Clip} for audio playback and
 * {@link FloatControl} to adjust the audio.
 *
 * AI WAS USED IN ORDER TO HELP DEBUG + HELP LEARN MORE ABOUT THE CLASSES IT USES
 * @author Aya Abdulnabi
 * @author Kamaldeep Ghorta
 * @version 1.0
 */
public class MusicPlayer {
    /** Clip instance to for background music playback */
    private static Clip backgroundMusic;
    /** Flag to indicate if background music is playing or not */
    private static boolean isPlaying = false;
    /** Float used to adjust the volume of the background music */
    private static float volume = 0.1f;
    /** Float used to adjust the volume of sound effects*/
    private static float sfxVolume = 0.1f;
    /** Cache for loaded sounds and reloading */
    private static Map<String, Clip> soundEffects = new HashMap<>();

    /**
     * Plays background music from the specified file path, it stops
     * any currently playing music before starting new track.
     *
     * @param filePath Path to the audio file
     */
    public static void playBackgroundMusic(String filePath) {
        try {
            // Stop if already existing music is playing
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }

            // Load audio from the file path if found
            InputStream raw = MusicPlayer.class.getResourceAsStream("/" + filePath);
            if (raw == null) {
                System.out.println("Background music file not found: " + filePath);
                return;
            }
            // Set up audio stream and clip
            BufferedInputStream buffered = new BufferedInputStream(raw);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buffered);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);

            // Set the volume, continously loop and let it play
            setVolume(volume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Unable to play background music: " + e.getMessage());
        }
    }

    /**
     * Plays a sound effect from the specified file path
     *
     * @param filePath Path to the sound effect file
     *
     */
    public static void playSoundEffect(String filePath) {
        try {
            // Check to see if sound effect is already cached or not
            Clip clip = soundEffects.get(filePath);

            if (clip == null) {
                // Load new sound effect from the file path
                InputStream raw = MusicPlayer.class.getResourceAsStream("/" + filePath);
                if (raw == null) {
                    System.out.println("Sound not found: " + filePath);
                    return;
                }
                // Set up audio stream and clip
                BufferedInputStream buffered = new BufferedInputStream(raw);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buffered);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // Cache it for reuse
                soundEffects.put(filePath, clip);
            }

            // Play from beginning
            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            System.out.println("Error playing sound effect: " + filePath + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Sets the volume level for sound effects.
     * Between 0.0 (silent) and 0.5 (max).
     *
     * @param volumeLevel Desired volume level
     *
     */
    public static void setSfxVolume(float volumeLevel) {
        sfxVolume = Math.max(0.0f, Math.min(0.5f, volumeLevel));
    }

    /**
     * Sets the volume level for background music.
     * Between 0.0 (silent) and 1.0 (max)
     *
     * @param volumeLevel Desired volume level
     *
     */
    public static void setVolume(float volumeLevel) {
        volume = Math.max(0.0f, Math.min(1.0f, volumeLevel));
        if (backgroundMusic != null && backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    /**
     * Stops the currently playing background music.
     *
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            isPlaying = false;
        }
    }

    /**
     * Toggles background music playback state.
     * If playing, stops the music. If stopped, resumes playing
     *
     */
    public static void toggleBackgroundMusic() {
       // If playying, stop, otherwise, if not playing, let it play
        if (isPlaying) {
            stopBackgroundMusic();
        } else {
            if (backgroundMusic != null) {
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                isPlaying = true;
            }
        }
    }
}