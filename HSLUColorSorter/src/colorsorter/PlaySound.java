package colorsorter;

import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class PlaySound extends Thread {
	static Sound so1;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	static Audio audio;
	
	public void run(Brick brick, int colorid) {
		System.out.println("Spiele Sound...");
		audio = brick.getAudio();
		if (colorid == 1) {
			audio.playSample(new File("yellow.wav"), 100);
		}
		else if (colorid == 2) {
			audio.playSample(new File("red.wav"), 100);
		}
		else if (colorid == 3) {
			audio.playSample(new File("blue.wav"), 100);
		}
		else if (colorid == 4) {
			audio.playSample(new File("green.wav"), 100);
		}
		this.stop();
		this.interrupt();
		System.out.println("Abgespielt...");
	}
	
}
