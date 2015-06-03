package ihm;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Chargement extends JComponent implements ActionListener {
	private double facteur;
	private Image img;
	private boolean afficher;
	private Timer horloge;

	Chargement() {
		facteur = 0.0;
		try {
			img = ImageIO.read(getClass().getResource("/images/chargement.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		afficher = false;
		horloge = new Timer(10, this);
	}

	public void afficher() {
		afficher = true;
		horloge.start();
	}

	public void cacher() {
		afficher = false;
		horloge.stop();
	}

	public void paintComponent(Graphics g) {
		if (afficher) {
			Graphics2D gra = (Graphics2D) g;
			//TODO gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gra.rotate(facteur, getWidth() / 2, getHeight() / 2);
			gra.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		facteur = (double) System.currentTimeMillis() / 250;
		repaint();
	}
}
