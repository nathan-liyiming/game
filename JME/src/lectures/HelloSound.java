package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class HelloSound extends SimpleGame {
	AudioSystem audio;
	AudioTrack mySound;
	AudioTrack mySound2;
	
	protected void simpleInitGame() {
        KeyBindingManager.getKeyBindingManager().set("PLAY", KeyInput.KEY_SPACE);
		audio = AudioSystem.getSystem();
		mySound = audio.createAudioTrack("/lectures/data/sound/laser.ogg", false);
		mySound2 = audio.createAudioTrack("/lectures/data/sound/test.ogg", false);
		MusicTrackQueue queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.ONE);
		queue.addTrack(mySound2);
		queue.play();

	}
	
	protected void simpleUpdate() {
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("PLAY", false)) {
			mySound.play();
		}
		AudioSystem.getSystem().update();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloSound app = new HelloSound();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
