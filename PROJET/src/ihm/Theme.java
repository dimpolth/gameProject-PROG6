package ihm;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Theme {

	public enum Type {
		STANDARD("standard"), BOIS("bois"), MARBRE("marbre"), SOMBRE("dark");

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
			ihm.tg.imgPlateau = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/plateau.png"));
			ihm.tg.imgPion1 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion1.png"));
			ihm.tg.imgPion2 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion2.png"));
			ihm.tg.imgCroix = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/croix.png"));

			ihm.coucheJeu.add(ihm.tg, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}