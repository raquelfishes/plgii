/* Generated By:JJTree: Do not edit this line. Nodoliteral_integer.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

public
class Nodoliteral_integer extends SimpleNode {
	int val;
  public Nodoliteral_integer(int id) {
    super(id);
  }

  public Nodoliteral_integer(Compilador p, int id) {
    super(p, id);
  }
  
  
  public void interpret()
  {
	 
     stack[++top] = new Integer(val);
  }

}
/* JavaCC - OriginalChecksum=d7a7c6a3cc6511ec62f97ddbf436d874 (do not edit this line) */
