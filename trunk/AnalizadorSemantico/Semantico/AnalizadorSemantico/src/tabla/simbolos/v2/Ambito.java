package tabla.simbolos.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ambito {

	private TablaSimbolos tsLocal;
	private List<Ambito> listaDeAmbitosPadres;
	private List<Ambito> listaDeAmbitosHijos;
	
	public Ambito(List<Ambito> listaDePadresTS){
		this.listaDeAmbitosPadres = listaDePadresTS;
		this.tsLocal = new TablaSimbolos();
		this.listaDeAmbitosHijos = new ArrayList<Ambito>();
	}
	
	public Ambito(){
		this.listaDeAmbitosPadres = new ArrayList<Ambito>();
		tsLocal = new TablaSimbolos();
		this.listaDeAmbitosHijos = new ArrayList<Ambito>();
	}
	
	public void insertarAmbitoHijo(Ambito a){
		this.listaDeAmbitosHijos.add(a);
	}
	
	public TablaSimbolos dameTS(){
		return tsLocal;
	}
	
	public List<Ambito> dameListaAmbitosPadre(){
		return listaDeAmbitosPadres;
	}
	
	public List<Ambito> dameListaAmbitosHijos(){
		return listaDeAmbitosHijos;
	}
	
	public void insertarLexema(String lexema, Atributos atributos){
		tsLocal.insertar(lexema, atributos);
	}
	
	public Atributos dameAtributosDeLexema(String lexema){
		
		//Si el lexema se encuentra en la TS local al Ambito
		if(tsLocal.containsLexema(lexema)){
			return tsLocal.getAtributos(lexema);
		}
		
		//Si el lexema no esta en la TS local al ambito se busca en sus padres
		for(Ambito tsPadre : listaDeAmbitosPadres){
		
			//Si algun padre la tiene se devuelve el atributo
			if(tsPadre.dameTS().containsLexema(lexema)){
				return tsPadre.dameTS().getAtributos(lexema);
			}
			
		}

		return null;
		
	}
	
	/**
	 * Metodo que sirve para comprobar la declaracion de variables repetidas.
	 * 
	 * 	void miMetodo(){
	 * 		
	 * 		int i = 10;
	 * 		int j = 20;
	 * 
	 * 		if(i==10){
	 * 			int j = 3;   <-- Esto deberia dar un error semantico, porque la variable j ya estaba declarada en el ambitode su padre(el metodo "mimetodo")
	 * 		}
	 * 
	 * 	}
	 * 
	 * @param lexema
	 * @return Devuelve true si el lexema no ha sido declarado  ni en su ambito local
	 * ni en alguno de sus padres.
	 */
	public boolean esLexemaValido(String lexema){
		return dameAtributosDeLexema(lexema) == null;
	}
	
	public Iterator<String> dameLexemas(){
		return tsLocal.getLexemas();
	}
}
