package tabla.simbolos;

import java.util.*;

public class TablaSimbolos {

	private Hashtable<String, Atributos> ts;
	private LinkedList<String> listaLexemas;
	private long numeroTemp;
	
	public TablaSimbolos()
	{
		ts = new Hashtable<String, Atributos>();
		listaLexemas = new LinkedList<String>();
	}
	
	public void insertar(String lexema, Atributos atributos)
	{
		ts.put(lexema, atributos);
		listaLexemas.add(lexema);
	}
	
	public Atributos getAtributos(String lexema)
	{
		return ts.get(lexema);
	}
	
	public boolean containsLexema(String lexema)
	{
		return listaLexemas.contains(lexema);
	}
	
	public LinkedList<String> getLexemas() {
		return listaLexemas;
	}
	
	public long getNumTemp(){
		return numeroTemp;
	}
	
	public void actualizaTemp(){
		numeroTemp++;
	}
	
	public void nuevoTemp(){
		numeroTemp = 0;
	}
}
