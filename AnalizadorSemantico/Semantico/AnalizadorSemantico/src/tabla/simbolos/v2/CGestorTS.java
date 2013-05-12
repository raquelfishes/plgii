package tabla.simbolos.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class CGestorTS implements IGestorTS {
	
	private Ambito ambitoGlobal;	//El ambito global es la clase	
	private Ambito ambitoActual;	//El ambito actual cambia segun se crea o cierra un ambito.
	private LinkedList<Ambito> pilaAmbitosSuperiores;
	private List<Ambito> ambitos; 	//Lista de ambitos, donde SOLO en este caso un ambito es un metodo o una constructora
	
	public CGestorTS() {
		ambitos = new ArrayList<Ambito>();
		pilaAmbitosSuperiores = new LinkedList<Ambito>();
		ambitoGlobal = new Ambito("AmbitoClase");		
		ambitoActual = ambitoGlobal;
		
	}
	
	private void apilaAmbito(Ambito a) //Siempre añadimos al principio de "ambitos" para que funcione como pila
	{
		pilaAmbitosSuperiores.addFirst(a);
	}
	
	private Ambito desapilaAmbito() //Siempre eliminamos del principio de "ambitos" para que funcione como pila
	{
		if(pilaAmbitosSuperiores.isEmpty())
			System.err.println("TS: Error al cerrar las llaves, no quedan llaves por cerrar.");
		return pilaAmbitosSuperiores.poll();
	}
	
	@Override
	public boolean esPalabraReservada(String lexema) {
		return PalabrasReservadas.esReservada(lexema);
	}

	/**
	 * Este método debe ser llamado cuando al ejecutar el analizador se detecte una llave "{" 
	 */
	@Override
	public void nuevoAmbito(String nombreAmbito) {
		
		List<Ambito> listaAmbitosPadre = new ArrayList<Ambito>(ambitoActual.dameListaAmbitosPadre());
		listaAmbitosPadre.add(ambitoActual);
		Ambito nuevoAmbito = new Ambito(nombreAmbito, listaAmbitosPadre);
		ambitoActual.insertarAmbitoHijo(nuevoAmbito);
		apilaAmbito(ambitoActual);
		ambitoActual = nuevoAmbito;
		
	}

	@Override
	public void cierraAmbito() {
		ambitoActual = desapilaAmbito();
	}

	@Override
	public void insertar(String lexema, Atributos atributos) {
		ambitoActual.insertarLexema(lexema, atributos);
	}

	@Override
	public boolean esLexemaValido(String lexema) {
		return ambitoActual.esLexemaValido(lexema);
	}

	@Override
	public Atributos getAtributos(String lexema) {
		return ambitoActual.dameAtributosDeLexema(lexema);
	}
	
	private String calcularTabulacion(int n){
		
		String s = "";
		
		//Cuando n va a cero
		while(n-->0){
			s+="\t";
		}
		
		return s;
	}
	
	public void listarTodosLosLexemas(){
		listarTodosLosLexemasAux(ambitoGlobal, 0);
	}
	
	private void listarTodosLosLexemasAux(Ambito ambito, int n_tab){
		
		String tabulacion = calcularTabulacion(n_tab);
		
		System.out.println(tabulacion+ambito.getNombre()+"{");
		imprimirLexemas(ambito, tabulacion);
		List<Ambito> listaAmbitoHijos = ambito.dameListaAmbitosHijos();
		
		int numHijos = listaAmbitoHijos.size();
		
		if(numHijos==0){
			System.out.println(tabulacion+"}"+ambito.getNombre());
			return;
		}
		else{
			for(Ambito ambitoHijo : listaAmbitoHijos){
				listarTodosLosLexemasAux(ambitoHijo, n_tab+1);
			}
		}
		
		System.out.println(tabulacion+"}"+ambito.getNombre());
		
	}
	
	
	private void imprimirLexemas(Ambito a, String tabulacion){
		Iterator<String> it = a.dameLexemas();
		while(it.hasNext()){
			System.out.println(tabulacion+"\t"+it.next());
		}
	}
	
//	@Override
//	public Iterator<Atributos> listarLexemasAmbitoActual() {
//		return ambitoActual.dameLexemas();
//	}
//	
//	@Override
//	public LinkedList<Atributos> listarTodos() {
//		return null;
//	}
	

}
