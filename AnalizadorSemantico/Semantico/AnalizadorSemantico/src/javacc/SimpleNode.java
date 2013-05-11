/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=Nodo,NODE_EXTENDS=XNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc;

import tabla.simbolos.Atributos;

public
class SimpleNode extends XNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Compilador parser;
  protected Token firstToken;
  protected Token lastToken;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(Compilador p, int i) {
    this(i);
    parser = p;
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  public Token jjtGetFirstToken() { return firstToken; }
  public void jjtSetFirstToken(Token token) { this.firstToken = token; }
  public Token jjtGetLastToken() { return lastToken; }
  public void jjtSetLastToken(Token token) { this.lastToken = token; }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() { return CompiladorTreeConstants.jjtNodeName[id]; }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + " ");
        }
      }
    }
  }
  
  public Object getValor(){
	  return value;
  }
  
  public void setValor(Object value){
	  this.value = value;
  }
  
  public void comprobacionNumerica(){
	  
	  if(esTipoOperacionNumerica(children[0]))
		  children[0].interpret();
	  
	  if(esTipoOperacionNumerica(children[1]))
		  children[1].interpret();

	  
	  if( ! (	(esTipoNumerico(children[0])) && 
			  	(esTipoNumerico(children[1]))	) ){
		  
		  addErrSemantico(firstToken.beginLine, "los operadores " 
				  + (String)((SimpleNode)children[0]).value + " y "
				  + (String)((SimpleNode)children[1]).value +
				  " no son de tipo num�rico");
	  }
	  
  }
  
  private boolean esTipoOperacionNumerica(Node nodo){
	  return (nodo instanceof NodoAdd) || (nodo instanceof NodoSub) || (nodo instanceof NodoMult) ||(nodo instanceof NodoDiv) ||
			 (nodo instanceof NodoMod); 
  }
  
  private boolean esTipoNumerico(Node nodo){
	  
	  if(nodo instanceof Nodoidentificador){
		  Object s = ((SimpleNode)children[1]).getValor();
		  if(s!=null){
			  boolean esta = Compilador.gestorTS.estaLexema(s.toString());
			  if(esta){
				  Atributos at = Compilador.gestorTS.getAtributos(s.toString());
				  String tipo = at.getTipo();
				  if(tipo.equals("int") || tipo.equals("float")){
					  return true;
				  }
			  }
		  }
	  }
	  
	  return (nodo instanceof NodoLiteralInteger) || (nodo instanceof NodoLiteralFloat) || esTipoOperacionNumerica(nodo); 
  }
  
  
  
  //FIXME PARAMETRIZAR!!
 public void comprobacionBooleana(){
	  
	  if(esTipoOperacionBooleana(children[0]))
		  children[0].interpret();
	  
	  if(esTipoOperacionBooleana(children[1]))
		  children[1].interpret();

	  
	  if( ! (	(esTipoBoolean(children[0])) && 
			  	(esTipoBoolean(children[1]))	) ){
		  
		  addErrSemantico(firstToken.beginLine, "los operadores " 
				  + (String)((SimpleNode)children[0]).value + " y "
				  + (String)((SimpleNode)children[1]).value +
				  " no son de tipo boolean");
	  }
	  
  }
 
 private boolean esTipoOperacionBooleana(Node nodo){
	  return (nodo instanceof NodoAnd) || (nodo instanceof NodoOr) || (nodo instanceof NodoBAnd) || (nodo instanceof NodoBOr) || 
			 (nodo instanceof NodoIgualIgual) || (nodo instanceof NodoMayor) || (nodo instanceof NodoMayorEq) || 
			 (nodo instanceof NodoMenor) || (nodo instanceof NodoMenorEq) || (nodo instanceof NodoNIgual) || (nodo instanceof NodoNot); 
 }
 
 private boolean esTipoBoolean(Node nodo){
	  
	  if(nodo instanceof Nodoidentificador){
		  Object s = ((SimpleNode)children[1]).getValor();
		  if(s!=null){
			  boolean esta = Compilador.gestorTS.estaLexema(s.toString());
			  if(esta){
				  Atributos at = Compilador.gestorTS.getAtributos(s.toString());
				  String tipo = at.getTipo();
				  if(tipo.equals("boolean")){
					  return true;
				  }
			  }
		  }
	  }
	  
	  return (nodo instanceof NodoLiteralBoolean) || esTipoOperacionBooleana(nodo); 
 }
 
 public void verificarComparacionValida(){
	 
	 Node n1 = children[0];
	 Node n2 = children[1];
	 if(n1==null || n2==null){
		 addErrSemantico(firstToken.beginLine, "los operadores no son del mismo tipo");
		 return;
	 }
	 String tipo1="";
	 if(n1 instanceof Nodoidentificador){
		  Object s = ((SimpleNode)n1).getValor();
		  if(s!=null){
			  boolean esta = Compilador.gestorTS.estaLexema(s.toString());
			  if(esta){
				  Atributos at = Compilador.gestorTS.getAtributos(s.toString());
				  tipo1 = at.getTipo();
			  }
		  }
	 }
	 else{
		 tipo1 = ((SimpleNode)n1).value.toString();
	 }
	 
	 
	 
	 String tipo2="";
	 if(n2 instanceof Nodoidentificador){
		  Object s = ((SimpleNode)n2).getValor();
		  if(s!=null){
			  boolean esta = Compilador.gestorTS.estaLexema(s.toString());
			  if(esta){
				  Atributos at = Compilador.gestorTS.getAtributos(s.toString());
				  tipo2 = at.getTipo();
			  }
		  }
	 }
	 else{
		 tipo2 = ((SimpleNode)n2).value.toString();
	 }
	  
	  if(!ConstantesTipos.esCompatible(tipo1, tipo2)){
		  addErrSemantico(firstToken.beginLine, "los operadores " 
				  + (String)((SimpleNode)children[0]).value + " y "
				  + (String)((SimpleNode)children[1]).value +
				  " no son del mismo tipo");
	  }
 }
}

/* JavaCC - OriginalChecksum=f69512949a6aa4d6f617ae870809b883 (do not edit this line) */
