package engine.audio;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioEngine {
	
	private static List<Integer> buffers = new ArrayList<Integer>();
	private static List<AudioSrc> sources = new ArrayList<AudioSrc>();
	
	private static List<AudioSrc> autoKills = new ArrayList<AudioSrc>();
	
	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static void addSource(AudioSrc source) {
		sources.add(source);
	}
	
	public static int loadSound(String file) {
		int buffer = 0;
		
		buffer = AL10.alGenBuffers(); 
		buffers.add(buffer);
		//ClassLoader load = Class.class.getClassLoader();
		//URL url = load.getResource("/res/" + file + ".wav");
		//WaveData wave = WaveData.create(Class.class.getResourceAsStream("/res/" + file + ".wav"));
		WaveData wave = WaveData.create(new BufferedInputStream(Class.class.getResourceAsStream("/res/" + file + ".wav")));
		//WaveData wave = WaveData.create(url);
		if (wave == null) {
			System.err.println("Not again, the WaveData create during sound loading is null");
		}
		AL10.alBufferData(buffer, wave.format, wave.data, wave.samplerate);
		wave.dispose();
		
		return buffer;
	}
	
	public static void cleanUp() {
		for (int el : buffers) {
			AL10.alDeleteBuffers(el);
		}
		for (AudioSrc el : sources) {
			el.delete();
		}
		AL.destroy();
	}
	
	
	// Are these efficient?
	public static void playTempSrc(int buffer, float vol) {
		AudioSrc src = new AudioSrc();
		autoKills.add(src);
		src.play(buffer, vol);
	}
	
	// alGenSources might be quite expensive
	public static void playTempSrc(int buffer, float vol, float x, float y, float z) {
		AudioSrc src = new AudioSrc();
		src.setPosition(x, y, z);
		autoKills.add(src);
		src.play(buffer, vol);
	}
	
	public static void update() {
		for (int i = 0; i < autoKills.size(); i++) {
			AudioSrc el = autoKills.get(i);
			if (!el.isPlaying()) {
				autoKills.remove(i);
				el.delete(); // VERY IMPORTANT! otherwise sources just pile up
			}
		}
		
	}

}
