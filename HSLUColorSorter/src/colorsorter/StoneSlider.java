// Dieser Klasse steuert das Auflegen der Steine. Solange die globale Variable machinerunning
// auf true ist, werden Steine aufgelegt, dies muss in einem eigenen Thread geschehen da es ein
// simultan ablaufender Prozess ist. Daher die extra Klasse.

package colorsorter;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class StoneSlider extends Thread {

	public void run() {
		// Initialisiere den Motor, warte aber 5 Sekunden, bis auch der Scanner bereit ist.
		Machine.slider = new EV3LargeRegulatedMotor(MotorPort.D);
		Delay.msDelay(5000);
		
		// Solange der Thread nicht unterbrochen wird UND die Variable machinerunning auf true
		// ist werden alle 4 Sekunden Steine aufgelegt
		while (!Thread.currentThread().isInterrupted() && Machine.machinerunning == true) {
			//Geschwindigkeit auf 40 einstellen und Schieber "aufmachen"
			Machine.slider.setSpeed(40);
			Machine.slider.rotate(-85);
			 
			// Kurz abwarten und Schieber zurückschieben 
			Delay.msDelay(200);
			Machine.slider.rotate(85);
			
			// Für 4 Sekunden pausieren
			Delay.msDelay(4000);
		 }

		//Falls die Schleife unterbrochen wird, den Thread abbrechen
		Machine.slider.close();
		this.interrupt();	
	}
	
}
