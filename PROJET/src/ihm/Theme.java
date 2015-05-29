package ihm;

public class Theme  {
	
	public enum Type{
		BOIS("bois");
		
		private String s;
		Type(String s){
			this.s = s;
		}
		
		public String getId(){
			return this.s;
		}
	}
	
	IHM ihm;
	Type id = null;
	
	Theme(IHM ihm){
		this.ihm = ihm;
		
	}
	
	public Type getTheme(){
		return id;
	}
	
	public void setTheme(Type pId){
		if(this.id == null || !this.id.equals(pId)){
			this.id = pId;
			Bouton.setThemeTous(pId);
		}		
	}
	
}