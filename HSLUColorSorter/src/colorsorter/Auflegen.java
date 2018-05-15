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

	RegulatedMotor m4 = new EV3LargeRegulatedMotor(MotorPort.D);
	
	public void run() {
		//System.out.println("Steine auflegen...");
		Delay.msDelay(5000);
		while (!Thread.currentThread().isInterrupted()) {
			while (Sortierer.machinerunning==true) {
				//System.out.println("Stein auflegen.");
				//Schieber vorschieben
				m4.setSpeed(40);
				m4.rotate(-85);
				//Kurz abwarten
				Delay.msDelay(150);
				//Schieber zurückschieben
				m4.rotate(85);
				//Für 4 Sekunden pausieren
				Delay.msDelay(4000);
			 }
			try {
				//Für 1 Sekunde pausieren.
				Thread.sleep(1000);
				}
			catch (InterruptedException intExc) {
				this.interrupt();
				break;
			}
		}
		m4.close();
	}
	
}
