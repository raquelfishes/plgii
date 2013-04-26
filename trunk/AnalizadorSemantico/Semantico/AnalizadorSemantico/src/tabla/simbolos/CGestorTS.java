package tabla.simbolos;

import java.util.Iterator;
import java.util.LinkedList;


public class CGestorTS implements IGestorTS {
	private LinkedList<TablaSimbolos> ambitos; //Lista (funciona como pila) que guarda el ambito actual y los superiores
	private LinkedList<TablaSimbolos> ambitosCerrados; //Lista de los ámbitos ya cerrados
	
	public CGestorTS() {
		ambitos = new LinkedList<TablaSimbolos>();
		ambitosCerrados = new LinkedList<TablaSimbolos>();
	}
	
	private void apilaNuevaTS() //Siempre añadimos al principio de "ambitos" para que funcione como pila
	{
		TablaSimbolos ts = new TablaSimbolos();
		ts.nuevoTemp();
		ambitos.addFirst(ts);
				
	}
	
	private void desapilaTS() //Siempre eliminamos del principio de "ambitos" para que funcione como pila
	{
		if (!ambitos.isEmpty()) {	
			ambitosCerrados.add(ambitos.getFirst()); 		//Guardamos TS en lista "ambitosCerrados"
			ambitos.removeFirst();							//Desapilamos de "ambitos"
		}
	}
	
	@Override
	public boolean esPalabraReservada(String lexema) {
		return PalabrasReservadas.esReservada(lexema);
	}

	@Override
	public void nuevoAmbito() {
		apilaNuevaTS();
	}

	@Override
	public void cierraAmbito() {
		desapilaTS(); 			//Desapilamos TS y la guardamos en "ambitosCerrados"
	}

	@Override
	public void insertar(String lexema, Atributos atributos) {
		if (ambitos.isEmpty()) apilaNuevaTS(); 				//Si es la primera vez que insertamos, creamos nueva TS
		if (atributos!=null) { 									//Solo insertamos si atributos no es NULL
			TablaSimbolos ts = ambitos.getFirst(); 				//Insertamos en el ámbito actual
			ts.insertar(lexema, atributos);
		}
	}

	@Override
	public boolean estaLexema(String lexema) {
		boolean encontrado = false;
		TablaSimbolos ts;
		Iterator<TablaSimbolos> it = ambitos.iterator();
		while (!encontrado && it.hasNext()) 
		{
			ts = it.next();
			encontrado = ts.containsLexema(lexema);
		}
		return encontrado;
	}

	@Override
	public Atributos getAtributos(String lexema) {
		boolean encontrado = false;
		TablaSimbolos ts = new TablaSimbolos();
		Iterator<TablaSimbolos> it = ambitos.iterator();
		while (!encontrado && it.hasNext()) 
		{
			ts = it.next();
			encontrado = ts.containsLexema(lexema);
		}
		Atributos atributos;
		atributos = encontrado ? atributos = ts.getAtributos(lexema) : null; 
		return atributos;
	}
	
	@Override
	public LinkedList<Atributos> listarAmbitoActual() {
		LinkedList<Atributos> lA = new LinkedList<Atributos>();
		LinkedList<String> lS = new LinkedList<String>();
		TablaSimbolos ts = new TablaSimbolos();
		String s = new String();
		//Para cada Tabla de Símbolos visibles desde nuestro ámbito
		Iterator<TablaSimbolos> it = ambitos.iterator();
		while (it.hasNext()) 
		{
			ts = it.next();
			lS = ts.getLexemas();
			//Para cada lexema
			Iterator<String> iter = lS.iterator();
			while (iter.hasNext()) {
				s = iter.next();
				lA.add(ts.getAtributos(s));
			}
		}
		return lA;
	}
	
	@Override
	public LinkedList<Atributos> listarTodos() {
		LinkedList<Atributos> lA = new LinkedList<Atributos>();
		LinkedList<String> lS = new LinkedList<String>();
		TablaSimbolos ts = new TablaSimbolos();
		String s = new String();
		//Para cada Tabla de Símbolos de "ambitosCerrados"
		Iterator<TablaSimbolos> it = ambitosCerrados.iterator();
		while (it.hasNext()) 
		{
			ts = it.next();
			lS = ts.getLexemas();
			//Para cada lexema
			Iterator<String> iter = lS.iterator();
			while (iter.hasNext()) {
				s = iter.next();
				lA.add(ts.getAtributos(s));
			}
		}
		return lA;
	}
	
	public String getNewTemp(){
		TablaSimbolos ts = ambitos.getFirst();
		long n = ts.getNumTemp();
		ts.actualizaTemp();
		return ("temp"+n);
		
	}

}
