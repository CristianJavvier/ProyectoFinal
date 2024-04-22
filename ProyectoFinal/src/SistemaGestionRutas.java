import java.util.*;

public class SistemaGestionRutas {
    private Grafo grafo;
    private Scanner scanner;

    public SistemaGestionRutas() {
        this.grafo = new Grafo();
        this.scanner = new Scanner(System.in);
    }

    // agrega una ubicación como un nodo en el grafo
    // parametros ubicacion para poder agregarla al nodo
    public void agregarUbicacion(Ubicacion ubicacion) {
        Nodo nodo = new Nodo(grafo, ubicacion);
        grafo.agregarNodo(nodo);
    }

    // agrega una conexión entre dos ubicaciones
    /* parametros origen
                  destino
                  distancia	
    */
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

    // busca un nodo en el grafo por su ubicación
    /* parametros ubicacion
    */
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

    
    public double[][] floydWarshall() {
        int n = grafo.getCantidadNodos();
        double[][] distancias = new double[n][n];

        // Inicializar matriz de distancias
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Llenar matriz con distancias conocidas
        List<Nodo> nodos = grafo.getNodos();
        for (int i = 0; i < n; i++) {
            Nodo nodo = nodos.get(i);
            for (Arista arista : nodo.obtenerAristas()) {
                Nodo vecino = arista.getDestino();
                int j = nodos.indexOf(vecino);
                distancias[i][j] = arista.getDistancia();
            }
        }

        // Algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        return distancias;
    }
    // Método principal para el ejemplo de uso
    public static void main(String[] args) {
        // Ejemplo de uso del sistema
        SistemaGestionRutas sistema = new SistemaGestionRutas();
        Scanner scanner = new Scanner(System.in);

        int opcion;
        do {
        	System.out.println("\nMenú:");
            System.out.println("1. Agregar ubicación");
            System.out.println("2. Agregar conexión entre ubicaciones");
            System.out.println("3. Calcular ruta más corta entre dos ubicaciones");
            System.out.println("4. Encontrar árbol de expansión mínima usando Prim");
            System.out.println("5. Encontrar árbol de expansión mínima usando Kruskal");
            System.out.println("6. Calcular camino más corto entre todas las ubicaciones (Floyd-Warshall)");
            System.out.println("7. Salir");
            System.out.print("Ingrese su opción: ");
            opcion = Integer.parseInt(sistema.scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre de la ubicación:");
                    String nombre = scanner.nextLine();
                    System.out.println("Ingrese la latitud de la ubicación:");
                    double latitud = Double.parseDouble(scanner.nextLine());
                    System.out.println("Ingrese la longitud de la ubicación:");
                    double longitud = Double.parseDouble(scanner.nextLine());
                    Ubicacion ubicacion = new Ubicacion(nombre, latitud, longitud);
                    sistema.agregarUbicacion(ubicacion);
                    break;
                case 2:
                    System.out.println("Ingrese el nombre de la ubicación de origen:");
                    String nombreOrigen = scanner.nextLine();
                    System.out.println("Ingrese el nombre de la ubicación de destino:");
                    String nombreDestino = scanner.nextLine();
                    System.out.println("Ingrese la distancia entre las ubicaciones:");
                    double distancia = Double.parseDouble(scanner.nextLine());
                    Ubicacion origen = new Ubicacion(nombreOrigen, 0.0, 0.0);
                    Ubicacion destino = new Ubicacion(nombreDestino, 0.0, 0.0);
                    sistema.agregarConexion(origen, destino, distancia);
                    break;
                case 3:
                    System.out.println("Ingrese el nombre de la ubicación de origen:");
                    nombreOrigen = scanner.nextLine();
                    System.out.println("Ingrese el nombre de la ubicación de destino:");
                    nombreDestino = scanner.nextLine();
                    Ubicacion ubicacionOrigen = new Ubicacion(nombreOrigen, 0.0, 0.0);
                    Ubicacion ubicacionDestino = new Ubicacion(nombreDestino, 0.0, 0.0);
                    List<Nodo> rutaCorta = sistema.calcularRutaMasCorta(ubicacionOrigen, ubicacionDestino);
                    System.out.println("Ruta más corta:");
                    if (rutaCorta != null) {
                        for (Nodo nodo : rutaCorta) {
                            System.out.println(nodo.getUbicacion().getNombre());
                        }
                    }
                    break;
                case 4:
                    List<Arista> arbolPrim = sistema.encontrarArbolExpansionMinimaPrim();
                    System.out.println("Árbol de expansión mínima usando Prim:");
                    for (Arista arista : arbolPrim) {
                        System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
                    }
                    break;
                case 5:
                    List<Arista> arbolKruskal = sistema.encontrarArbolExpansionMinimaKruskal();
                    System.out.println("Árbol de expansión mínima usando Kruskal:");
                    for (Arista arista : arbolKruskal) {
                        System.out.println(arista.getOrigen().getUbicacion().getNombre() + " - " + arista.getDestino().getUbicacion().getNombre());
                    }
                    break;
                case 6:
                    // Calcular camino más corto entre todas las ubicaciones (Floyd-Warshall)
                    double[][] distancias = sistema.floydWarshall();
                    System.out.println("\nMatriz de distancias usando Floyd-Warshall:");
                    for (int i = 0; i < distancias.length; i++) {
                        for (int j = 0; j < distancias[i].length; j++) {
                            if (distancias[i][j] == Double.POSITIVE_INFINITY) {
                                System.out.print("INF\t");
                            } else {
                                System.out.printf("%.2f\t", distancias[i][j]);
                            }
                        }
                        System.out.println();
                    }
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese una opción válida.");
            }
        } while (opcion != 7);
        scanner.close();
    }
}
