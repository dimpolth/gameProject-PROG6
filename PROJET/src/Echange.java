import java.util.HashMap;
//import java.util.Iterator;

/*
 * 
 * Echange e = new Echange();
 * e.addIndication("Joueur 1 a gagné !");
 * 
 */

public class Echange {
	
	HashMap<String,Object> infos = new HashMap<String,Object>();
	
	public void addIndication(String indic){
		infos.put("indication", indic);
	}
	
	public String getIndication(){
		return (String)infos.get("indication");
	}
	
	/*public Iterator getInfos(){
		return infos;
	}*/
		

}

