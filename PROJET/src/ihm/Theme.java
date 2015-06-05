package ihm;

import java.awt.BorderLayout;
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

	IHM ihm;
	Type id = null;

	private Image imgMenuLarge, imgMenuFin;

	Theme(IHM ihm) {
		this.ihm = ihm;

	}

	public Type getTheme() {
		return id;
	}

	public void setTheme(Type pId) {

		if ((this.id == null || !this.id.equals(pId))) {
			this.id = pId;

			Bouton.setThemeTous(pId);

		}
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