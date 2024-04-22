import java.util.ArrayList;
import java.util.List;

public class Grafo {
    private List<Nodo> nodos;
    private List<Arista> aristas;

    public Grafo() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    // Métodos para agregar nodos y aristas al grafo
    public void agregarNodo(Nodo nodo) {
        nodos.add(nodo);
    }

    public void agregarArista(Nodo origen, Nodo destino, double distancia) {
        Arista arista = new Arista(origen, destino, distancia);
        aristas.add(arista);
        origen.agregarArista(destino, distancia);
    }

    public List<Nodo> getNodos() {
        return nodos;
    }

    public List<Arista> getAristas() {
        return aristas;
    }
}