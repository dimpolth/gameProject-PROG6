package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	public Color couleurFond = null;

	Theme(IHM ihm) {
		this.ihm = ihm;
	}

	public Type getTheme() {
		return id;
	}

	public void setTheme(Type pId) {
		if ((this.id == null || !this.id.equals(pId))) {
			this.id = pId;
			switch(pId) {
			case STANDARD:
				couleurFond = new Color(238, 238, 238);
				break;
			case BOIS:
				couleurFond = new Color(0, 0, 0);
				break;
			case MARBRE:
				couleurFond = new Color(238, 238, 238);
				break;
			case SOMBRE:
				couleurFond = new Color(12, 20, 31);
				break;
			case COCHON:
				couleurFond = new Color(238, 238, 238);
				break;
			}
			ihm.coucheJeu.setBackground(couleurFond);
			Bouton.setThemeTous(pId);
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
				ihm.popupReseau.setImage(imgMenuFin);
				ihm.coucheJeu.add(ihm.tg, BorderLayout.CENTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}