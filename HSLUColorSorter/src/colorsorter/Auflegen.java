//Dieser Klasse steuert das Auflegen der Steine. Solange die globale Variable machinerunning
//auf true ist, werden Steine aufgelegt.

package colorsorter;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Auflegen extends Thread {

	
	
	public void run() {
		Sortierer.slider = new EV3LargeRegulatedMotor(MotorPort.D);
		//System.out.println("Steine auflegen...");
		Delay.msDelay(5000);
			while (!Thread.currentThread().isInterrupted() && Sortierer.machinerunning == true) {
				//Schieber vorschieben
				Sortierer.slider.setSpeed(40);
				Sortierer.slider.rotate(-85);
				//Kurz abwarten
				Delay.msDelay(200);
				//Schieber zurückschieben
				Sortierer.slider.rotate(85);
				//Für 4 Sekunden pausieren
				Delay.msDelay(4000);
			 }
			//Sortierer.slider.close();
	System.out.println("Auflegen Thread vorbei.");
	Sortierer.slider.close();
	this.interrupt();	
	}
	
}
