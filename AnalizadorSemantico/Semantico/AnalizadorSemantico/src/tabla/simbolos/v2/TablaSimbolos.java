package tabla.simbolos.v2;

import java.util.*;

public class TablaSimbolos {
	
	private HashMap<String,Atributos> ts;
	
	public TablaSimbolos()
	{
		ts = new HashMap<String, Atributos>();
	}
	
	public void insertar(String lexema, Atributos atributos)
	{
		if(atributos==null){
			System.err.println("Cuidado! se esta insertando un lexema con atributo asociado nulo.");
		}
		ts.put(lexema, atributos);
	}
	
	public Atributos getAtributos(String lexema)
	{
		return ts.get(lexema);
	}
	
	public Atributos getAtributosDeALias(String alias)
	{
		Iterable<Atributos> listaAtributos = ts.values();
		
		//El alias de los arrays puede llegar a null
		for(Atributos a : listaAtributos){
			if(a.getAlias()!=null && a.getAlias().equals(alias)){
				return a;
			}
		}
		
		return null;
	}
	
	public boolean containsLexema(String lexema)
	{
		return ts.containsKey(lexema);
	}
	
	public Iterator<String> getLexemas() {
		return ts.keySet().iterator();
	}
	
	
	/**
	 * Elegi iterable para que se pueda usar con un for each
	 * 
	 * 	for(Tipo t: listaTipos){
	 * 
	 * 	}
	 * 
	 * @return
	 */
	public Iterable<Atributos> getListaAtributos(){
		
		return ts.values();
		
	}
	
	public int numSimbolos(){
		return ts.size();
	}
	
	
	public String toString(){
		
		String s ="{";
		Iterator<String> it = getLexemas();
		
		while(it.hasNext()){
			s+=it.next();
		}
		
		s+="}";
		
		return s;
		
	}
}
