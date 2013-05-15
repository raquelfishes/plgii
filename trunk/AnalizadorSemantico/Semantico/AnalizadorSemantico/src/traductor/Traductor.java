package traductor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import tabla.simbolos.v2.*;

public class Traductor {

	private static int MARCO_ACTIVACION = 0;
	
	private CGestorTS gestorTS;
	
	private List<Atributos> ambitoActual;
	private List<Atributos> globales;
	
	private ArrayList<String> input;
	private ArrayList<String> output;
	
	public Traductor(CGestorTS gestorTS) {
		this.gestorTS = gestorTS;
	}
	
	public void traduce(String rutaInput, String rutaOutput){
			
		ArrayList<String> lineasIn;
		ArrayList<String> lineasOut;
		
		lineasIn = new ArrayList<String>();
		
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fichero = null;
		PrintWriter pw = null;
		
		try {
			//LECTURA//////////////////////////////////////////////////////////
			System.out.println("Leyendo de "+rutaInput+"...");
			archivo = new File (rutaInput);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while((linea=br.readLine())!=null) {
				lineasIn.add(linea);
			}
			
			//TRADUCCI�N///////////////////////////////////////////////////////
			init(lineasIn);
			System.out.println("Traduciendo...");
			comienzaTraduccion();
			lineasOut = getOutput();
			
			//ESCRITURA////////////////////////////////////////////////////////
			try {
				System.out.println("Escribiendo en "+rutaOutput+"...");
				fichero = new FileWriter(rutaOutput);
				pw = new PrintWriter(fichero);
				for (int i = 0; i < lineasOut.size(); i++) {
					pw.println(lineasOut.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fichero)
						fichero.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			///////////////////////////////////////////////////////////////////
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
	}
	
	private void init(ArrayList<String> lineas) {
		input = lineas;
		output = new ArrayList<String>(); 
		ambitoActual = new ArrayList<Atributos>();
		globales = new ArrayList<Atributos>();
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	/**
	 * Llamada principal de esta clase, que traduce autom�ticamente su ArrayList de Strings input.
	 */
	private void comienzaTraduccion() {
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
		if (linea.endsWith(":")) { //S� es etiqueta
			if (!linea.contains("class ")) { //Si no es comienzo de clase:
				String lineaAux = linea.replaceAll(" ", "");
				String lineaAux2 = lineaAux.replaceAll(":", "");
				ambitoActual = gestorTS.dameListaAtributos(lineaAux2);
		        output.add(lineaAux+"        ; Abriendo �mbito \'"+ lineaAux2 + "\'.");
			} else { //Si es comienzo de clase
				String lineaAux = linea.replaceAll(" ", "");
				lineaAux = lineaAux.replaceAll("class", "");
				globales = gestorTS.dameListaAtributosDeClase();
				output.add(lineaAux);				
			}
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
			tipoDeOperacion(linea);
		} else {
			//Asignaci�n
			tipoDeAsignacion(linea);
		}
	}
	
	/**
	 * �Qu� tipo de operaci�n es esta l�nea? Act�a en consecuencia.
	 * @param linea
	 */
	private void tipoDeOperacion(String linea) {
		//. . .
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
				bloqueAsignacionTmpVariable(s1,s2);
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
		String s = "MOVE #" + s2 + " .R" + "0" + "        ;Asignacion Tmp:=Inmediato"; //El �ltimo "0" es lo que hay que cambiar, lo que comento arriba
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 * @param linea
	 */
	private void bloqueAsignacionTmpVariable(String s1, String s2) {
		//FIXME: Posicion de la variable depende de la pila
		
		//Ejemplo: MOVE #6[.IX],.R1 
		//El 6 representa el desplazamiento desde el registro indice (que apuntar� a la base del registro de activaci�n).
		//El numero tenemos que sacarlo del indice de la variable dentro de un �mbito (posici�n que ocupa la variable
		//en el �mbito actual).
		int desplazamiento = MARCO_ACTIVACION;		
		Atributos a = gestorTS.getAtributosDeAlias(s2);
		if (globales.contains(a)) { //Es global XXX: Decisi�n: con pila o en pool de literales?
			desplazamiento += globales.indexOf(a);
		} else if (ambitoActual.contains(a)) { //Es del ambito actual
			desplazamiento += ambitoActual.indexOf(a);
		} else {
			output.add("Error en bloqueAsignacionTmpVariable(s1,s2)");
		}
		
		/*
		Iterator<Atributos> it = globales.iterator();
		Atributos a = new Atributos("vacio","vacio");		
		boolean b = false;
		while (it.hasNext() && !b) {
			a = it.next();
			b = a.getAlias().equals(s2);
		}
		if (b) { //Es global
			output.add("global");
			desplazamiento += globales.indexOf(a);
		} else { //No es global
			it = ambitoActual.iterator();
			while (it.hasNext() && !b) {
				a = it.next();
				b = a.getAlias().equals(s2);
			}
			if (b) { //Es del ambito actual
				output.add("local");
				desplazamiento += globales.indexOf(a);
			}
		}
		*/
		
		String s = "MOVE #" + desplazamiento + "[.IX] .R" + "0" + "        ;Asignacion Tmp:=Variable";
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
