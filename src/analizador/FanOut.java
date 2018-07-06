package analizador;

import java.util.ArrayList;

public class FanOut {
	
	

	public static void analizar(Clase c) {
		ArrayList<Metodo> metodos = c.getMetodos();
		Metodo aux;
		for (Metodo m : metodos) {
			for (String n : m.getListaFanOut()) {
				if ((aux = buscarMetodo(n, metodos)) != null) {
					aux.agregarFanIn(m.getNombre());
				}
				
			}
		}
		
	}
	
	public static Metodo buscarMetodo(String nombre, ArrayList<Metodo> lista) {
		for (Metodo m : lista) {
			if (m.getNombre().equals(nombre)) {
				return m;
			}
		}
		return null;
	}
	
}
