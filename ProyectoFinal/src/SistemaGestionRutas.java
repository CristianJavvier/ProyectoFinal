import java.util.*;


// Clase que representa un sistema de gestión de rutas
public class SistemaGestionRutas {
    private Grafo grafo;
    
    public SistemaGestionRutas() {
        this.grafo = new Grafo();
    }
    
    // Método para agregar una ubicación como un nodo en el grafo
    public void agregarUbicacion(Ubicacion ubicacion) {
        Nodo nodo = new Nodo(grafo, ubicacion);
        grafo.agregarNodo(nodo);
    }
    
    // Método para agregar una conexión entre dos ubicaciones
    public void agregarConexion(Ubicacion origen, Ubicacion destino, double distancia) {
        Nodo nodoOrigen = buscarNodoPorUbicacion(origen);
        Nodo nodoDestino = buscarNodoPorUbicacion(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            grafo.agregarArista(nodoOrigen, nodoDestino, distancia);
        } else {
            // Manejo de error si no se encuentra alguno de los nodos
            System.out.println("Error: No se encontró alguna de las ubicaciones.");
        }
    }
    
    // Método para buscar un nodo en el grafo por su ubicación
    private Nodo buscarNodoPorUbicacion(Ubicacion ubicacion) {
        String nombreUbicacion = ubicacion.getNombre().toLowerCase(); // Convertir a minúsculas
        for (Nodo nodo : grafo.getNodos()) {
            if (nodo.getUbicacion().getNombre().toLowerCase().equals(nombreUbicacion)) { // Convertir a minúsculas
                return nodo;
            }
        }
        return null;
    }
    
    // Algoritmo de Dijkstra para calcular la ruta más corta entre dos ubicaciones
    public List<Nodo> calcularRutaMasCorta(Ubicacion origen, Ubicacion destino) {
        Nodo nodoOrigen = buscarNodoPorUbicacion(origen);
        Nodo nodoDestino = buscarNodoPorUbicacion(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            return dijkstra(nodoOrigen, nodoDestino);
        } else {
            System.out.println("Error: No se encontró alguna de las ubicaciones.");
            return null;
        }
    }
    
    // Algoritmo de Prim para encontrar un árbol de expansión mínima
    public List<Arista> encontrarArbolExpansionMinimaPrim() {
        return prim(grafo);
    }
    
    // Algoritmo de Kruskal para encontrar un árbol de expansión mínima
    public List<Arista> encontrarArbolExpansionMinimaKruskal() {
        return kruskal(grafo);
    }
    
    // Algoritmo de Dijkstra para calcular la ruta más corta entre dos nodos
    private List<Nodo> dijkstra(Nodo origen, Nodo destino) {
        Map<Nodo, Double> distancias = new HashMap<>();
        Map<Nodo, Nodo> predecesores = new HashMap<>();
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        // Inicialización
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
    
    // Algoritmo de Prim para encontrar un árbol de expansión mínima
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
    
    // Algoritmo de Kruskal para encontrar un árbol de expansión mínima
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

    // Método principal para el ejemplo de uso
    public static void main(String[] args) {
        // Ejemplo de uso del sistema
        SistemaGestionRutas sistema = new SistemaGestionRutas();
        Scanner scanner = new Scanner(System.in);

        // Solicitar al usuario que agregue ubicaciones
        System.out.println("Agregar ubicaciones:");
        while (true) {
            System.out.println("Ingrese el nombre de la ubicación (o 'fin' para terminar):");
            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("fin")) {
                break;
            }
            System.out.println("Ingrese la latitud de la ubicación:");
            double latitud = Double.parseDouble(scanner.nextLine());
            System.out.println("Ingrese la longitud de la ubicación:");
            double longitud = Double.parseDouble(scanner.nextLine());
            Ubicacion ubicacion = new Ubicacion(nombre, latitud, longitud);
            sistema.agregarUbicacion(ubicacion);
        }

        // Solicitar al usuario que agregue conexiones entre ubicaciones
        System.out.println("Agregar conexiones entre ubicaciones:");
        while (true) {
            System.out.println("Ingrese el nombre de la ubicación de origen (o 'fin' para terminar):");
            String nombreOrigen = scanner.nextLine();
            if (nombreOrigen.equalsIgnoreCase("fin")) {
                break;
            }
            System.out.println("Ingrese el nombre de la ubicación de destino:");
            String nombreDestino = scanner.nextLine().toLowerCase();
            System.out.println("Ingrese la distancia entre las ubicaciones:");
            double distancia = Double.parseDouble(scanner.nextLine());
            
            Ubicacion origen = new Ubicacion(nombreOrigen, 0.0, 0.0);
            Ubicacion destino = new Ubicacion(nombreDestino, 0.0, 0.0);
            
            sistema.agregarConexion(origen, destino, distancia);
        }

        // Solicitar al usuario los nodos de origen y destino para calcular la ruta más corta
        System.out.println("Ingrese el nombre de la ubicación de origen para calcular la ruta más corta:");
        String nombreOrigen = scanner.nextLine();
        System.out.println("Ingrese el nombre de la ubicación de destino para calcular la ruta más corta:");
        String nombreDestino = scanner.nextLine();
        
        Ubicacion ubicacionOrigen = new Ubicacion(nombreOrigen, 0.0, 0.0);
        Ubicacion ubicacionDestino = new Ubicacion(nombreDestino, 0.0, 0.0);
        
        //cálculo de ruta más corta usando Dijkstra
        List<Nodo> rutaCorta = sistema.calcularRutaMasCorta(ubicacionOrigen, ubicacionDestino);
        System.out.println("Ruta más corta usando Dijkstra:");
        if (rutaCorta != null) {
            for (Nodo nodo : rutaCorta) {
                System.out.println(nodo.getUbicacion().getNombre());
            }
        }

        // cálculo de árbol de expansión mínima usando Prim
        List<Arista> arbolPrim = sistema.encontrarArbolExpansionMinimaPrim();
        System.out.println("\nÁrbol de expansión mínima usando Prim:");
        for (Arista arista : arbolPrim) {
            System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
        }

        // cálculo de árbol de expansión mínima usando Kruskal
        List<Arista> arbolKruskal = sistema.encontrarArbolExpansionMinimaKruskal();
        System.out.println("\nÁrbol de expansión mínima usando Kruskal:");
        for (Arista arista : arbolKruskal) {
            System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
        }
        scanner.close();
    }
}
