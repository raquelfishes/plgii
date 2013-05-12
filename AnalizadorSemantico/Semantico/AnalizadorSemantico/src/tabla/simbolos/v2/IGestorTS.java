package tabla.simbolos.v2;

import java.util.LinkedList;

public interface IGestorTS {
	
	/**
	 * Partiendo de que la idea es que cada ámbito tenga una tabla de símbolos asignada...
	 */
	
	
	/**
	 * Si es palabra reservada no se debe insertar en la tabla 
	 * @param lexema identificador encontrado en la fase de análisis
	 * @return true si es una palabra reservada, false en c.c.
	 */
	public boolean esPalabraReservada(String lexema);
	
	/**
	 * Nuevo ámbito es equivalente a decir que se quiere una nueva tabla de símbolos para
	 * un nuevo método.
	 * 
	 * Aunque lo ideal sería que si no se ha llamado aún al cierraAmbito, el método
	 * nuevoAmbito() cree un nuevo subAmbito y por tanto una nueva tabla de símbolos
	 * para ella. 
	 * 
	 * Los subAmbitos están pensados para las estructuras de control: if,else, while, for, etc.
	 */
	public void nuevoAmbito();
	
	/**
	 * Es importante cerrar el ámbito, porque así, al llamar de nuevo al método
	 * nuevoAmbito() se podrán tener ámbitos en paralelo sin que ellos puedan observarse.
	 * Por ejemplo, los métodos de java no pueden observar su contenido entre ellos.
	 */
	public void cierraAmbito();
	
	/**
	 * Se insertará un elemento en el ámbito actual. Se deberá considerar antes si insertarlo
	 * comprobando si ya esta con la función estaLexema(String lexema).
	 * Esto debe ser así ya que el análisis del fichero es de arriba hacia abajo,
	 * por tanto, sí en la fase de análisis que es en la que se supone que vamos 
	 * reconociendo tokens, nos encontramos con el símbolo "{" significaría que
	 * deberíamos abrir un nuevo ámbito o un nuevo subAmbito, y cuando finalmente
	 * se reconozca el símbolo "}" debemos cerrar el ámbito o subAmbito para así
	 * asegurar el paralelismo entre ámbitos con ámbitos y subAmbitos con subAmbitos. 
	 * @param lexema
	 */
	public void insertar(String lexema, Atributos atributos);
	
	/**
	 * Indica si el lexema está o no insertado en el propio ámbito o en los superiores
	 * a él, en el caso de los métodos solo tienen como ámbito superior las variables 
	 * globales(o tabla para las variables globales)
	 * @param lexema
	 * @return
	 */
	public boolean estaLexema(String lexema);
	
	
	/**
	 * Como get que es, devuelve null si no existe (comprobar antes con estaLexema)
	 * @param lexema
	 * @return
	 */
	public Atributos getAtributos(String lexema);
	
//	/**
//	 * Devuelve una lista con los atributos visibles desde el ámbito actual
//	 * @return LinkedList<Atributos>
//	 */
//	public LinkedList<Atributos> listarAmbitoActual();
//	
//	/**
//	 * Devuelve una lista con los atributos de todos los lexemas del programa.
//	 * No debe realizarse esta llamada hasta haber cerrado todos los ámbitos (fin del análisis)
//	 * @return LinkedList<Atributos> 
//	 */
//	public LinkedList<Atributos> listarTodos();
	

}
