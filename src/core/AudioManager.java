/*
 * @author Thad Benjaponpitak
 */
package core;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
	public static final double VOLUME = 0.5;
	public static final Media LOST_SIGNAL = new Media(ClassLoader.getSystemResource("lost_signal.mp3").toString());
	public static final Media DEAR_DIARY = new Media(ClassLoader.getSystemResource("dear_diary.mp3").toString());
	public static final AudioClip DOOR_OPEN = new AudioClip(ClassLoader.getSystemResource("043.wav").toString());
	public static final AudioClip DOOR_CLOSE = new AudioClip(ClassLoader.getSystemResource("019.wav").toString());
	public static final AudioClip ANCHOR = new AudioClip(ClassLoader.getSystemResource("046.wav").toString());
	public static final AudioClip CHRONE_CREATE = new AudioClip(ClassLoader.getSystemResource("052.wav").toString());
	public static final AudioClip CHRONE_REMOVE = new AudioClip(ClassLoader.getSystemResource("053.wav").toString());
	public static final AudioClip INFO_BLOCK = new AudioClip(ClassLoader.getSystemResource("m_cancel.wav").toString());
	public static final AudioClip GOAL_BLOCK = new AudioClip(ClassLoader.getSystemResource("m_pass.wav").toString());

	private static AudioManager instance = new AudioManager();
	private Set<AudioClip> audioClips;
	private MediaPlayer bgm;

	private AudioManager() {
		audioClips = new HashSet<>();
	}

	public static AudioManager getInstance() {
		return instance;
	}

	public void uniquePlay(AudioClip audioClip) {
		uniquePlay(audioClip, AudioManager.VOLUME);
	}

	public void uniquePlay(AudioClip audioClip, double volume) {
		if (!contains(audioClip)) {
			add(audioClip);
			audioClip.play(volume);
		}
	}

	public boolean contains(AudioClip audioClip) {
		return audioClips.contains(audioClip);
	}

	public void add(AudioClip audioClip) {
		audioClips.add(audioClip);
	}

	public void remove(AudioClip audioClip) {
		audioClips.remove(audioClip);
	}

	public void playBgm(Media media) {
		stopBgm();
		bgm = new MediaPlayer(media);
		bgm.setCycleCount(MediaPlayer.INDEFINITE);
		bgm.play();
	}

	public void stopBgm() {
		if (bgm != null && bgm.getStatus() == MediaPlayer.Status.PLAYING)
			bgm.stop();
	}
}
