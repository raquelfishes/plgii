package traductor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Traductor {

	private ArrayList<String> input;
	private ArrayList<String> output;
	
	public Traductor(){}
	
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
			
			//TRADUCCIÓN///////////////////////////////////////////////////////
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
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	/**
	 * Llamada principal de esta clase, que traduce automáticamente su ArrayList de Strings input.
	 */
	private void comienzaTraduccion() {
		output.add("");
		for (int i=0; i<input.size(); i++) {
			//Aquí es donde empezaremos a partir cada línea con un tokenizer,
			//para descubrir qué instrucción es, y llamaremos a un método que traduzca ese tipo.
			//Una vez dentro de ese método, se tomarán decisiones más concretas, para añadir a output
			//las líneas que correspondan como fruto de la traducción.
			primeraDecision(input.get(i));
		}
		
		
	}
	
	
	
	/**
	 * ¿Esta línea es una asignación/operación o no? Actúa en consecuencia. 
	 * @param linea
	 */
	private void primeraDecision(String linea) {
		if (linea.contains(":=")) {
			//Es asignación/operación
			asignacionUOperacion(linea);
		} else {
			//Es una etiqueta
			if (linea.contains(":")){
				esEtiqueta(linea);
			} else {
				
				//No es asignación/operación ni etiqueta (if, for, etc)
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
	 * ¿Esta línea es una asignación simple o una operación? Actúa en consecuencia.
	 * @param linea
	 */
	private void asignacionUOperacion(String linea) {
		if (linea.contains("+")||linea.contains("-")||linea.contains("*")||linea.contains("/")) {
			//Operación
			tipoDeOperacion(linea);
		} else {
			//Asignación
			tipoDeAsignacion(linea);
		}
	}
	
	/**
	 * ¿Qué tipo de operación es esta línea? Actúa en consecuencia.
	 * @param linea
	 */
	private void tipoDeOperacion(String linea) {
		//. . .
	}
	
	/**
	 * ¿Qué tipo de asignación es esta línea? Actúa en consecuencia.
	 * @param linea
	 */
	private void tipoDeAsignacion(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(":=");
		String s2 = sT.nextToken(":=");
		if (s1.contains("tmp")) {
			//Primer token es un tmp
			if (esUnNumero(s2)) {
				// Segundo token es un número (inmediato)
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
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 * @param linea
	 */
	private void bloqueAsignacionTmpInmediato(String s1, String s2) {
		//FIXME: Este método no sería así del todo,
		//porque tenemos que saber a qué registro realmente hay que enviar
		//el inmediato, en base al número de tmp# que tengamos, y lo que el algoritmo
		//de selección de registros haya asignado a ese tmp
		String s = "MOVE $" + s2 + " .R" + "0"; //El último "0" es lo que hay que cambiar, lo que comento arriba
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
