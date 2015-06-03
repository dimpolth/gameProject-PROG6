package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PopupVictoire extends JPanel implements ActionListener {
	private Timer t;
	private long precedentLancement;
	private LinkedList<Fusee> fusees;
	private Random aleat;
	
	public PopupVictoire() {
		super();
		t = new Timer(10, this);
		precedentLancement = 0;
		fusees = new LinkedList<Fusee>();
		aleat = new Random();
	}
	
	public void lancer() {
		setVisible(true);
		repaint();
		//tirer();
		t.start();
	}
	
	public void arreter() {
		setVisible(false);
		t.stop();
	}
	
	private void tirer() {
		precedentLancement = System.currentTimeMillis();
		for(int i=0 ; i<aleat.nextInt(3)+1 ; i++) {
			fusees.add(new Fusee(new Point(aleat.nextInt(getWidth()),getHeight()), aleat.nextInt(3000)+1000, aleat.nextInt(100)+50));
		}
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}

class Fusee {
	public Fusee(Point c, long t, int n) {
		coord = c;
		tempsExplosion = t;
		nombreParticule = n;
	}
	public Point coord;
	public long tempsExplosion;
	public int nombreParticule;
}