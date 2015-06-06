package ihm;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Popup extends JPanel {
	public Image img;

	public Popup() {
		super();
	}

	public Popup(LayoutManager l) {
		super(l);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		if (img == null) {

		}
	}

	public void setImage(Image i) {
		img = i;
	}
}
