package ihm;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Theme {
	TerrainGraphique tg = null;
	IHM i;

	public enum Type {
		BOIS("bois"), MARBRE("marbre");

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

	public void setTheme(Type pId, TerrainGraphique t, IHM ihm_theme) {
		tg = t;
		i = ihm_theme;

		if ((this.id == null || !this.id.equals(pId))) {
			this.id = pId;

			Bouton.setThemeTous(pId);

		}
		try {
			tg.imgPlateau = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/plateau.png"));
			tg.imgPion1 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion1.png"));
			tg.imgPion2 = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/pion2.png"));
			tg.imgCroix = ImageIO.read(getClass().getResource("/images/themes/" + pId.s + "/croix.png"));

			i.coucheJeu.add(tg, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}