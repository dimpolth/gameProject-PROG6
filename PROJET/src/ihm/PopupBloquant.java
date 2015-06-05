package ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class PopupBloquant extends Popup {
	public PopupBloquant(Color c) {
		super();
		setBackground(c);
		setOpaque(false);
		addMouseListener(new MouseAdapter() {
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}