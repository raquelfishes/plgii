/* Generated By:JJTree: Do not edit this line. Nodosentencia_for.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodosentencia_for extends SimpleNode {
  public Nodosentencia_for(int id) {
    super(id);
  }

  public Nodosentencia_for(Compilador p, int id) {
    super(p, id);
  }

	public void interpret() {
		
		Compilador.gestorTS.nuevoAmbito("for");
		System.out.println("Abriendo ambito: for");
		int i, k = jjtGetNumChildren();

		for (i = 0; i < k; i++)
			jjtGetChild(i).interpret();
		
		Compilador.gestorTS.cierraAmbito();
		System.out.println("Cerrando ambito: for");
	}
}
/* JavaCC - OriginalChecksum=0ab76c19c8bca2ef552d7992f060a3ab (do not edit this line) */
