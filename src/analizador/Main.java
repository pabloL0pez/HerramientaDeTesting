package analizador;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		// COMO SE USA?
		
		// EL OBJETO CLASE TOMA UN ARCHIVO Y LO ANALIZA AUTOMATICAMENTE
		// LEEMOS EL ARCHIVO EN LA CLASE:
		Clase c = null;
		try {
			c = new Clase("C:\\Users\\PabloJoel\\Desktop\\analizador 2.0\\FanOut.java");
		} catch (IOException e) {
			System.err.println("ARCHIVO NO ENCONTRADO");
		}
		
		// PARA OBTENER LOS METODOS DE LA CLASE:
		c.getMetodos(); //devuelve un arraylist de metodos
		
		// EJEMPLO ITERACION
		System.out.println("EJEMPLO ITERACION\n");
		for (Metodo m : c.getMetodos()) {
			System.out.println(m + "\n");
		}
		
		// QUE TIENE METODO PARA HACER:
		Metodo metodo = c.getMetodos().get(0); // OBTENGO UN METODO CUALQUIERA PARA EJEMPLO
		metodo.getCantidadLineas();
		metodo.getCantidadComentarios();
		metodo.getComplejidadCiclomatica();
		metodo.getLongitud();
		metodo.getVolumen();
		metodo.getEsfuerzo();
		metodo.getFanIn();
		metodo.getFanOut();
	}

}
