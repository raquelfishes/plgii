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
 * @author Jesús
 *
 * Clase Traductor, traduce código intermedio a código final.
 */
public class Traductor {

	private static int MARCO_ACTIVACION = 0;
	
	private CGestorTS gestorTS;
	
	private String nombreAmbitoActual;
	private String nombreAmbitoClase;
	private List<Atributos> ambitoActual;
	private List<Atributos> ambitoGlobal;
	
	private ArrayList<String> input;
	private ArrayList<String> output;
	
	private String []registros;
	
	public Traductor(CGestorTS gestorTS) {
		this.gestorTS = gestorTS;
		nombreAmbitoActual = "";
		nombreAmbitoClase = "";
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
		ambitoActual = new ArrayList<Atributos>();
		ambitoGlobal = new ArrayList<Atributos>();
		registros = new String[10];
		for (int i=0; i<10; i++) registros[i] = "";
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	/**
	 * Llamada principal de esta clase, que traduce automáticamente su ArrayList de Strings input.
	 */
	private void comienzaTraduccion() {
		for (int i=0; i<input.size(); i++) {
			liberaRegistros(i);
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
			esAsignacionUOperacion(linea);
		} else if (linea.contains(":")){
			//Es una etiqueta
			esEtiqueta(linea);
		} else if (linea.contains("goto")) {
			//Es un salto
			esSalto(linea);
		} else if (linea.contains("param")||linea.contains("call")||linea.contains("return")) {
			//Es un procedimiento
			esProcedimiento(linea);
		}
	}

	/**
	 * ¿Esta línea es una asignación simple o una operación? Actúa en consecuencia.
	 * @param linea
	 */
	private void esAsignacionUOperacion(String linea) {
		if (linea.contains("+")||linea.contains("-")||linea.contains("*")||linea.contains("/")) {
			//Operación
			esOperacion(linea);
		} else {
			//Asignación
			esAsignacionSimple(linea);
		}
	}
	
	/**
	 * Estamos ante una etiqueta; actúa en consecuencia.
	 * @param linea
	 */
	private void esEtiqueta(String linea) {
		if (linea.endsWith(":")) { //Sí es etiqueta
			if (!linea.contains("class ")) { 
				//NO ES COMIENZO DE UNA CLASE:
				if (linea.contains("&")){
					//ES EL COMIENZO DE UN MÉTODO O FUNCIÓN
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
				ambitoGlobal = gestorTS.dameListaAtributosDeClase();
				bloqueComienzoClase();
			}
	    }
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// SALTOS
	///////////////////////////////////////////////////////////////////////////
	/**
	 * Estamos ante un salto. ¿Condicional o incondicional?
	 * @param linea
	 */
	private void esSalto(String linea) {
		if (linea.contains("if")) {
			// Condicionales: (if x op_rel y goto E), que compara x e y mediante un operador relacional op_rel (<,>, <=, etc.). Si dicha relación se verifica, la siguiente proposicion que debe ejecutarse es la etiquetada con E. En caso contrario, la siguiente proposición que debe ejecutarse es la siguiente a la actual (secuencia habitual, no se produce salto)
			esSaltoCondicional(linea);
		} else {
			// Incondicionales: (goto E), que indica que la siguiente proposición de tres direcciones que debe ejecutarse es la etiquetada con E.
			esSaltoIncondicional(linea);
		}
	}
	
	/**
	 * Salto incondicional; actúa en consecuencia.
	 * @param linea
	 */
	private void esSaltoIncondicional(String linea) {
		String s = "\t\tBR $" + linea.replaceAll("goto", "").replaceAll(" ","");
		output.add(s);
	}
	
	/**
	 * Salto condicional; actúa en consecuencia.
	 * @param linea
	 */
	private void esSaltoCondicional(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(")");
		String s2 = sT.nextToken(")");
		s1 = s1.replaceAll("if(", "").replaceAll(" ", "");	//comparación
		s2 = s2.replaceAll("goto","").replaceAll(" ","");	//etiqueta
		bloqueComparacion(s1);
		bloqueSaltoCondicional(s1,s2);
	}
	
	private void bloqueComparacion(String comp) {
		StringTokenizer sT = new StringTokenizer(comp);
		String s1 = sT.nextToken("<>=!|&");
		String s2 = sT.nextToken("<>=!|&");
		String s = "\t\t\t\tCMP ";
		if (esUnNumero(s1)) {
			//Primer token es un número
			s += "#" + s1;
		} else if (!s1.contains("$")) {
			//Primer token es un tmp
			s += ".R" + dameNumeroRegistro(s1);
		} else if (!s1.contains("[")){
			//Primer token es una variable
			s += getDesplazamientoVariable(s1);
		} else {
			//Primer token es un array
			//XXX Array
			s += "";
		}
		s += " ";
		if (esUnNumero(s2)) {
			//Segundo token es un número
			s += "#" + s2;
		} else if (!s2.contains("$")) {
			//Segundo token es un tmp
			s += ".R" + dameNumeroRegistro(s2);
		} else if (!s2.contains("[")){
			//Segundo token es una variable
			s += getDesplazamientoVariable(s1);
		} else {
			//Segundo token es un array
			//XXX Array
			s += "";
		}
		output.add(s);
	}
	
	private void bloqueSaltoCondicional(String comp, String etiq){
		//TODO Completar tipos de salto condicional
		String s = "\t\t\t\t";
		if (comp.contains("!=")){
			s += "BNZ";
		} else if (comp.contains("=")){
			s += "BZ";
		} else if (comp.contains(">")){
			s += "BP";
		} else if (comp.contains("<")){
			s += "BN";
		} //...
		s += " " + etiq;
		output.add(s);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// METODOS Y FUNCIONES
	///////////////////////////////////////////////////////////////////////////
	
	private void esProcedimiento(String linea) {
		// Proposiciones propias de procedimientos y funciones
		// Llamada a procedimiento o función 
		if (linea.contains("param")) {
			//	i. Paso de parámetros: (param x). Introduce el parametro x en la pila
			esProcedimientoPasoParam(linea);
		} else if (linea.contains("call")/*||linea.contains("]")*/) {
			//	ii. Invocación del subprograma: (call p, n). Llama al procedimiento p, y le dice que tome n parámetros de la cima de la pila.
			esProcedimientoInvocacion(linea);
		} else {
			// Retorno: (return y), donde el parámetro y es opcional. Si aparece, es el valor devuelto por una función; en cualquier caso, devuelve el flujo de control a proposición situada inmediatamente después de call p, n que invocó el subprograma en curso.
			esProcedimientoRetorno(linea);
		}
		
	}
	
	/**
	 * Estamos ante un param; actúa en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoPasoParam(String linea) {
		linea = linea.replaceAll("param", "").replaceAll(" ", "");
		String s = "\t\t\t\tPUSH ";
		if (esUnNumero(linea)) {
			//Primer token es un número
			s += "#" + linea;
		} else if (!linea.contains("$")) {
			//Primer token es un tmp
			s += ".R" + dameNumeroRegistro(linea);
		} else if (!linea.contains("[")){
			//Primer token es una variable
			s += getDesplazamientoVariable(linea);
		} else {
			//Primer token es un array
			//XXX Array
			s += "";
		}
		output.add(s);
	}
	
	/**
	 * Estamos ante un call; actúa en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoInvocacion(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(",");
		String s2 = sT.nextToken(",");
		s1 = s1.replaceAll("call", "").replaceAll(" ","");
		s2 = s2.replaceAll(" ", "");
		String s = "\t\t\t\tCALL /" + s1;
		output.add(s);
	}
	
	/**
	 * Estamos ante un return; actúa en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoRetorno(String linea) {
		if (linea.replaceAll(" ", "").equals("&return")) {
			// Return sin devolver valor
			if (nombreAmbitoActual.toLowerCase().equals("main")) { //Fin de la ejecución
				output.add("\t\t;liberamos el espacio para variables globales");
				output.add("\t\tADD .SP, #" + calcularTamanoAmbito(ambitoGlobal));
				output.add("\t\tMOVE .A, .SP");
				output.add("");
				output.add("\t\t;Fin de la ejecución.");
				output.add("\t\tHALT");
			} else {
				bloqueReturnSinValor();
			}
			nombreAmbitoActual = nombreAmbitoClase;
			ambitoActual = ambitoGlobal;
		} else {
			//TODO Return con valor
			output.add(";LINEA DE CODIGO SIN HACER, RETURN CON VALOR");
		}
	}
	
	/**
	 * Añade a output el bloque de código correspondiente al comienzo de la clase.
	 */
	private void bloqueComienzoClase() {
		String s0 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Comienzo del programa";
		String s1 = nombreAmbitoActual + ":";
		String s2 = "\t\t;colocamos el puntero de pila en la cima de la memoria";
		String s3 = "\t\tMOVE #65535, .SP";
		String s4 = "\t\t;reservamos el espacio para variables globales de clase en la pila";
		String s5 = "\t\tSUB .SP, #" + calcularTamanoAmbito(ambitoGlobal);
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
	 * Añade a output el bloque de código correspondiente al comienzo de un ámbito (método).
	 */
	private void bloqueComienzoAmbito() {
		String s0 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Comienzo del ámbito " + nombreAmbitoActual;
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
		String s15 = "\t\tSUB .SP, #" + calcularTamanoAmbito(ambitoActual);
		String s16 = "\t\tMOVE .A, .SP";
		
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
		
		
		//Parte encargada de gestionar los parámetros pasados
		output.add("\t\t;salvamos el valor de .IY");
		output.add("\t\tMOVE .IY, .R9");
		output.add("\t\t;.IX apunta todavía al anterior marco de pila, lo usaremos para rescatar los parámetros (si hay)");
		output.add("\t\tMOVE .IX, .IY");
		output.add("\t\t;guardamos en .IX el puntero a pila, para usar este registro como índice de este método");
		output.add("\t\tMOVE .SP, .IX");
		output.add("\t\t;guardamos los parámetros pasados (si los hay) en sus respectivas variables locales");
		int desp =  0; //Desp desde .IX
		ArrayList<Atributos> aL = (ArrayList<Atributos>) gestorTS.dameParametrosMetodo(nombreAmbitoActual);
		for (int i = desp; i< aL.size(); i++) {
			String s= "\t\tMOVE #" + -i + "[.IY], #" + desplazamiento(aL.get(i), ambitoActual) + "[.IX]";
			output.add(s);
		}
		output.add("\t\t;restauramos el valor de .IY");
		output.add("\t\tMOVE .R9, .IY");		
		
		
		
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
		output.add("");
	}
	
	/**
	 * Añade a output el bloque de código correspondiente al retorno de un método.
	 */
	private void bloqueReturnSinValor() {
		output.add("");
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Return del metodo " + nombreAmbitoActual);
		output.add("\t\t;liberamos el espacio para variables locales de la pila");
		output.add("\t\tADD .SP, #" + calcularTamanoAmbito(ambitoActual));
		output.add("\t\tMOVE .A, .SP");
		output.add("\t\t;cargamos los registros de la pila");
		output.add("\t\tPOP .R9");
		output.add("\t\tPOP .R8");
		output.add("\t\tPOP .R7");
		output.add("\t\tPOP .R6");
		output.add("\t\tPOP .R5");
		output.add("\t\tPOP .R4");
		output.add("\t\tPOP .R3");
		output.add("\t\tPOP .R2");
		output.add("\t\tPOP .R1");
		output.add("\t\tPOP .R0");
		output.add("\t\tPOP .IX");
		
		output.add("\t\t;copiamos el antiguo .PC unas posiciones más abajo, justo encima del SP anterior");
		output.add("\t\tMOVE .IY, .R9");
		output.add("\t\tMOVE .SP, .IY");
		output.add("\t\tMOVE #1[.IY], #0[.IX]");
		output.add("\t\tMOVE .R9, .IY");
		
		String s17 = "\t\t;liberamos el espacio de los parametros de la llamada";
		ArrayList<Atributos> aL = (ArrayList<Atributos>) gestorTS.dameParametrosMetodo(nombreAmbitoActual);
		String s18 = "\t\tADD .SP, #" + aL.size();
		String s19 = "\t\tMOVE .A, .SP";
		String s20 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
		String s21 = "\t\tRET";
		
		output.add(s17);
		output.add(s18);
		output.add(s19);
		output.add(s20);
		if (!nombreAmbitoActual.toLowerCase().equals("main")) 	output.add(s21);
		output.add("");
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// ASIGNACIONES Y OPERACIONES
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * ¿Qué tipo de operación es esta línea? Actúa en consecuencia.
	 * @param linea
	 */
	private void esOperacion(String linea) {
		//TODO OPERACIONES	
	}
	
	/**
	 * ¿Qué tipo de asignación es esta línea? Actúa en consecuencia.
	 * @param linea
	 */
	private void esAsignacionSimple(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(":=");
		String s2 = sT.nextToken(":=");
		if (!s1.contains("$")) {
			//Primer token es un tmp
			asignaRegistro(s1);
			if (esUnNumero(s2)) {
				// Segundo token es un número (inmediato)
				bloqueAsignacionTmpInmediato(s1,s2);
			} else if (!s2.contains("&")){
				// Segundo token es un tmp
				asignaRegistro(s2);
				bloqueAsignacionTmpTmp(s1,s2);
			} else if (!s2.contains("[")){
				// Segundo token es una variable
				bloqueAsignacionTmpVariable(s1,s2);
			} else {
				// Segundo token es un array
				bloqueAsignacionTmpArray(s1,s2);
			}
		} else if (!s1.contains("[")){
			//Primer token es una variable
			if (esUnNumero(s2)) {
				// Segundo token es un número (inmediato)
				bloqueAsignacionVariableInmediato(s1,s2);
			} else 	if (!s2.contains("$")) {
				// Segundo token es un tmp
				bloqueAsignacionVariableTmp(s1,s2);
			} else if (!s2.contains("[")){
					// Segundo token es una variable
				bloqueAsignacionVariableVariable(s1,s2);
			} else {
				// Segundo token es un array
				bloqueAsignacionVariableArray(s1,s2);
			}
		} else {
			//Primer token es un array
			if (esUnNumero(s2)) {
				// Segundo token es un número (inmediato)
				bloqueAsignacionArrayInmediato(s1,s2);
			} else 	if (!s2.contains("$")) {
				// Segundo token es un tmp
				bloqueAsignacionArrayTmp(s1,s2);
			} else if (!s2.contains("[")){
					// Segundo token es una variable
				bloqueAsignacionArrayVariable(s1,s2);
			} else {
				// Segundo token es un array
				bloqueAsignacionArrayArray(s1,s2);
			}
		}
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionTmpInmediato(String s1, String s2) {
		String s = "\t\t\t\tMOVE #" + s2 + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionTmpTmp(String s1, String s2) {
		String s = "\t\t\t\tMOVE .R" + dameNumeroRegistro(s2) + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionTmpVariable(String s1, String s2) {
		String s = "\t\t\t\tMOVE " + getDesplazamientoVariable(s2) + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}

	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionVariableInmediato(String s1, String s2) {
		String s = "\t\t\t\tMOVE #" + s2 + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionVariableTmp(String s1, String s2) {
		String s = "\t\t\t\tMOVE .R" + dameNumeroRegistro(s2) + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionVariableVariable(String s1, String s2) {
		String s = "\t\t\t\tMOVE " + getDesplazamientoVariable(s2) + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}

	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionTmpArray(String s1, String s2) {
		//TODO AsignacionTmpArray
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionVariableArray(String s1, String s2) {
		//TODO AsignacionVariableArray
	}

	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionArrayInmediato(String s1, String s2) {
		//TODO AsignacionArrayInmediato
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionArrayTmp(String s1, String s2) {
		//TODO AsignacionArrayTmp
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionArrayVariable(String s1, String s2) {
		//TODO AsignacionArrayVariable
	}
	
	/**
	 * Añade a output el bloque de código correspondiente a este tipo de asignación.
	 */
	private void bloqueAsignacionArrayArray(String s1, String s2) {
		//TODO AsignacionArrayArray
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// AUXILIARES
	///////////////////////////////////////////////////////////////////////////
	
	//REFERENCIA DE VARIABLES//////////////////////////////////////////////////
	private String getDesplazamientoVariable(String alias) {
		String s = "";
		Atributos a = gestorTS.getAtributosDeAlias(alias);
		if (ambitoGlobal.contains(a)) { 	//ES GLOBAL
			s= "#" + desplazamiento(a, ambitoGlobal) + "[.IY]";
		} else if (ambitoActual.contains(a)) { //ES DEL ÁMBITO ACTUAL
			s= "#" + desplazamiento(a, ambitoActual) + "[.IX]";
		} else {
			System.err.println("Traductor error: Error en getDesplazamiento. Variable no encontrada.");
		}
		return s;
	}
	
	private int desplazamiento(Atributos a, List<Atributos> l) {
		int d = MARCO_ACTIVACION;
		Atributos actual = new Atributos("vacio","vacio",false);
		for (int i=0; i<l.size() && !actual.equals(a); i++) {
			actual = l.get(i);
			if (actual.getEsArray()) {
				d += actual.getTamArray();
			} else {
				d ++;
			}
		}
		return d;
	}
	
	private int calcularTamanoAmbito(List<Atributos> l) {
		int t = 0;
		Atributos actual;
		for (int i=0; i<l.size(); i++) {
			actual = l.get(i);
			if (actual.getEsArray()) {
				t += actual.getTamArray();
			} else {
				t ++;
			}
		}
		return t;//-1;  // XXX ¿?
	}
	
	//ASIGNACIÓN DE REGISTROS//////////////////////////////////////////////////
	private boolean estaAsignado(String tmp) {
		boolean asignado = false;
		for (int r = 0; r<10 && !asignado; r++) {
			if (registros[r].equals(tmp)) {
				//registro encontrado
				asignado = true;
			}
		}
		return asignado;
	}
	
	private void asignaRegistro(String tmp) {
		if (!estaAsignado(tmp)) {
			boolean asignado = false;
			for (int r = 0; r<9 && !asignado; r++) {
				if (registros[r].equals("")) {
					//Hueco encontrado
					registros[r] = tmp;
					asignado = true;
				}
			}
		}
	}
	
	private int dameNumeroRegistro(String tmp) {
		int reg = -1;
		boolean encontrado = false;
		for (int r = 0; r<10 && !encontrado; r++) {
			if (registros[r].equals(tmp)) {
				//Encontrado!
				reg = r;
				encontrado = true;
			}
		}
		return reg;
	}
	
	private void liberaRegistros(int lineaActual) {
		for (int r = 0; r<10; r++) {
			boolean aparece = false;
			for (int i = lineaActual; i<input.size() && !aparece; i++) {
				if (input.get(i).contains(registros[r])) {
					//El tmp del registro r aparece en más partes del código, no se puede liberar
					aparece = true;
				}
			}
			if (!aparece) {
				registros[r] = "";
			}
		}
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
