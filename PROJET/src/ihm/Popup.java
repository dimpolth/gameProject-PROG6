package ihm;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Popup extends JPanel {
	public Image img;
	
	public Popup() {
		super();
		try {
			img = ImageIO.read(getClass().getResource("/images/themes/bois/fondMenu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
	}
	public Popup(LayoutManager l) {
		super(l);
	}
	
	public void setImage(Image i) {
		img = i;
	}
}
