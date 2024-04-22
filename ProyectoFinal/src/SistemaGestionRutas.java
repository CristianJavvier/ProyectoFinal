import java.util.*;

// Clase que representa una ubicaci�n
class Ubicacion {
    private String nombre;
    private double latitud;
    private double longitud;
    
    public Ubicacion(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    
    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getLatitud() {
        return latitud;
    }
    
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    
    public double getLongitud() {
        return longitud;
    }
    
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

// Clase que representa un nodo en el grafo
class Nodo {
    private Ubicacion ubicacion;
    private List<Arista> aristas;
    private Grafo grafo; // Referencia al grafo al que pertenece el nodo
    
    public Nodo(Grafo grafo, Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        this.aristas = new ArrayList<>();
        this.grafo = grafo;
    }
    
    // M�todos para agregar y obtener aristas
    public void agregarArista(Nodo destino, double distancia) {
        Arista arista = new Arista(this, destino, distancia);
        aristas.add(arista);
    }
    
    public List<Arista> obtenerAristas() {
        return aristas;
    }
    
    // Getter para obtener la ubicaci�n del nodo
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    // Getter para obtener el grafo al que pertenece el nodo
    public Grafo getGrafo() {
        return grafo;
    }
}

// Clase que representa una arista en el grafo
class Arista {
    private Nodo origen;
    private Nodo destino;
    private double distancia;
    
    public Arista(Nodo origen, Nodo destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }
    
    // Getters para obtener origen, destino y distancia de la arista
    public Nodo getOrigen() {
        return origen;
    }
    
    public Nodo getDestino() {
        return destino;
    }
    
    public double getDistancia() {
        return distancia;
    }
}

// Clase que representa un grafo
class Grafo {
    private List<Nodo> nodos;
    private List<Arista> aristas;
    
    public Grafo() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }
    
    // M�todos para agregar nodos y aristas al grafo
    public void agregarNodo(Nodo nodo) {
        nodos.add(nodo);
    }
    
    public void agregarArista(Nodo origen, Nodo destino, double distancia) {
        Arista arista = new Arista(origen, destino, distancia);
        aristas.add(arista);
        origen.agregarArista(destino, distancia);
    }
    
    // Getters para obtener nodos y aristas del grafo
    public List<Nodo> getNodos() {
        return nodos;
    }
    
    public List<Arista> getAristas() {
        return aristas;
    }
}

// Clase principal que contiene la l�gica del sistema
public class SistemaGestionRutas {
    private Grafo grafo;
    
    public SistemaGestionRutas() {
        this.grafo = new Grafo();
    }
    
    // M�todo para agregar una ubicaci�n como un nodo en el grafo
    public void agregarUbicacion(Ubicacion ubicacion) {
        Nodo nodo = new Nodo(grafo, ubicacion);
        grafo.agregarNodo(nodo);
    }
    
    // M�todo para agregar una conexi�n entre dos ubicaciones
    public void agregarConexion(Ubicacion origen, Ubicacion destino, double distancia) {
        Nodo nodoOrigen = buscarNodoPorUbicacion(origen);
        Nodo nodoDestino = buscarNodoPorUbicacion(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            grafo.agregarArista(nodoOrigen, nodoDestino, distancia);
        } else {
            // Manejo de error si no se encuentra alguno de los nodos
            System.out.println("Error: No se encontr� alguna de las ubicaciones.");
        }
    }
    
    // M�todo para buscar un nodo en el grafo por su ubicaci�n
    private Nodo buscarNodoPorUbicacion(Ubicacion ubicacion) {
        for (Nodo nodo : grafo.getNodos()) {
            if (nodo.getUbicacion().equals(ubicacion)) {
                return nodo;
            }
        }
        return null;
    }
    
    // Algoritmo de Dijkstra para calcular la ruta m�s corta entre dos ubicaciones
    public List<Nodo> calcularRutaMasCorta(Ubicacion origen, Ubicacion destino) {
        Nodo nodoOrigen = buscarNodoPorUbicacion(origen);
        Nodo nodoDestino = buscarNodoPorUbicacion(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            return dijkstra(nodoOrigen, nodoDestino);
        } else {
            System.out.println("Error: No se encontr� alguna de las ubicaciones.");
            return null;
        }
    }
    
    // Algoritmo de Prim para encontrar un �rbol de expansi�n m�nima
    public List<Arista> encontrarArbolExpansionMinimaPrim() {
        return prim(grafo);
    }
    
    // Algoritmo de Kruskal para encontrar un �rbol de expansi�n m�nima
    public List<Arista> encontrarArbolExpansionMinimaKruskal() {
        return kruskal(grafo);
    }
    
