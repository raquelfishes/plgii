import java.util.*;

public class GestorTS {
	private LinkedList<TablaSimbolos> listaTS;
	
	public GestorTS ()
	{
		listaTS = new LinkedList<TablaSimbolos>();
	}
	
	public void nuevaTS()
	{
		TablaSimbolos ts = new TablaSimbolos();
		listaTS.addFirst(ts);		//Añadimos al principio para utilizar como pila
	}
	
	public boolean containsLexema(String lexema)
	{
		boolean encontrado = false;
		TablaSimbolos ts;
		Iterator<TablaSimbolos> it = listaTS.iterator();
		while (!encontrado && it.hasNext()) 
		{
			ts = it.next();
			encontrado = ts.containsLexema(lexema);
		}
		return encontrado;
	}
	
	public void insertar(String lexema, Atributos atributos)
	{
		if (listaTS.isEmpty()) nuevaTS(); //Si es la primera vez que insertamos, creamos nueva TS
		TablaSimbolos ts = listaTS.getFirst();
		ts.insertar(lexema, atributos);
	}
	
	// Como get que es, devuelve null si no existe (comprobar antes con containsLexema)
	public Atributos getAtributos(String lexema)
	{
		boolean encontrado = false;
		TablaSimbolos ts = new TablaSimbolos();
		Iterator<TablaSimbolos> it = listaTS.iterator();
		while (!encontrado && it.hasNext()) 
		{
			ts = it.next();
			encontrado = ts.containsLexema(lexema);
		}
		Atributos atributos;
		atributos = encontrado ? atributos = ts.getAtributos(lexema) : null; 
		return atributos;
	}
}
