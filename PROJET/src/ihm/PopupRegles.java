package ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


@SuppressWarnings("serial")
class PopupRegles extends Popup {
	public PopupRegles(IHM i) {
		super(new GridBagLayout());

		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx = 0;

		contraintes.weightx = 2;
		contraintes.weighty = 1;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.insets = new Insets(50, 50, 50, 50);

		JEditorPane regles = new JEditorPane();		
		regles.setAutoscrolls(true);
		regles.setContentType("text/html");
		regles.setEditable(false);
		regles.setText("<html><h1>Les règles du Fanorona</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum felis accumsan quis. Suspendisse potenti. Morbi pharetra purus vitae blandit vehicula. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin elit dui, consequat tristique dictum ac, vulputate congue felis. Cras consequat augue nec suscipit maximus.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum felis accumsan quis. Suspendisse potenti. Morbi pharetra purus vitae blandit vehicula. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin elit dui, consequat tristique dictum ac, vulputate congue felis. Cras consequat augue nec suscipit maxim Etiam fringilla erat lacinia sem tincidunt gravida. Vestibulum porttitor orci ut ante eleifend, tincidunt molestie elit ornare. Suspendisse placerat neque odio, a posuere quam congue non. Nulla diam orci, lobortis ut orci et, interdum malesuada arcu. Vestibulum porttitor vehicula urna, et ornare mi eleifend eget. In consequat congue eros eget volutpat. Proin quis rhoncus velit.</p></html>");
		JScrollPane scrollPane = new JScrollPane(regles);	
		regles.setSize(new Dimension(0,1000));
		add(scrollPane, contraintes);
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.ipadx = 100;
		contraintes.ipady = 40;

		Bouton retour = new Bouton("Retour au menu");
		retour.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_RETOUR, i));
		add(retour, contraintes);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}