    // Algoritmo de Dijkstra para calcular la ruta m�s corta entre dos nodos
    private List<Nodo> dijkstra(Nodo origen, Nodo destino) {
        Map<Nodo, Double> distancias = new HashMap<>();
        Map<Nodo, Nodo> predecesores = new HashMap<>();
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        // Inicializaci�n
        for (Nodo nodo : origen.getGrafo().getNodos()) {
            distancias.put(nodo, Double.POSITIVE_INFINITY);
            predecesores.put(nodo, null);
        }
        distancias.put(origen, 0.0);
        colaPrioridad.add(origen);

        // Bucle principal
        while (!colaPrioridad.isEmpty()) {
            Nodo actual = colaPrioridad.poll();
            if (actual.equals(destino)) break;
            for (Arista arista : actual.obtenerAristas()) {
                Nodo vecino = arista.getDestino();
                double nuevaDistancia = distancias.get(actual) + arista.getDistancia();
                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    predecesores.put(vecino, actual);
                    colaPrioridad.add(vecino);
                }
            }
        }

        // Reconstruir la ruta
        List<Nodo> ruta = new ArrayList<>();
        for (Nodo nodo = destino; nodo != null; nodo = predecesores.get(nodo)) {
            ruta.add(nodo);
        }
        Collections.reverse(ruta);
        return ruta;
    }
    
    // Algoritmo de Prim para encontrar un �rbol de expansi�n m�nima
    private List<Arista> prim(Grafo grafo) {
        List<Arista> arbolExpMinima = new ArrayList<>();
        Set<Nodo> visitados = new HashSet<>();
        PriorityQueue<Arista> colaAristas = new PriorityQueue<>(Comparator.comparingDouble(Arista::getDistancia));

        Nodo primerNodo = grafo.getNodos().get(0);
        visitados.add(primerNodo);
        colaAristas.addAll(primerNodo.obtenerAristas());

        while (!colaAristas.isEmpty()) {
            Arista arista = colaAristas.poll();
            Nodo origen = arista.getOrigen();
            Nodo destino = arista.getDestino();
            if (visitados.contains(origen) && !visitados.contains(destino)) {
                visitados.add(destino);
                arbolExpMinima.add(arista);
                colaAristas.addAll(destino.obtenerAristas());
            }
        }

        return arbolExpMinima;
    }
    
    // Algoritmo de Kruskal para encontrar un �rbol de expansi�n m�nima
    private List<Arista> kruskal(Grafo grafo) {
        List<Arista> aristasOrdenadas = new ArrayList<>(grafo.getAristas());
        aristasOrdenadas.sort(Comparator.comparingDouble(Arista::getDistancia));

        DisjointSetUnion dsu = new DisjointSetUnion(grafo.getNodos());
        List<Arista> arbolExpMinima = new ArrayList<>();

        for (Arista arista : aristasOrdenadas) {
            Nodo origen = arista.getOrigen();
            Nodo destino = arista.getDestino();
            if (dsu.find(origen) != dsu.find(destino)) {
                arbolExpMinima.add(arista);
                dsu.union(origen, destino);
            }
        }

        return arbolExpMinima;
    }

    // Clase para representar el conjunto disjunto (Disjoint Set Union)
    static class DisjointSetUnion {
        private Map<Nodo, Nodo> padre;

        public DisjointSetUnion(List<Nodo> nodos) {
            padre = new HashMap<>();
            for (Nodo nodo : nodos) {
                padre.put(nodo, nodo);
            }
        }

        public Nodo find(Nodo nodo) {
            if (padre.get(nodo) == nodo) {
                return nodo;
            }
            Nodo p = find(padre.get(nodo));
            padre.put(nodo, p);
            return p;
        }

        public void union(Nodo x, Nodo y) {
            padre.put(find(x), find(y));
        }
    }

    // M�todo principal para el ejemplo de uso
    public static void main(String[] args) {
        // Ejemplo de uso del sistema
        SistemaGestionRutas sistema = new SistemaGestionRutas();
        
        // Agregar ubicaciones
        Ubicacion ubicacionA = new Ubicacion("A", 0.0, 0.0);
        Ubicacion ubicacionB = new Ubicacion("B", 1.0, 1.0);
        sistema.agregarUbicacion(ubicacionA);
        sistema.agregarUbicacion(ubicacionB);
        
        // Agregar conexi�n entre ubicaciones
        sistema.agregarConexion(ubicacionA, ubicacionB, 10.0);
        
        // Ejemplo de c�lculo de ruta m�s corta usando Dijkstra
        List<Nodo> rutaCorta = sistema.calcularRutaMasCorta(ubicacionA, ubicacionB);
        System.out.println("Ruta m�s corta usando Dijkstra:");
        if (rutaCorta != null) {
            for (Nodo nodo : rutaCorta) {
                System.out.println(nodo.getUbicacion().getNombre());
            }
        }

        // Ejemplo de c�lculo de �rbol de expansi�n m�nima usando Prim
        List<Arista> arbolPrim = sistema.encontrarArbolExpansionMinimaPrim();
        System.out.println("\n�rbol de expansi�n m�nima usando Prim:");
        for (Arista arista : arbolPrim) {
            System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
        }

        // Ejemplo de c�lculo de �rbol de expansi�n m�nima usando Kruskal
        List<Arista> arbolKruskal = sistema.encontrarArbolExpansionMinimaKruskal();
        System.out.println("\n�rbol de expansi�n m�nima usando Kruskal:");
        for (Arista arista : arbolKruskal) {
            System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
        }
    }
}
