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

/**
 * 
 * @author Jes�s
 *
 * Clase Traductor, traduce c�digo intermedio a c�digo final.
 */
public class Traductor {

	private static int MARCO_ACTIVACION = 1;
	
	private CGestorTS gestorTS;
	
	private String nombreAmbitoActual;
	private String nombreAmbitoClase;
	private List<Atributos> ambitoActual;
	private List<Atributos> globales;
	
	private ArrayList<String> input;
	private ArrayList<String> output;
	
	private String []registros;
	
	public Traductor(CGestorTS gestorTS) {
		this.gestorTS = gestorTS;
		nombreAmbitoActual = "";
		nombreAmbitoClase = "";
		registros = new String[10];
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
			esAsignacionUOperacion(linea);
		} else {
			//Es una etiqueta
			if (linea.contains(":")){
				esEtiqueta(linea);
			} else {
				//No es asignaci�n/operaci�n ni etiqueta (if, for, etc)
				if  (linea.endsWith("return") || linea.startsWith("&return ")) {
					esReturn(linea);
				}
			}
			
		}
	}
	
	/**
	 * Estamos ante una etiqueta; act�a en consecuencia.
	 * @param linea
	 */
	private void esEtiqueta(String linea) {
		if (linea.endsWith(":")) { //S� es etiqueta
			if (!linea.contains("class ")) { 
				//NO ES COMIENZO DE UNA CLASE:
				if (linea.contains("&")){
					//ES EL COMIENZO DE UN M�TODO O FUNCI�N
					String lineaAux = linea.replaceAll(" ", "");
					String lineaAux2 = lineaAux.replaceAll(":", "");
					lineaAux2 = lineaAux2.replaceAll("&", "");
					nombreAmbitoActual = lineaAux2;
					ambitoActual = gestorTS.dameListaAtributos(nombreAmbitoActual);
					bloqueComienzoAmbito();
				} else {
					//ES UNA ETIQUETA NORMAL
					output.add("");
					output.add(linea.replaceAll(" ", ""));
					
				}
			} else {
				//SI ES EL COMIENZO DE LA CLASE
				String lineaAux = linea.replaceAll(" ", "");
				lineaAux = lineaAux.replaceAll("class", "");
				nombreAmbitoActual = nombreAmbitoClase = lineaAux.replaceAll(":", "").replaceAll("&", "");
				globales = gestorTS.dameListaAtributosDeClase();
				bloqueComienzoClase();
			}
	    }
	}
	
	/**
	 * Estamos ante un return; act�a en consecuencia.
	 * @param linea
	 */
	private void esReturn(String linea) {
		if (linea.replaceAll(" ", "").equals("&return")) {
			bloqueReturnMetodo();
			if (nombreAmbitoActual.toLowerCase().equals("main")) { //Fin de la ejecuci�n
				output.add("\t\t;Fin de la ejecuci�n.");
				output.add("\t\tHALT");
			}
			nombreAmbitoActual = nombreAmbitoClase;
			ambitoActual = globales;
		} else {
			//TODO
			output.add(";LINEA DE CODIGO SIN HACER, RETURN CON VALOR");
		}
	}

	/**
	 * �Esta l�nea es una asignaci�n simple o una operaci�n? Act�a en consecuencia.
	 * @param linea
	 */
	private void esAsignacionUOperacion(String linea) {
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
		if (!s1.contains("$")) {
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
			if (esUnNumero(s2)) {
				// Segundo token es un n�mero (inmediato)
				bloqueAsignacionVariableInmediato(s1,s2);
			} else {
				if (!s2.contains("$")) {
					// Segundo token es un tmp
					bloqueAsignacionVariableTmp(s1,s2);
				} else {
					// Segundo token es una variable
					bloqueAsignacionVariableVariable(s1,s2);
				}
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	// METODOS Y FUNCIONES
	///////////////////////////////////////////////////////////////////////////
	/**
	 * A�ade a output el bloque de c�digo correspondiente al comienzo de la clase.
	 */
	private void bloqueComienzoClase() {
		String s0 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Comienzo del programa";
		String s1 = nombreAmbitoActual + ":";
		String s2 = "\t\t;colocamos el puntero de pila en la cima de la memoria";
		String s3 = "\t\tMOVE #65535, .SP";
		String s4 = "\t\t;reservamos el espacio para variables globales de clase en la pila";
		String s5 = "\t\tSUB .SP, #" + globales.size();
		String s6 = "\t\tMOVE .A, .SP";
		String s7 = "\t\t;guardamos en .IY el puntero a pila, para tener controladas las variables globales";
		String s8 = "\t\tMOVE .SP, .IY";
		String s9 = "\t\t;guardamos tambien en .IX el puntero a pila, para tener controlado siempre SP en las recursiones";
		String s10 = "\t\tMOVE .SP, .IX";
		output.add("");
		output.add(s0);
		output.add(s1);
		output.add(s2);
		output.add(s3);
		output.add(s4);
		output.add(s5);
		output.add(s6);
		output.add(s7);
		output.add(s8);
		output.add(s9);
		output.add(s10);
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
		output.add("");
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente al comienzo de un �mbito (m�todo).
	 */
	private void bloqueComienzoAmbito() {
		String s0 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Comienzo del �mbito " + nombreAmbitoActual;
		String s1 = nombreAmbitoActual + ":";
		String s2 = "\t\t;guardamos los registros en pila";
		String s3 = "\t\tPUSH .IX";
		String s4 = "\t\tPUSH .R0";
		String s5 = "\t\tPUSH .R1";
		String s6 = "\t\tPUSH .R2";
		String s7 = "\t\tPUSH .R3";
		String s8 = "\t\tPUSH .R4";
		String s9 = "\t\tPUSH .R5";
		String s10 = "\t\tPUSH .R6";
		String s11 = "\t\tPUSH .R7";
		String s12 = "\t\tPUSH .R8";
		String s13 = "\t\tPUSH .R9";
		String s14 = "\t\t;reservamos el espacio para variables locales en la pila";
		String s15 = "\t\tSUB .SP, #" + ambitoActual.size();
		String s16 = "\t\tMOVE .A, .SP";
		String s17 = "\t\t;guardamos en .IX el puntero a pila, para usar este registro como �ndice de este m�todo";
		String s18 = "\t\tMOVE .SP, .IX";
		output.add("");
		output.add(s0);
		output.add(s1);
		output.add(s2);
		output.add(s3);
		output.add(s4);
		output.add(s5);
		output.add(s6);
		output.add(s7);
		output.add(s8);
		output.add(s9);
		output.add(s10);
		output.add(s11);
		output.add(s12);
		output.add(s13);
		output.add(s14);
		output.add(s15);
		output.add(s16);
		output.add(s17);
		output.add(s18);
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
		output.add("");
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente al retorno de un m�todo.
	 */
	private void bloqueReturnMetodo() {
		String s1 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Return del metodo " + nombreAmbitoActual;
		String s2 = "\t\t;liberamos el espacio para variables locales de la pila";
		String s3 = "\t\tADD .SP, #" + ambitoActual.size();
		String s4 = "\t\tMOVE .A, .SP";
		String s5 = "\t\t;cargamos los registros de la pila";
		String s6 = "\t\tPOP .R9";
		String s7 = "\t\tPOP .R8";
		String s8 = "\t\tPOP .R7";
		String s9 = "\t\tPOP .R6";
		String s10 = "\t\tPOP .R5";
		String s11 = "\t\tPOP .R4";
		String s12 = "\t\tPOP .R3";
		String s13 = "\t\tPOP .R2";
		String s14 = "\t\tPOP .R1";
		String s15 = "\t\tPOP .R0";
		String s16 = "\t\tPOP .IX";
		String s17 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
		output.add("");
		output.add(s1);
		output.add(s2);
		output.add(s3);
		output.add(s4);
		output.add(s5);
		output.add(s6);
		output.add(s7);
		output.add(s8);
		output.add(s9);
		output.add(s10);
		output.add(s11);
		output.add(s12);
		output.add(s13);
		output.add(s14);
		output.add(s15);
		output.add(s16);
		output.add(s17);
		output.add("");
	}
	///////////////////////////////////////////////////////////////////////////
	// ASIGNACIONES
	///////////////////////////////////////////////////////////////////////////
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpInmediato(String s1, String s2) {
		registros[0] = s1;
		String s = "\t\t\t\tMOVE #" + s2 + ", .R" + "0" + "\t\t\t\t;r0 contiene " + registros[0];
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpVariable(String s1, String s2) {
		registros[0] = s1;
		String s = "\t\t\t\tMOVE " + getDesplazamiento(s2) + ", .R" + "0" + "\t\t\t\t;r0 contiene " + registros[0];
		output.add(s);
	}

	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableInmediato(String s1, String s2) {
		String s = "\t\t\t\tMOVE #" + s2 + ", " + getDesplazamiento(s1) + "\t\t\t\t;Asignacion Variable:=Inmediato";
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableTmp(String s1, String s2) {
		registros[0] = s2;
		String s = "\t\t\t\tMOVE .R" + "0" + ", " + getDesplazamiento(s1) + "\t\t\t\t;r0 contiene " + registros[0];
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableVariable(String s1, String s2) {
		registros[0] = s2;
		String s = "\t\t\t\tMOVE " + getDesplazamiento(s2) + ", " + getDesplazamiento(s1) + "\t\t\t\t;r0 contiene " + registros[0];
		output.add(s);
	}
	
	
	private String getDesplazamiento(String alias) {
		//Ejemplo: MOVE #6[.IX],.R1 
		//El 6 representa el desplazamiento desde el registro indice (que apuntar� a la base del registro de activaci�n).
		//El numero tenemos que sacarlo del indice de la variable dentro de un �mbito (posici�n que ocupa la variable
		//en el �mbito actual).
		int desplazamiento = MARCO_ACTIVACION;		
		String s = "";
		Atributos a = gestorTS.getAtributosDeAlias(alias);
		if (globales.contains(a)) {
			desplazamiento += globales.indexOf(a);
			s= "#" + desplazamiento + "[.IY]";
		} else if (ambitoActual.contains(a)) { //Es del ambito actual
			desplazamiento += ambitoActual.indexOf(a);
			s= "#" + desplazamiento + "[.IX]";
		} else {
			output.add(";Error en getDesplazamiento. Variable no encontrada.");
		}
		return s;
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
				s.startsWith("9") ||
				s.startsWith("-0") ||
				s.startsWith("-1") ||
				s.startsWith("-2") ||
				s.startsWith("-3") ||
				s.startsWith("-4") ||
				s.startsWith("-5") ||
				s.startsWith("-6") ||
				s.startsWith("-7") ||
				s.startsWith("-8") ||
				s.startsWith("-9"));
	}
}
