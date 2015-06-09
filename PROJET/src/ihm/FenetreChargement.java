package ihm;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FenetreChargement extends JFrame {
	private Image img;
	private Chargement chargement;

	public FenetreChargement() {
		setUndecorated(true);
		try {
			setIconImage(ImageIO.read(getClass().getResource("/images/icone.png")));
			img = ImageIO.read(getClass().getResource("/images/fenetreChargement.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JPanel pan = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			}
		};
		add(pan);
		setResizable(false);
		setSize(384, 256);
		setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getHeight() / 2);
		chargement = new Chargement();
		pan.add(chargement);
		chargement.afficher();
		setVisible(true);
		chargement.setLocation(217, 185);
	}
}
