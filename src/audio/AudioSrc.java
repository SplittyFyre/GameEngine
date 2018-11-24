package audio;

import org.lwjgl.openal.AL10;

public class AudioSrc {
	
	private int srcID;
	
	public float volume = 1;
	
	private int boundSound = -1;
	
	public void bind(int buffer) {
		this.boundSound = buffer;
	}
	
	public void unBind() {
		this.boundSound = -1;
	}
	
	public AudioSrc() {
		srcID = AL10.alGenSources();
		AudioEngine.addSource(this);
	}
	
	public void play(int buffer) {
		stop();
		AL10.alSourcei(srcID, AL10.AL_BUFFER, buffer);
		contPlaying();
	}
	
	public void play(int buffer, float vol) {
		setVolume(vol);
		stop();
		AL10.alSourcei(srcID, AL10.AL_BUFFER, buffer);
		contPlaying();
	}
	
	public void play() {
		stop();
		AL10.alSourcei(srcID, AL10.AL_BUFFER, boundSound);
		contPlaying();
	}
	
	public void delete() {
		stop();
		AL10.alDeleteSources(srcID);
	}
	
	public void pause() {
		AL10.alSourcePause(srcID);
	}
	
	public void contPlaying() {
		AL10.alSourcePlay(srcID);
	}
	
	public void stop() {
		AL10.alSourceStop(srcID);
	}
	
	//*********************************************
	
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(srcID, AL10.AL_VELOCITY, x, y, z);
	}
	
	public void setLooping(boolean loop) {
		AL10.alSourcei(srcID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(srcID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setVolume(float vol) {
		this.volume = vol;
		AL10.alSourcef(srcID, AL10.AL_GAIN, vol);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(srcID, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(srcID, AL10.AL_POSITION, x, y, z);
	}

}
