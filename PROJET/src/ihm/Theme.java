package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

public class Theme {
	public enum Type {
		STANDARD("standard"), BOIS("bois"), MARBRE("marbre"), SOMBRE("dark"), COCHON("cochon");
		public String s;

		Type(String s) {
			this.s = s;
		}

		public String getId() {
			return this.s;
		}
	}

	private IHM ihm;
	private Type id = null;
	private Image imgMenuLarge, imgMenuFin;
	public Color couleurFond = null, couleurDefaut = null, couleurJ1 = null, couleurJ2 = null, couleurPolice = null, couleurPoliceGrisee = null, couleurBordures = null, couleurMenu = null;
	public Color couleurSup = null, couleurInf = null, couleurTDefaut = null;

	Theme(IHM ihm) {
		this.ihm = ihm;
	}

	public Type getTheme() {
		return id;
	}

	public void setTheme(Type pId) {
		if ((this.id == null || !this.id.equals(pId))) {
			this.id = pId;
			switch (pId) {
			case STANDARD:
				couleurFond = new Color(238, 238, 238);
				couleurDefaut = Color.LIGHT_GRAY;
				couleurJ1 = Color.WHITE;
				couleurJ2 = Color.DARK_GRAY;
				couleurPolice = Color.WHITE;
				couleurPoliceGrisee = Color.GRAY;
				couleurBordures = Color.BLACK;
				couleurSup = Color.BLACK;
				couleurInf = Color.BLACK;
				couleurTDefaut = Color.BLACK;
				couleurMenu = Color.BLACK;
				break;
			case BOIS:
				couleurFond = new Color(0, 0, 0);
				couleurDefaut = new Color(240, 230, 140);
				couleurJ1 = new Color(218, 165, 32);
				couleurJ2 = new Color(160, 82, 45);
				couleurPolice = Color.WHITE;
				couleurPoliceGrisee = Color.GRAY;
				couleurBordures = new Color(128, 0, 0);
				couleurSup = Color.BLACK;
				couleurInf = Color.BLACK;
				couleurTDefaut = Color.BLACK;
				couleurMenu = Color.WHITE;
				break;
			case MARBRE:
				couleurFond = new Color(238, 238, 238);
				couleurDefaut = Color.LIGHT_GRAY;
				couleurJ1 = Color.WHITE;
				couleurJ2 = Color.DARK_GRAY;
				couleurPolice = Color.WHITE;
				couleurPoliceGrisee = Color.GRAY;
				couleurBordures = Color.BLACK;
				couleurSup = Color.BLACK;
				couleurInf = Color.BLACK;
				couleurTDefaut = Color.BLACK;
				couleurMenu = Color.BLACK;
				break;
			case SOMBRE:
				couleurFond = new Color(12, 20, 31);
				couleurDefaut = new Color(12, 20, 31);
				couleurJ1 = new Color(111, 195, 223);
				couleurJ2 = new Color(223, 116, 12);
				couleurPolice = new Color(230, 255, 255);
				couleurPoliceGrisee = new Color(162, 186, 186);
				couleurBordures = new Color(230, 255, 255);
				couleurSup = new Color(230, 255, 255);
				couleurInf = new Color(230, 255, 255);
				couleurTDefaut = new Color(230, 255, 255);
				couleurMenu = new Color(230, 255, 255);
				break;
			case COCHON:
				couleurFond = new Color(238, 238, 238);
				couleurDefaut = Color.LIGHT_GRAY;
				couleurJ1 = Color.PINK;
				couleurJ2 = Color.ORANGE;
				couleurPolice = Color.RED;
				couleurPoliceGrisee = Color.GRAY;
				couleurBordures = Color.BLACK;
				couleurSup = Color.BLACK;
				couleurInf = Color.BLACK;
				couleurTDefaut = Color.BLACK;
				couleurMenu = Color.BLACK;
				break;
			}
			ihm.coucheJeu.setBackground(couleurFond);
			ihm.bandeauInfos.panTextes.setBackground(couleurDefaut);
			ihm.bandeauInfos.panTextes.setBorder(BorderFactory.createLineBorder(couleurBordures));
			if (ihm.bandeauInfos.idSelect == 1) {
				ihm.bandeauInfos.panJ1.setBackground(couleurJ1);
				ihm.bandeauInfos.panJ2.setBackground(couleurDefaut);
			} else {
				ihm.bandeauInfos.panJ1.setBackground(couleurDefaut);
				ihm.bandeauInfos.panJ2.setBackground(couleurJ2);
			}
			ihm.bandeauInfos.panJ1.setBorder(BorderFactory.createLineBorder(couleurBordures));
			ihm.bandeauInfos.panJ2.setBorder(BorderFactory.createLineBorder(couleurBordures));
			Bouton.setThemeTous(pId, couleurPolice, couleurPoliceGrisee);
			ihm.bandeauInfos.j1_identifiant.setForeground(couleurTDefaut);
			ihm.bandeauInfos.j1_score.setForeground(couleurTDefaut);
			ihm.bandeauInfos.j2_identifiant.setForeground(couleurTDefaut);
			ihm.bandeauInfos.j2_score.setForeground(couleurTDefaut);
			ihm.bandeauInfos.texteInf.setForeground(couleurInf);
			ihm.bandeauInfos.texteSup.setForeground(couleurSup);
			ihm.popupReseau.etiqId.setForeground(couleurMenu);
			ihm.popupReseau.etiqHeberger.setForeground(couleurMenu);
			ihm.popupReseau.etiqRejoindre.setForeground(couleurMenu);
			ihm.popupReseau.etiqHebergerPort.setForeground(couleurMenu);
			ihm.popupReseau.etiqRejoindrePort.setForeground(couleurMenu);
			ihm.popupO.selectJoueur1Etiq.setForeground(couleurMenu);
			ihm.popupO.selectJoueur2Etiq.setForeground(couleurMenu);
			ihm.popupO.themeEtiq.setForeground(couleurMenu);
			try {
				ihm.tg.imgPlateau = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/plateau.jpg"));
				ihm.tg.imgPion1 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion1.png"));
				ihm.tg.imgPion2 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion2.png"));
				ihm.tg.imgCroix = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/croix.png"));
				imgMenuFin = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/fondMenuFin.png"));
				imgMenuLarge = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/fondMenuLarge.png"));
				ihm.popupM.setImage(imgMenuFin);
				ihm.popupO.setImage(imgMenuLarge);
				ihm.popupR.setImage(imgMenuLarge);
				ihm.popupReseau.setImage(imgMenuLarge);
				ihm.coucheJeu.add(ihm.tg, BorderLayout.CENTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}