//VALOR DE RETORNO EN FUNCIONES

//Comprobamos que la función devuelve el valor adecuadamente

public class Prueba {
	int resultado = 0;
	
	void main(){
		int x = 10;
		int y = 22;
		resultado = suma(x,y);
	}
	
	int suma(int a, int b){
		//int s = a+b;
		return a+b;
	}
}
