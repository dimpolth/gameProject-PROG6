import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Gauffre toto = new Gauffre(5,5);
		Historique h = new Historique();
		boolean victoire = false;
		while(!victoire) {
			boolean jouer = false;
			toto.ecrire();
			while(!jouer) {
				System.out.println("X :");
				Scanner s = new Scanner(System.in);
				String in = s.nextLine();
				System.out.println(in.compareTo("p"));
				if(in.compareTo("p") == 0) {
					if(h.sansPrecedant())
						System.out.println("Rien à défaire");
					else
						toto = h.annuler();
					jouer = true;
				} else if(in.compareTo("s") == 0) {
					if(h.sansSuivant())
						System.out.println("Rien à refaire");
					else
						toto = h.refaire();
					jouer = true;
				} else {
					int coupX = Integer.parseInt(in);
					System.out.println("Y :");
					int coupY = s.nextInt();
					if(toto.gauffre[coupX][coupY].getEtat() == Etat.MANGEABLE) {
						h.ajouter(toto);
						toto.manger(coupX, coupY);
						jouer = true;
					}
					else if(coupX == toto.xpoison && coupY == toto.ypoison) {
						victoire = true;
						jouer = true;
					}
				}
			}
			
		}
		System.out.println("Fini!");
	}

}
