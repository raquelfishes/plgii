/* Generated By:JJTree: Do not edit this line. NodoIgualIgual.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoIgualIgual extends SimpleNode {
  public NodoIgualIgual(int id) {
    super(id);
    value = ConstantesTipos.BOOLEAN;
  }

  public NodoIgualIgual(Compilador p, int id) {
    super(p, id);
    value = ConstantesTipos.BOOLEAN;
  }

  public void interpret()
  {      
	  
	 //super.comprobacionBooleana(); 
	 
	 
     int i, k = jjtGetNumChildren();

     for (i = 0; i < k; i++)
        jjtGetChild(i).interpret();

  }
}
/* JavaCC - OriginalChecksum=1f10e683ff3a2d0252355ce591d92569 (do not edit this line) */
