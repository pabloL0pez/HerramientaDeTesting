package analizador;

public class Comentarios {
	
	private boolean enComentario = false;
	private int cantComentarios = 0;
	private int cantLineas = 0;
	
	public Comentarios() {
		this.enComentario = false;
		this.cantComentarios = 0;
		this.cantLineas = 0;
	}

	public void analizarLinea(String linea) {
		if (linea.contains("//")) cantComentarios++;
		else if (linea.contains("/*")){
			cantComentarios++;
			enComentario = true;
		}
		else if (enComentario) {
			if (linea.contains("*/")) {
				enComentario = false;
			}
			cantComentarios++;
		}
		cantLineas++;
	}
	
	public int getCantidadComentarios() {
		return cantComentarios;
	}
	
	public int getCantidadLineas() {
		return cantLineas;
	}
	
	public float getPorcentajeComentarios() {
		return ((float)getCantidadComentarios()/getCantidadLineas())*100;
	}
	
	public String toString() {
		return "Cantidad de comentarios: "+getCantidadComentarios()+"\nCantidad de lineas: "+getCantidadLineas()+"\n% de comentarios: "+getPorcentajeComentarios();
	}
}
