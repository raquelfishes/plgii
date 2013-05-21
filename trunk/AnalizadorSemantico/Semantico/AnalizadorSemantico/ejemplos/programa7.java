//RECURSION

//Vemos como se pueden emplear funciones recursivas 

public class Prueba {
	
	int a = 6;
	int fact = 0;
	
	int factorial_rec(int x) {
		int f = 1;
		if (x>1) {
			int x2 = x;
			x2 = x2-1;
			f = factorial_rec(x2);
			f = f*x;
		}
		return f;
	}
	
	void main() {
		fact = factorial_rec(a);
	}
	
	
}
