/* Generated By:JJTree: Do not edit this line. NodoDiv.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class NodoDiv extends SimpleNode {
  public NodoDiv(int id) {
    super(id);
    value = ConstantesTipos.INT;
  }

  public NodoDiv(Compilador p, int id) {
    super(p, id);
    value = ConstantesTipos.INT;
  }
  
  public void interpret()
  { 
	 
	  super.comprobacionNumerica(); 
	  
//    stack[--top] = new Integer(((Integer)stack[top]).intValue() +
//    ((Integer)stack[top + 1]).intValue());
	  
  }

}
/* JavaCC - OriginalChecksum=f85fc919a6efe63d62a13896fc309993 (do not edit this line) */
