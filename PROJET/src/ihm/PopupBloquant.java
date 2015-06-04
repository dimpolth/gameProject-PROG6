package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class PopupBloquant extends JPanel {
	public PopupBloquant(Color c) {
		super();
		setBackground(c);
		setOpaque(false);
		addMouseListener(new MouseAdapter() {
		});
	}

	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}