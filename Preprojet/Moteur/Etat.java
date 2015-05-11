public enum Etat {
	MANGEE(0),
	MANGEABLE(1),
	POISON(2);
		
	final int val;
		
	Etat(int a) {
		val = a;
	}
}