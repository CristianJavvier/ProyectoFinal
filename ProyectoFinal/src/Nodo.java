import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private Ubicacion ubicacion;
    private List<Arista> aristas;
    private Grafo grafo;

    public Nodo(Grafo grafo, Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        this.aristas = new ArrayList<>();
        this.grafo = grafo;
    }

    // Métodos para agregar y obtener aristas
    public void agregarArista(Nodo destino, double distancia) {
        Arista arista = new Arista(this, destino, distancia);
        aristas.add(arista);
    }

    public List<Arista> obtenerAristas() {
        return aristas;
    }

    // Getter para obtener la ubicación del nodo
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    // Getter para obtener el grafo al que pertenece el nodo
    public Grafo getGrafo() {
        return grafo;
    }
}