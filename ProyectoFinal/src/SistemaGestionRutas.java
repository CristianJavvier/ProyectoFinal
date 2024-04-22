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
    
    public Nodo(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        this.aristas = new ArrayList<>();
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

// Clase principal que contiene la l�gica del sistema
public class SistemaGestionRutas {
    private List<Nodo> nodos;
    
    public SistemaGestionRutas() {
        this.nodos = new ArrayList<>();
    }
    
    // M�todo para agregar una ubicaci�n como un nodo en el grafo
    public void agregarUbicacion(Ubicacion ubicacion) {
        Nodo nodo = new Nodo(ubicacion);
        nodos.add(nodo);
    }
    
    // M�todo para agregar una conexi�n entre dos ubicaciones
    public void agregarConexion(Ubicacion origen, Ubicacion destino, double distancia) {
        Nodo nodoOrigen = buscarNodoPorUbicacion(origen);
        Nodo nodoDestino = buscarNodoPorUbicacion(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            nodoOrigen.agregarArista(nodoDestino, distancia);
        } else {
            // Manejo de error si no se encuentra alguno de los nodos
            System.out.println("Error: No se encontr� alguna de las ubicaciones.");
        }
    }
    
    // M�todo para buscar un nodo en el grafo por su ubicaci�n
    private Nodo buscarNodoPorUbicacion(Ubicacion ubicacion) {
        for (Nodo nodo : nodos) {
            if (nodo.getUbicacion().equals(ubicacion)) {
                return nodo;
            }
        }
        return null;
    }
    
    // Otros m�todos para editar y eliminar ubicaciones podr�an ser implementados aqu�
    
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
        
    }
}