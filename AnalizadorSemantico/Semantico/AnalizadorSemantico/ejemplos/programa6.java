public class Prueba {
	int i, k;
	int b = 100;
	boolean a;
	
	int llamada(int x){
		x = x+b;
		return x;
	}
	
	void main(){
		i = 25;
		k = llamada (i);
	}
}
