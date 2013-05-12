package tabla.simbolos.v2;

import java.util.LinkedList;

public interface IGestorTS {
	
	/**
	 * Partiendo de que la idea es que cada �mbito tenga una tabla de s�mbolos asignada...
	 */
	
	
	/**
	 * Si es palabra reservada no se debe insertar en la tabla 
	 * @param lexema identificador encontrado en la fase de an�lisis
	 * @return true si es una palabra reservada, false en c.c.
	 */
	public boolean esPalabraReservada(String lexema);
	
	/**
	 * Nuevo �mbito es equivalente a decir que se quiere una nueva tabla de s�mbolos para
	 * un nuevo m�todo.
	 * 
	 * Aunque lo ideal ser�a que si no se ha llamado a�n al cierraAmbito, el m�todo
	 * nuevoAmbito() cree un nuevo subAmbito y por tanto una nueva tabla de s�mbolos
	 * para ella. 
	 * 
	 * Los subAmbitos est�n pensados para las estructuras de control: if,else, while, for, etc.
	 */
	public void nuevoAmbito();
	
	/**
	 * Es importante cerrar el �mbito, porque as�, al llamar de nuevo al m�todo
	 * nuevoAmbito() se podr�n tener �mbitos en paralelo sin que ellos puedan observarse.
	 * Por ejemplo, los m�todos de java no pueden observar su contenido entre ellos.
	 */
	public void cierraAmbito();
	
	/**
	 * Se insertar� un elemento en el �mbito actual. Se deber� considerar antes si insertarlo
	 * comprobando si ya esta con la funci�n estaLexema(String lexema).
	 * Esto debe ser as� ya que el an�lisis del fichero es de arriba hacia abajo,
	 * por tanto, s� en la fase de an�lisis que es en la que se supone que vamos 
	 * reconociendo tokens, nos encontramos con el s�mbolo "{" significar�a que
	 * deber�amos abrir un nuevo �mbito o un nuevo subAmbito, y cuando finalmente
	 * se reconozca el s�mbolo "}" debemos cerrar el �mbito o subAmbito para as�
	 * asegurar el paralelismo entre �mbitos con �mbitos y subAmbitos con subAmbitos. 
	 * @param lexema
	 */
	public void insertar(String lexema, Atributos atributos);
	
	/**
	 * Indica si el lexema est� o no insertado en el propio �mbito o en los superiores
	 * a �l, en el caso de los m�todos solo tienen como �mbito superior las variables 
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
//	 * Devuelve una lista con los atributos visibles desde el �mbito actual
//	 * @return LinkedList<Atributos>
//	 */
//	public LinkedList<Atributos> listarAmbitoActual();
//	
//	/**
//	 * Devuelve una lista con los atributos de todos los lexemas del programa.
//	 * No debe realizarse esta llamada hasta haber cerrado todos los �mbitos (fin del an�lisis)
//	 * @return LinkedList<Atributos> 
//	 */
//	public LinkedList<Atributos> listarTodos();
	

}
