/* Generated By:JJTree: Do not edit this line. Nododeclaracion_metodo.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nododeclaracion_metodo extends SimpleNode {
  public Nododeclaracion_metodo(int id) {
    super(id);
  }

  public Nododeclaracion_metodo(Compilador p, int id) {
    super(p, id);
  }
  
  public void interpret()
  {
	 
	  // Se supone que un metodo siempre estar� compuesto por un tipo_metodo ,
	  // argumentos y finalmente una sentencia_return
	  int numHijos = jjtGetNumChildren();
	  int nodo_tipo_metodo = 0;
	  int nodo_sentencia_return = numHijos - 1;
	
	  // TipoMetodo, al terminar esta sentencia, este
	  // nodo(Nododeclaracion_metodo) ya tiene asignado su tipo
	  jjtGetChild(nodo_tipo_metodo).interpret();
	
	  // Los nodos que estan entre el primero(nodo_tipo_metodo)y el
	  // �ltimo(nodo_sentencia_return)
	  for (int i = nodo_tipo_metodo + 1; i < nodo_sentencia_return; i++)
	          jjtGetChild(i).interpret();
	
	  // Sentencia return
	  jjtGetChild(nodo_sentencia_return).interpret();

  }

}
/* JavaCC - OriginalChecksum=e7d93d282e1b0c942beb6dc404649ff7 (do not edit this line) */
