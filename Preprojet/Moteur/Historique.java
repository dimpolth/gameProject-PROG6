import java.util.Stack;


public class Historique {

	Stack<Gauffre> pileprec;
	Stack<Gauffre> pilesuiv;
	
	Historique(){
		pileprec = new Stack<Gauffre>();
		pilesuiv = new Stack<Gauffre>();
	}
	
	void ajouter(Gauffre g) {
		pileprec.push(g);
		pilesuiv = new Stack<Gauffre>();
	}
	
	Gauffre annuler(){
		pilesuiv.push(pileprec.pop());
		return pilesuiv.peek();
	}
	
	Gauffre refaire(){
		pileprec.push(pileprec.pop());
		return pileprec.peek();
	}
	
	boolean sansPrecedant() {
		return pileprec.isEmpty();
	}
	
	boolean sansSuivant() {
		return pilesuiv.isEmpty();
	}
	
	void sauvegarder() {
		
	}
	
	void charger() {
		
	}

}
