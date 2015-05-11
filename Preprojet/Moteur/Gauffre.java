public class Gauffre {

	int x, y;
	Case[][] gauffre;
	int xpoison,ypoison;
	
	
	Gauffre(int longueur, int hauteur) {
		x = hauteur;
		y = longueur;
		xpoison=0;
		ypoison=0;
		gauffre =new Case[hauteur][longueur];
		for(int i = xpoison; i < hauteur; i++) {
			for(int j = ypoison; j < longueur; j++) {
				if(i == xpoison && j == ypoison)
					gauffre[i][j]=new Case(Etat.POISON);
				else
					gauffre[i][j]=new Case(Etat.MANGEABLE);
			}
			
		}
			 
	}
	
	void manger(int a, int b){
	
		for(int i=a;i<x;i++){
			for(int j=b; j<y;j++){
				gauffre[i][j].setEtat(Etat.MANGEE);
			}
		}
		
	}
	
	void ecrire(){
		for(int z=0;z<y;z++){System.out.print("_");}
		System.out.println();
		for(int i=0;i<x;i++){
			System.out.print("|");
			for(int j=0;j<y;j++){
				System.out.print(gauffre[i][j].getEtat() +"|");
			}
			System.out.println();
			for(int z=0;z<y;z++){System.out.print("_");}
			System.out.println();
		}
	}
	
	
}
