package traductor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import tabla.simbolos.v2.*;

/**
 * 
 * @author Jes�s, V�ctor
 *
 * Clase Traductor, traduce c�digo intermedio a c�digo final.
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
	
	private int contadorEtiquetas = 0;
	private boolean ningunMetodo = true;
	
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
		ambitoGlobal = new ArrayList<Atributos>();
		registros = new String[10];
		for (int i=0; i<10; i++) registros[i] = "";
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	/**
	 * Llamada principal de esta clase, que traduce autom�ticamente su ArrayList de Strings input.
	 */
	private void comienzaTraduccion() {
		for (int i=0; i<input.size(); i++) {
			liberaRegistros(i);
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
	 * �Esta l�nea es una asignaci�n simple o una operaci�n? Act�a en consecuencia.
	 * @param linea
	 */
	private void esAsignacionUOperacion(String linea) {
		if (linea.contains("+")||linea.contains("-")||linea.contains("*")||linea.contains("/") ||
				linea.contains("%")||linea.contains("&")||linea.contains("|")||linea.contains("^")) {
			//Operaci�n
			esOperacion(linea);
		} else {
			//Asignaci�n
			esAsignacionSimple(linea);
		}
	}
	
	/**
	 * Estamos ante una etiqueta; act�a en consecuencia.
	 * @param linea
	 */
	private void esEtiqueta(String linea) {
		if (linea.replaceAll(" ","").endsWith(":")) { //S� es etiqueta
			if (!linea.contains("class ")) { 
				//NO ES COMIENZO DE UNA CLASE:
				if (linea.contains("&")){
					//ES EL COMIENZO DE UN M�TODO O FUNCI�N
					if (ningunMetodo) {
						ningunMetodo = false;
						output.add("CALL /main");
					}
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
					output.add("NOP");
					
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
	 * Estamos ante un salto. �Condicional o incondicional?
	 * @param linea
	 */
	private void esSalto(String linea) {
		if (linea.startsWith("if")) {
			// Condicionales: (if x op_rel y goto E), que compara x e y mediante un operador relacional op_rel (<,>, <=, etc.). Si dicha relaci�n se verifica, la siguiente proposicion que debe ejecutarse es la etiquetada con E. En caso contrario, la siguiente proposici�n que debe ejecutarse es la siguiente a la actual (secuencia habitual, no se produce salto)
			esSaltoCondicional(linea);
		} else {
			// Incondicionales: (goto E), que indica que la siguiente proposici�n de tres direcciones que debe ejecutarse es la etiquetada con E.
			esSaltoIncondicional(linea);
		}
	}
	
	/**
	 * Salto incondicional; act�a en consecuencia.
	 * @param linea
	 */
	private void esSaltoIncondicional(String linea) {
		String s = "BR $" + linea.replaceAll("goto", "").replaceAll(" ","");
		output.add(s);
	}
	
	/**
	 * Salto condicional; act�a en consecuencia.
	 * @param linea
	 */
	private void esSaltoCondicional(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(")");
		String s2 = sT.nextToken(")");
		s1 = s1.replace('(', ' ');
		s1 = s1.replaceAll(" ", "");
		s1 = s1.replaceAll("if", "");   //comparaci�n
		s2 = s2.replaceAll(" ","");
		s2 = s2.replaceAll("goto","");	//etiqueta
		bloqueComparacion(s1);
		bloqueSaltoCondicional(s1,s2);
	}
	
	private void bloqueComparacion(String comp) {
		StringTokenizer sT = new StringTokenizer(comp);
		String s1 = sT.nextToken("<>=!|&");
		String s2 = sT.nextToken("<>=!|&");
		String s = "CMP ";
		if (esUnNumero(s1)) {
			//Primer token es un n�mero
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
		s += ", ";
		if (esUnNumero(s2)) {
			//Segundo token es un n�mero
			s += "#" + s2;
		} else if (!s2.contains("$")) {
			//Segundo token es un tmp
			s += ".R" + dameNumeroRegistro(s2);
		} else if (!s2.contains("[")){
			//Segundo token es una variable
			s += getDesplazamientoVariable(s2);
		} else {
			//Segundo token es un array
			//XXX Array
			s += "";
		}
		output.add(s);
	}
	
	private void bloqueSaltoCondicional(String comp, String etiq){
		String s = "";
		if (comp.contains("!=")){
			s += "BNZ";
			s += " $" + etiq;
			output.add(s);
		} else if (comp.contains("=")){
			s += "BZ";
			s += " $" + etiq;
			output.add(s);
		} else if (comp.contains(">")){
			s += "BN $etiqueta_"+ contadorEtiquetas +"\nBNZ";
			s += " $" + etiq;
			output.add(s);
			output.add("etiqueta_"+contadorEtiquetas+":");
			contadorEtiquetas++;
		} else if (comp.contains("<")){
			s += "BN";
			s += " $" + etiq;
			output.add(s);
		} //...
	}
	
	///////////////////////////////////////////////////////////////////////////
	// METODOS Y FUNCIONES
	///////////////////////////////////////////////////////////////////////////
	
	private void esProcedimiento(String linea) {
		// Proposiciones propias de procedimientos y funciones
		// Llamada a procedimiento o funci�n 
		if (linea.contains("param")) {
			//	i. Paso de par�metros: (param x). Introduce el parametro x en la pila
			esProcedimientoPasoParam(linea);
		} else if (linea.contains("call")/*||linea.contains("]")*/) {
			//	ii. Invocaci�n del subprograma: (call p, n). Llama al procedimiento p, y le dice que tome n par�metros de la cima de la pila.
			esProcedimientoInvocacion(linea);
		} else {
			// Retorno: (return y), donde el par�metro y es opcional. Si aparece, es el valor devuelto por una funci�n; en cualquier caso, devuelve el flujo de control a proposici�n situada inmediatamente despu�s de call p, n que invoc� el subprograma en curso.
			esProcedimientoRetorno(linea);
		}
		
	}
	
	/**
	 * Estamos ante un param; act�a en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoPasoParam(String linea) {
		linea = linea.replaceAll("param", "").replaceAll(" ", "");
		String s = "PUSH ";
		if (esUnNumero(linea)) {
			//Primer token es un n�mero
			s += "#" + linea;
		} else if (!linea.contains("$")) {
			//Primer token es un tmp
			s += ".R" + dameNumeroRegistro(linea);
		} else if (!linea.contains("[")){
			//Primer token es una variable
			s += getDesplazamientoVariable(linea);
		} else {
			s = "";
		}
		output.add(s);
	}
	
	/**
	 * Estamos ante un call; act�a en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoInvocacion(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(",");
		String s2 = sT.nextToken(",");
		s1 = s1.replaceAll("call", "").replaceAll(" ","");
		s2 = s2.replaceAll(" ", "");
		String s = "CALL /" + s1;
		output.add(s);
	}
	
	/**
	 * Estamos ante un return; act�a en consecuencia.
	 * @param linea
	 */
	private void esProcedimientoRetorno(String linea) {
		if (linea.replaceAll(" ", "").equals("&return")) {
			// Return sin devolver valor
			if (nombreAmbitoActual.toLowerCase().equals("main")) { //Fin de la ejecuci�n
				output.add(";-------------fin");
				//Escribir variables globales por consola
				output.add("WRSTR /sVarGlobales");
				for (int i = 1; i<= calcularTamanoAmbito(ambitoGlobal); i++) {
					output.add("WRINT #" + i + "[.IY]");
					output.add("WRSTR /sRetCarro");
				}
				
				output.add(";liberamos el espacio para variables globales");
				output.add("ADD .SP, #" + calcularTamanoAmbito(ambitoGlobal));
				output.add("MOVE .A, .SP");
				output.add("");
				output.add(";Fin de la ejecuci�n.");
				output.add("HALT");
				output.add("");
				output.add("");
				output.add("sVarGlobales:    DATA \"Variables globales:\\n\"");
				output.add("sRetCarro:       DATA \"\\n\"");
			} else {
				bloqueReturnSinValor();
			}
			nombreAmbitoActual = nombreAmbitoClase;
			ambitoActual = ambitoGlobal;
		} else {
			//Return devolviendo valor
			linea = linea.replaceAll("return","");
			linea = linea.replaceAll(" ", "");
			linea = linea.replaceAll("&", "");
			bloqueReturnConValor(linea);
			nombreAmbitoActual = nombreAmbitoClase;
			ambitoActual = ambitoGlobal;
		}
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente al comienzo de la clase.
	 */
	private void bloqueComienzoClase() {		
		String s0 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Comienzo del programa";
		String s1 = nombreAmbitoActual + ":";
		String s2 = ";colocamos el puntero de pila en la cima de la memoria";
		String s3 = "MOVE #65535, .SP";
		String s4 = ";reservamos el espacio para variables globales de clase en la pila";
		String s5 = "SUB .SP, #" + calcularTamanoAmbito(ambitoGlobal);
		String s6 = "MOVE .A, .SP";
		String s7 = ";guardamos en .IY el puntero a pila, para tener controladas las variables globales";
		String s8 = "MOVE .SP, .IY";
		String s9 = ";guardamos tambien en .IX el puntero a pila, para tener controlado siempre SP en las recursiones";
		String s10 = "MOVE .SP, .IX";
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
		String s2 = ";guardamos los registros en pila";
		String s3 = "PUSH .IX";
		String s4 = "PUSH .R0";
		String s5 = "PUSH .R1";
		String s6 = "PUSH .R2";
		String s7 = "PUSH .R3";
		String s8 = "PUSH .R4";
		String s9 = "PUSH .R5";
		String s10 = "PUSH .R6";
		String s11 = "PUSH .R7";
		String s13 = "PUSH .R9";
		String s14 = ";reservamos el espacio para variables locales en la pila";
		String s15 = "SUB .SP, #" + calcularTamanoAmbito(ambitoActual);
		String s16 = "MOVE .A, .SP";
		
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
		output.add(s13);
		output.add(s14);
		output.add(s15);
		output.add(s16);
		
		
		//Parte encargada de gestionar los par�metros pasados
		output.add(";salvamos el valor de .IY");
		output.add("MOVE .IY, .R9");
		output.add(";.IX apunta todav�a al anterior marco de pila, lo usaremos para rescatar los par�metros (si hay)");
		output.add("MOVE .IX, .IY");
		output.add(";guardamos en .IX el puntero a pila, para usar este registro como �ndice de este m�todo");
		output.add("MOVE .SP, .IX");
		output.add(";guardamos los par�metros pasados (si los hay) en sus respectivas variables locales");
		int desp =  0; //Desp desde .IX
		ArrayList<Atributos> aL = (ArrayList<Atributos>) gestorTS.dameParametrosMetodo(nombreAmbitoActual);
		for (int i = desp; i< aL.size(); i++) {
			String s= "MOVE #" + -i + "[.IY], #" + desplazamiento(aL.get(i), ambitoActual) + "[.IX]";
			output.add(s);
		}
		output.add(";restauramos el valor de .IY");
		output.add("MOVE .R9, .IY");		
		
		
		
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
		output.add("");
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente al retorno de un m�todo.
	 */
	private void bloqueReturnSinValor() {
		output.add("");
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Return del metodo " + nombreAmbitoActual);
		output.add(";liberamos el espacio para variables locales de la pila");
		output.add("ADD .SP, #" + calcularTamanoAmbito(ambitoActual));
		output.add("MOVE .A, .SP");
		output.add(";cargamos los registros de la pila");
		output.add("POP .R9");
		output.add("POP .R7");
		output.add("POP .R6");
		output.add("POP .R5");
		output.add("POP .R4");
		output.add("POP .R3");
		output.add("POP .R2");
		output.add("POP .R1");
		output.add("POP .R0");
		output.add("POP .IX");
		
		output.add(";copiamos el antiguo .PC unas posiciones m�s abajo, justo encima del SP anterior");
		output.add("MOVE .IY, .R9");
		output.add("MOVE .SP, .IY");
		output.add("MOVE #1[.IY], #0[.IX]");
		output.add("MOVE .R9, .IY");
		
		String s17 = ";liberamos el espacio de los parametros de la llamada";
		ArrayList<Atributos> aL = (ArrayList<Atributos>) gestorTS.dameParametrosMetodo(nombreAmbitoActual);
		String s18 = "ADD .SP, #" + aL.size();
		String s19 = "MOVE .A, .SP";
		String s20 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
		String s21 = "RET";
		
		output.add(s17);
		output.add(s18);
		output.add(s19);
		output.add(s20);
		if (!nombreAmbitoActual.toLowerCase().equals("main")) 	output.add(s21);
		output.add("");
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente al retorno de una funci�n.
	 */
	private void bloqueReturnConValor(String s) {
		//XXX
		if (esUnNumero(s)) {
			// Token es un n�mero (inmediato)
			output.add("MOVE #" + s + ", .R8");
		} else if (!s.contains("$")){
			// Token es un tmp
			output.add("MOVE .R" + dameNumeroRegistro(s) + ", .R8");
		} else if (!s.contains("[")){
			// Token es una variable
			output.add("MOVE " + getDesplazamientoVariable(s) + ", .R8");
		} else {
			// Token es un array
			// XXX Array
		}
		output.add("");
		output.add("");
		output.add(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Return del metodo " + nombreAmbitoActual);
		output.add(";liberamos el espacio para variables locales de la pila");
		output.add("ADD .SP, #" + calcularTamanoAmbito(ambitoActual));
		output.add("MOVE .A, .SP");
		output.add(";cargamos los registros de la pila");
		output.add("POP .R9");
		output.add("POP .R7");
		output.add("POP .R6");
		output.add("POP .R5");
		output.add("POP .R4");
		output.add("POP .R3");
		output.add("POP .R2");
		output.add("POP .R1");
		output.add("POP .R0");
		output.add("POP .IX");
		
		output.add(";copiamos el antiguo .PC unas posiciones m�s abajo, justo encima del SP anterior");
		output.add("MOVE .IY, .R9");
		output.add("MOVE .SP, .IY");
		output.add("MOVE #1[.IY], #0[.IX]");
		output.add("MOVE .R9, .IY");
		
		String s17 = ";liberamos el espacio de los parametros de la llamada";
		ArrayList<Atributos> aL = (ArrayList<Atributos>) gestorTS.dameParametrosMetodo(nombreAmbitoActual);
		String s18 = "ADD .SP, #" + aL.size();
		String s19 = "MOVE .A, .SP";
		String s20 = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
		String s21 = "RET";
		
		output.add(s17);
		output.add(s18);
		output.add(s19);
		output.add(s20);
		output.add(s21);
		output.add("");
	}
	
	///////////////////////////////////////////////////////////////////////////
	// ASIGNACIONES
	///////////////////////////////////////////////////////////////////////////	
	/**
	 * �Qu� tipo de asignaci�n es esta l�nea? Act�a en consecuencia.
	 * @param linea
	 */
	private void esAsignacionSimple(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(":=");
		String s2 = sT.nextToken(":=");
		
		//Atributos a = gestorTS.getAtributos(s2);
		//if (a!=null && a.getEsMetodo()) {
		if (!esUnNumero(s2) && !s2.startsWith("$") && !s2.startsWith("tmp")) {
			//Segundo token es una funci�n
			if (esUnNumero(s1)) {
				// Primer token es un n�mero (inmediato)
				output.add("MOVE .R8, #"+ s1);
			} else if (!s1.contains("$")){
				// Primer token es un tmp
				output.add("MOVE .R8, .R" + dameNumeroRegistro(s1));
			} else if (!s1.contains("[")){
				// Primer token es una variable
				output.add("MOVE .R8, " + getDesplazamientoVariable(s1));
			} else {
				// Primer token es un array
				// XXX Array
			}
		} else if (!s1.contains("$")) {
			//Primer token es un tmp
			asignaRegistro(s1);
			if (esUnNumero(s2)) {
				// Segundo token es un n�mero (inmediato)
				bloqueAsignacionTmpInmediato(s1,s2);
			} else if (!s2.contains("$")){
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
				// Segundo token es un n�mero (inmediato)
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
				// Segundo token es un n�mero (inmediato)
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
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpInmediato(String s1, String s2) {
		String s = "MOVE #" + s2 + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpTmp(String s1, String s2) {
		String s = "MOVE .R" + dameNumeroRegistro(s2) + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpVariable(String s1, String s2) {
		String s = "MOVE " + getDesplazamientoVariable(s2) + ", .R" + dameNumeroRegistro(s1);
		output.add(s);
	}

	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableInmediato(String s1, String s2) {
		String s = "MOVE #" + s2 + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableTmp(String s1, String s2) {
		String s = "MOVE .R" + dameNumeroRegistro(s2) + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableVariable(String s1, String s2) {
		String s = "MOVE " + getDesplazamientoVariable(s2) + ", " + getDesplazamientoVariable(s1);
		output.add(s);
	}

	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionTmpArray(String s1, String s2) {
		//TODO AsignacionTmpArray
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionVariableArray(String s1, String s2) {
		//TODO AsignacionVariableArray
	}

	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionArrayInmediato(String s1, String s2) {
		//TODO AsignacionArrayInmediato
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionArrayTmp(String s1, String s2) {
		//TODO AsignacionArrayTmp
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionArrayVariable(String s1, String s2) {
		//TODO AsignacionArrayVariable
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de asignaci�n.
	 */
	private void bloqueAsignacionArrayArray(String s1, String s2) {
		//TODO AsignacionArrayArray
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// OPERACIONES
	///////////////////////////////////////////////////////////////////////////	
	/**
	 * �Qu� tipo de operaci�n es esta l�nea? Act�a en consecuencia.
	 * @param linea
	 */
	private void esOperacion(String linea) {
		//OPERACIONES
		StringTokenizer sT = new StringTokenizer(linea);
		String s1 = sT.nextToken(":="); //Parte izquierda (siempre es un tmp)
		String s2 = sT.nextToken(":="); //Parte derecha (la que contiene la operacion)
		//Primer token es un tmp
		asignaRegistro(s1);
		if (s2.contains("~")||s2.contains("|")) {
//			Con operadores unarios: (x := op y), donde op ser�  un operador unario. Las operaciones unarias principales incluyen el menos unario, la negaci�n l�gica, los operador de desplazamiento y  operador de conversi�n de tipos (de entero a real, por ejemplo).		
			//Segundo token es la parte derecha
			esOperacionUnaria(s2); //Esta llamada imprime la operacion (antes del MOVE)
		}
		else if (s2.contains("+")||s2.contains("-")||s2.contains("*")||s2.contains("/")||s2.contains("%")||s2.contains("&")||s2.contains("|")||s2.contains("^")) {
//			Con operadores binarios: (x := y op z), donde op ser� un operador binario aritm�tico o l�gico.
			//Segundo token es la parte derecha
			esOperacionBinaria(s2); //Esta llamada imprime la operacion (antes del MOVE)
		}
		String s = "MOVE .A, .R" + dameNumeroRegistro(s1);
		output.add(s);		
	}
	
	/**
	 * �Que tipo de operacion unaria es? Act�a en consecuencia
	 * @param linea
	 */
	private void esOperacionUnaria(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String op, s1;
		//Primero distinguimos el tipo de operacion
		if (linea.contains("~")) {
		//Es Complento ->> temp1 := ~a
			op = "NEG"; //Cambio de signo del operando
			s1 = sT.nextToken("~");
		}
		else /*if (linea.contains("!"))*/ {
		//Es Negacion ->> temp1 := !a
			op = "NOT"; //Negacion logica bit a bit
			s1 = sT.nextToken("!");
		}
		//Segundo distinguimos el tipo de operando
		if (esUnNumero(s1)) {
			// Segundo token es un n�mero (inmediato)
			bloqueOperacionUnariaInmediato(op, s1);
		} else if (!s1.contains("$")){
			// Segundo token es un tmp
			asignaRegistro(s1);
			bloqueOperacionUnariaTmp(op, s1);
		} else if (!s1.contains("[")){
			// Segundo token es una variable
			bloqueOperacionUnariaVariable(op, s1);
		} else {
			// Segundo token es un array
			bloqueOperacionUnariaArray(op, s1);
		}
	}
	
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private void bloqueOperacionUnariaInmediato(String op, String s1) {
		String s = op + " " + s1;
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private void bloqueOperacionUnariaTmp(String op, String s1) {
		String s = op + " " + dameNumeroRegistro(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private void bloqueOperacionUnariaVariable(String op, String s1) {
		String s = op + " " + getDesplazamientoVariable(s1);
		output.add(s);
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private void bloqueOperacionUnariaArray(String op, String s1) {
		String s = op + " "/* + imprimeArray(s1)*/;
		output.add(s);
	}
	
	/**
	 * �Que tipo de operacion binaria es? Act�a en consecuencia.
	 * @param linea
	 */
	private void esOperacionBinaria(String linea) {
		StringTokenizer sT = new StringTokenizer(linea);
		String op, s1, s2; //SubStrings de linea
		
		//1� distinguimos el tipo de operacion
		//OperacionBinariaAritmetica
		if (linea.contains("+")) {
//		    + (mas) => temp1 := a + b; ->	ADD op1, op2
			op = "ADD"; //Cambio de signo del operando
			s1 = sT.nextToken("+");
			s2 = sT.nextToken("+");
		}
		else if (linea.contains("-")) {
//			- (menos) => temp1 := a - b;
			op = "SUB"; //Negacion logica bit a bit
			s1 = sT.nextToken("-");
			s2 = sT.nextToken("-");
		}
		else if (linea.contains("*")) {
//			* (por) => temp1 := a * b;
			op = "MUL"; //Negacion logica bit a bit
			s1 = sT.nextToken("*");
			s2 = sT.nextToken("*");
		}
		else if (linea.contains("/")) {
//			/ (div) => temp1 := a / b;
			op = "DIV"; //Negacion logica bit a bit
			s1 = sT.nextToken("/");
			s2 = sT.nextToken("/");
		}
		else if (linea.contains("%")) {
//			% (mod) => temp1 := a % b;
			op = "MOD"; //Negacion logica bit a bit
			s1 = sT.nextToken("%");
			s2 = sT.nextToken("%");
		}
		//OperacionBinariaLogica
		else if (linea.contains("&")) {
//			& (and) => temp1 := a & b;
			op = "AND"; //Negacion logica bit a bit
			s1 = sT.nextToken("&");
			s2 = sT.nextToken("&");
		}
		else if (linea.contains("|")) {
//			| (or) => temp1 := a | b;
			op = "OR"; //Negacion logica bit a bit
			s1 = sT.nextToken("|");
			s2 = sT.nextToken("|");
		}
		else /*if (linea.contains("^"))*/ {
//			^ (xor) => temp1 := a ^ b;
			op = "XOR"; //Negacion logica bit a bit
			s1 = sT.nextToken("^");
			s2 = sT.nextToken("^");
		}
			
		//2� distinguimos el tipo del 1� operando
		String oper1;
		if (esUnNumero(s1)) {
			// Segundo token es un n�mero (inmediato)
			oper1 = bloqueOperacionBinariaOperandoInmediato(s1);
		} else if (!s1.contains("$")){
			// Segundo token es un tmp
			asignaRegistro(s1);
			oper1 = bloqueOperacionBinariaOperandoTmp(s1);
		} else if (!s1.contains("[")){
			// Segundo token es una variable
			oper1 = bloqueOperacionBinariaOperandoVariable(s1);
		} else {
			// Segundo token es un array
			oper1 = bloqueOperacionBinariaOperandoArray(s1);
		}
		
		//3� distinguimos el tipo del 2� operando
		String oper2;
		if (esUnNumero(s2)) {
			// Segundo token es un n�mero (inmediato)
			oper2 = bloqueOperacionBinariaOperandoInmediato(s2);
		} else if (!s2.contains("$")){
			// Segundo token es un tmp
			asignaRegistro(s2);
			oper2 = bloqueOperacionBinariaOperandoTmp(s2);
		} else if (!s2.contains("[")){
			// Segundo token es una variable
			oper2 = bloqueOperacionBinariaOperandoVariable(s2);
		} else {
			// Segundo token es un array
			oper2 = bloqueOperacionBinariaOperandoArray(s2);
		}
		
		//4� Imprimimimos el codigo pertinente
		bloqueOperacionBinariaParam(op, oper1, oper2);
	}
	
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private String bloqueOperacionBinariaOperandoInmediato(String s1) {
		String s = "#"+s1;
		return s;
	}
		
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private String bloqueOperacionBinariaOperandoTmp(String s1) {
		String s = ".R" + dameNumeroRegistro(s1);
		return s;
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private String bloqueOperacionBinariaOperandoVariable(String s1) {
		String s = getDesplazamientoVariable(s1);
		return s;
	}

	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private String bloqueOperacionBinariaOperandoArray(String s1) {
		//XXX Array
		String s = ""/* + imprimeArray(s1)*/;
		return s;
	}
	
	/**
	 * A�ade a output el bloque de c�digo correspondiente a este tipo de operaci�n.
	 */
	private void bloqueOperacionBinariaParam(String op, String oper1, String oper2) {
		String s = "" + op + " " + oper1 + ", " + oper2;
		output.add(s);
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
		} else if (ambitoActual.contains(a)) { //ES DEL �MBITO ACTUAL
			s= "#" + desplazamiento(a, ambitoActual) + "[.IX]";
		} else {
			System.err.println(">>> Traductor error: Error en getDesplazamiento. Variable no encontrada.");
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
		return t;//-1;  �?
	}
	
	//ASIGNACI�N DE REGISTROS//////////////////////////////////////////////////
	private boolean estaAsignado(String tmp) {
		boolean asignado = false;
		for (int r = 0; r<8 && !asignado; r++) {
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
			for (int r = 0; r<8 && !asignado; r++) {
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
		for (int r = 0; r<8 && !encontrado; r++) {
			if (registros[r].equals(tmp)) {
				//Encontrado!
				reg = r;
				encontrado = true;
			}
		}
		return reg;
	}
	
	private void liberaRegistros(int lineaActual) {
		for (int r = 0; r<8; r++) {
			boolean aparece = false;
			for (int i = lineaActual; i<input.size() && !aparece; i++) {
				if (input.get(i).contains(registros[r])) {
					//El tmp del registro r aparece en m�s partes del c�digo, no se puede liberar
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
