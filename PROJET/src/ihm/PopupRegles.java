package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
class PopupRegles extends Popup {
	public PopupRegles(IHM i) {
		super(new BorderLayout());

		JEditorPane regles = new JEditorPane();
		regles.setAutoscrolls(true);
		regles.setContentType("text/html");
		regles.setEditable(false);
		regles.setText(lireFichier_Regle());

		JScrollPane scrollPane = new JScrollPane(regles);
		regles.setSize(new Dimension(0, 1000));
		scrollPane.setBorder(new EmptyBorder(25, 25, 0, 25));
		scrollPane.setOpaque(false);
		add(scrollPane, BorderLayout.CENTER);

		JPanel pBouton = new JPanel();
		Bouton plus = new Bouton("pdf externe");
		plus.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_PLUS, i));
		pBouton.add(plus);
		Bouton retour = new Bouton("Retour");
		retour.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_RETOUR, i));
		pBouton.add(retour);
		pBouton.setOpaque(false);
		add(pBouton, BorderLayout.SOUTH);
	}

	public String lireFichier_Regle() {
		String chaine = "";
		try {
			InputStreamReader ipsr = new InputStreamReader(getClass().getResource("/documents/regles.html").openStream(), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				chaine += ligne + "\n";
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return chaine;
	}
}
