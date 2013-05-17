//ASIGNACIONES Y MÉTODOS

//Este programa va a asignar 800 a la variable global "w" y 600 a la variable global "h", mediante
//llamadas a funciones y paso de parámetros

public class Prueba {
	
	int w = 1;
	int h = 2;
	int l = 3;
	
	void main(){
		int width = 800;
		llamada1(width);
	}
	
	void llamada1(int width){
		int height = 600;
		llamada2(width, height);
	}
	
	void llamada2(int width, int height) {
		int length = 100;
		llamada3(width, height, length);
	}
	
	void llamada3(int width, int height, int length){
		w = width;
		h = height;
		l = length;
	}
}
