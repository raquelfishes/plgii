//VALOR DE RETORNO EN FUNCIONES

//Comprobamos que la funci�n devuelve el valor adecuadamente

public class Prueba {
	boolean resultado = false;
	
	int suma(int a){
		//int s = a+b;
		return a+3;
	}
	
	void main(){
		int x = 3;
		int y = 22;
		resultado = suma(x,y);
	}
	
}
