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
	
	public boolean containsLexema(String lexema)
	{
		return ts.containsKey(lexema);
	}
	
	public Iterator<String> getLexemas() {
		return ts.keySet().iterator();
	}
	
	public int numSimbolos(){
		return ts.size();
	}
	
}
