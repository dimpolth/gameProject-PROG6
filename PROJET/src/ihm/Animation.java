package ihm;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Animation implements ActionListener {
	protected Pion pion;
	protected long tempsDepart;
	protected Timer horloge;
}
