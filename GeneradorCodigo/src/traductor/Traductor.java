package traductor;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Traductor {

	private ArrayList<String> input;
	private ArrayList<String> output;
	
	public Traductor(ArrayList<String> lineas){
		input = lineas;
		output = new ArrayList<String>(); 
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	/**
	 * Llamada principal de esta clase, que traduce autom�ticamente su ArrayList de Strings input.
	 */
	public void traduce() {
		for (int i=0; i<input.size(); i++) {
			//Aqu� es donde empezaremos a partir cada l�nea con un tokenizer,
			//para descubrir qu� instrucci�n es, y llamaremos a un m�todo que traduzca ese tipo.
			//Una vez dentro de ese m�todo, se tomar�n decisiones m�s concretas, para a�adir a output
			//las l�neas que correspondan como fruto de la traducci�n.
			primeraDecision(input.get(i));
		}
	}
	
	
	
	/**
	 * �Esta l�nea es una asignaci�n/operaci�n o no? Act�a en consecuencia. 
	 * @param linea
	 */
	private void primeraDecision(String linea) {
		if (linea.contains(":=")) {
			//Es asignaci�n/operaci�n
			asignacionUOperacion(linea);
		} else {
			//Es una etiqueta
			if (linea.contains(":")){
				esEtiqueta(linea);
			} else {
				
				//No es asignaci�n/operaci�n ni etiqueta (if, for, etc)
				// . . .
			}
			
		}
	}
	
	private void esEtiqueta(String linea) {
		
		if (linea.endsWith(":")) {
			String lineaAux = linea.replaceAll(" ", "");
	        output.add(lineaAux);
	    }
		// FIXME: en otro caso evaluar un posible error en la sintaxis.
	}
	
	/**
	 * �Esta l�nea es una asignaci�n simple o una operaci�n? Act�a en consecuencia.
	 * @param linea
	 */
	private void asignacionUOperacion(String linea) {
		if (linea.contains("+")||linea.contains("-")||linea.contains("*")||linea.contains("/")) {
			//Operaci�n
			//. . .
		} else {
			//Asignaci�n
			tipoDeAsignacion(linea);
		}
	}
	
	/**
	 * �Qu� tipo de asignaci�n es esta l�nea? Act�a en consecuencia.
	 * @param linea
	 */
	private void tipoDeAsignacion(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(":=");
		String s2 = sT.nextToken(":=");
		if (s1.contains("tmp")) {
			//Primer token es un tmp
			if (esUnNumero(s2)) {
				// Segundo token es un n�mero (inmediato)
				bloqueAsignacionTmpInmediato(s1,s2);
			} else {
				// Segundo token es una variable
				//. . .
			}
		} else {
			//Primer token es una variable
			//. . .
		}
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 * @param linea
	 */
	private void bloqueAsignacionTmpInmediato(String s1, String s2) {
		//FIXME: Este m�todo no ser�a as� del todo,
		//porque tenemos que saber a qu� registro realmente hay que enviar
		//el inmediato, en base al n�mero de tmp# que tengamos, y lo que el algoritmo
		//de selecci�n de registros haya asignado a ese tmp
		String s = "MOVE $" + s2 + " .R" + "0"; //El �ltimo "0" es lo que hay que cambiar, lo que comento arriba
		output.add(s);
	}
	
	private boolean esUnNumero(String s) {
		return (s.startsWith("0") ||
				s.startsWith("1") ||
				s.startsWith("2") ||
				s.startsWith("3") ||
				s.startsWith("4") ||
				s.startsWith("5") ||
				s.startsWith("6") ||
				s.startsWith("7") ||
				s.startsWith("8") ||
				s.startsWith("9"));
	}
}
