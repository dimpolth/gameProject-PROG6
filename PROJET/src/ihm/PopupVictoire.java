package ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class PopupVictoire extends JPanel {

	private Timer timer;
	private static final int DELAY = 30, DIVIDER = 180, MULTIPLY_FACTOR = 36, LINE_LENGTH = 2, FIREWORK_RADIUS = 100;
	private static final int ARRAY_LENGTH = 11;
	private Point CENTER;
	private Random r;
	private static Color colors[] = new Color[ARRAY_LENGTH];
	private static final double PI = 3.14159;
	int x[] = new int[10], y[] = new int[10];
	private int x1, moveX, color_index;
	List<Integer> xx = new ArrayList<Integer>();
	List<Integer> yy = new ArrayList<Integer>();

	public PopupVictoire() {
		setOpaque(false);

		x1 = color_index = 0;
		moveX = 3;
		timer = new Timer(DELAY, new MyChangeListener());
		CENTER = new Point(100, 100);

		r = new Random();

		colors[0] = Color.ORANGE;
		colors[1] = Color.BLUE;
		colors[2] = Color.CYAN;
		colors[3] = Color.RED;
		colors[4] = Color.PINK;
		colors[5] = Color.YELLOW;
		colors[6] = Color.DARK_GRAY;
		colors[7] = Color.GREEN;
		colors[8] = Color.WHITE;
		colors[9] = Color.GRAY;
		colors[10] = Color.MAGENTA;

		setBackground(Color.black);
	}
	
	public void lancer() {
		setVisible(true);
		timer.start();
	}
	public void arreter() {
		setVisible(false);
		timer.stop();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;

		Stroke stroke = new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 5, new float[] { 9 }, 0);
		graphics2d.setStroke(stroke);
		graphics2d.setColor(colors[color_index]);
		for (int i = 0; i < xx.size(); i++) {
			graphics2d.drawLine(xx.get(i), yy.get(i), xx.get(i) + LINE_LENGTH, yy.get(i) + LINE_LENGTH);
		}
	}

	class MyChangeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			x1 += moveX;
			if (x1 == 0 || x1 >= FIREWORK_RADIUS) {
				x1 = 0;
				CENTER.x = r.nextInt(getWidth() - 200) + 100;
				CENTER.y = r.nextInt(getHeight() - 200) + 100;
				color_index = r.nextInt(ARRAY_LENGTH);
			}
			xx.clear();
			yy.clear();
			for (int i = 0; i < 10; i++) {
				xx.add((int) (CENTER.x + x1 * Math.cos((MULTIPLY_FACTOR * i * PI) / DIVIDER)));
				yy.add((int) (CENTER.y + x1 * Math.sin((MULTIPLY_FACTOR * i * PI) / DIVIDER)));
			}
			repaint();
		}
	}
}

/*
 * @SuppressWarnings("serial") public class PopupVictoire extends JPanel
 * implements ActionListener { private Timer t; private long prochainLancement,
 * precTemps; private LinkedList<Particule> particules = new
 * LinkedList<Particule>(); private Random aleat;
 * 
 * public PopupVictoire() { super(); setOpaque(false); t = new Timer(10, this);
 * precTemps = 0; aleat = new Random(); }
 * 
 * public void lancer() { setVisible(true); repaint(); tirer(aleat); precTemps =
 * System.currentTimeMillis(); t.start(); }
 * 
 * public void arreter() { setVisible(false); t.stop(); }
 * 
 * private void tirer(Random a) { prochainLancement = System.currentTimeMillis()
 * + a.nextInt(3000)+2000; for(int i=0 ; i<aleat.nextInt(500)+2 ; i++) {
 * particules.add(new Fusee(aleat, Forme.FUSEE, Color.GRAY, particules)); } }
 * 
 * public void paintComponent(Graphics g) { Iterator<Particule> it =
 * particules.iterator(); while(it.hasNext()) { it.next().afficher(g,
 * getWidth(), getHeight()); } }
 * 
 * @Override public void actionPerformed(ActionEvent e) { float facteurSeconde =
 * (System.currentTimeMillis()-precTemps)/1000f; precTemps =
 * System.currentTimeMillis(); if(precTemps > prochainLancement) { tirer(aleat);
 * } Iterator<Particule> it = particules.iterator(); while(it.hasNext()) {
 * if(it.next().maj(facteurSeconde)) it.remove(); } repaint(); } }
 * 
 * enum Forme {OVALE, FUSEE};
 * 
 * class Particule { private LinkedList<Particule> particules;
 * 
 * public Float coord; public Float vecteur; public Forme forme; public Color
 * couleur; public Float taille;
 * 
 * public Particule(Random a, Forme f, Color c, LinkedList<Particule> p) { coord
 * = new Float(a.nextFloat(), 1); vecteur = new
 * Float((a.nextFloat()/2)-0.5f,a.nextFloat()/3); forme = f; couleur = c;
 * if(forme == Forme.OVALE || forme == Forme.FUSEE) { taille = new Float(0.01f,
 * 0.03f); } particules = p; //;//System.out.println("POP"); } public boolean
 * maj(float facteurSeconde) { coord.x = coord.x + vecteur.x*facteurSeconde;
 * //;//System.out.println(coord.x+"-"+vecteur.x+"-"+facteurSeconde); coord.y =
 * coord.y + vecteur.y*facteurSeconde; vecteur.x = vecteur.x *0.75f; vecteur.y =
 * vecteur.y - 0.2f*facteurSeconde; return false; } public void
 * afficher(Graphics g, int w, int h) { g.setColor(couleur); if(forme ==
 * Forme.OVALE || forme == Forme.FUSEE) { g.fillOval((int)(coord.x*w),
 * (int)(coord.y*h), (int)(taille.x*w), (int)(taille.y*h)); } } }
 * 
 * class Fusee extends Particule { public long tempsExplosion; public
 * Fusee(Random a, Forme f, Color c, LinkedList<Particule> p) { super(a, f, c,
 * p); tempsExplosion = System.currentTimeMillis()+a.nextInt(2000)+3000; }
 * public boolean maj(float facteurSeconde) { super.maj(facteurSeconde);
 * if(System.currentTimeMillis() > tempsExplosion) {
 * //;//System.out.println("ANIHILIATION !"); return true; } return false; }
 * 
 * }
 */