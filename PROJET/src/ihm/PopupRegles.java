package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


@SuppressWarnings("serial")
class PopupRegles extends JPanel {
	public PopupRegles(IHM i) {
		super(new BorderLayout());

		GridBagConstraints contraintes = new GridBagConstraints();

		JEditorPane regles = new JEditorPane();		
		regles.setAutoscrolls(true);
		regles.setContentType("text/html");
		regles.setEditable(false);
		regles.setText(lireFichier_Regle());
		
		
		JScrollPane scrollPane = new JScrollPane(regles);	
		regles.setSize(new Dimension(0,1000));
		add(scrollPane, BorderLayout.CENTER);

		Bouton retour = new Bouton("Retour au menu");
		retour.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_RETOUR, i));
		JPanel pRetour = new JPanel();
		pRetour.add(retour);
		add(pRetour, BorderLayout.SOUTH);
		

	}
	public String lireFichier_Regle(){
		String chaine="";
		try{
			InputStreamReader ipsr=new InputStreamReader(getClass().getResource("/documents/regles.html").openStream());
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				chaine+=ligne+"\n";
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return chaine;
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}