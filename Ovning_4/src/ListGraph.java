// PROG2 VT2024, Inlämningsuppgift, del 1
// Grupp 127
// Kerstin Lundgren kelu9328
// Alex Wagner xxxx1234
// William Skyllberg yyyy5678
import java.util.*;

public class ListGraph<T> implements Graph<T> {
    private final Map<T, Set<Edge<T>>> adjacencyList = new HashMap<>();//Nycklarna i denna map representerar en node.

    @Override //klar
    public void add(T node) { //klar
        adjacencyList.putIfAbsent(node, new HashSet<>());
    }

    @Override //klar
    public void connect(T node1, T node2, String name, int weight) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2))
            throw new NoSuchElementException("One of the nodes given does not exist");
        if (weight < 0)
            throw new IllegalArgumentException("Weight cannot be negative");

        add(node1);
        add(node2);
        Set<Edge<T>> edges1 = adjacencyList.get(node1);
        Set<Edge<T>> edges2 = adjacencyList.get(node2);

        for (Edge<T> edge : edges1) {
            if (edge.getName().equals(name) && edge.getWeight() == weight && edge.getDestination().equals(node2)) {
                throw new IllegalStateException();
            }
        }
        edges1.add(new Edge<>(name, weight, node2));
        edges2.add(new Edge<>(name, weight, node1));
    }

    @Override //klar
    public void setConnectionWeight(T node1, T node2, int weight) {
        //tar emot två noder och ett heltal (förbindelsens
        //nya vikt) och uppdaterar vikten hos förbindelserna mellan dessa två noder
        //till det angivna heltalet. Om någon av noderna saknas i grafen eller ingen
        //kant finns mellan dessa två noder skall undantaget NoSuchElementException
        // från paketet java.util genereras. Om vikten är negativ skall undantaget
        // IllegalArgumentException genereras.
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)
                || (getEdgeBetween(node1,node2) == null)) {
            throw new NoSuchElementException("One of the nodes does not exist or they are not connected");
        } else {
            getEdgeBetween(node1,node2).setWeight(weight);
            getEdgeBetween(node2, node1).setWeight(weight);
        }
    }

    @Override //klar
    public Set<T> getNodes() {
        // Skapa en kopia av nyckelmängden från adjacency list
        Set<T> nodesCopy = new HashSet<>(adjacencyList.keySet());
        return nodesCopy;
    }

    @Override //klar
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("Node does not exist");
        } else {
                Set<Edge<T>> copy = new HashSet<>(adjacencyList.get(node));
                return copy;
        }

        // tar emot en nod och returnerar en kopia av samlingen
        //av alla kanter som leder från denna nod. Om noden saknas i grafen skall
        //undantaget NoSuchElementException genereras.
    }

    @Override //klar, tar emot två noder och returnerar kanten mellan dessa
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {   // Kollar att båda finns
            throw new NoSuchElementException();
        } else {
            Set<Edge<T>> edges = adjacencyList.get(node1);
            for (Edge<T> edge : edges) {
                if (edge.getDestination().equals(node2)) {
                    return edge;
                  }
            }
        }
        return null;
    }

    @Override //klar
    public void disconnect(T node1, T node2) { //tar emot två noder och tar bort kanten som kopplar ihop
        Edge<T> edgeToRemove1 = getEdgeBetween(node1, node2);
        Edge<T> edgeToRemove2 = getEdgeBetween(node2, node1);
        if (edgeToRemove1 == null || edgeToRemove2 == null )
            throw new IllegalStateException();

        Set<Edge<T>> node1Edges = adjacencyList.get(node1);
        Set<Edge<T>> node2Edges = adjacencyList.get(node2);

        node1Edges.remove(edgeToRemove1);
        node2Edges.remove(edgeToRemove2);
    }

@Override //klar
    public void remove(T node) {
        if (!adjacencyList.containsKey(node)) {
            throw new NoSuchElementException("Node does not exist in graph");
        } else {
            for (Edge<T> edgeToRemove : getEdgesFrom(node)) {
                disconnect(edgeToRemove.getDestination(), node);
            }
            adjacencyList.remove(node);
        }
    //tar emot en nod och tar bort den från grafen. Om noden som
    //skall tas bort saknas i grafen skall undantaget NoSuchElementException
    //från paketet java.util genereras. När en nod tas bort skall även dess
    //kanter tas bort, och eftersom grafen är oriktad skall dessa kanter även tas
    //bort från de andra berörda noderna.

}

    @Override //klar
//tar emot två noder och returnerar true om det finns en väg
//genom grafen från den ena noden till den andra (eventuellt över många
//andra noder), annars returneras false. Om någon av noderna inte finns i
//grafen returneras också false. Använder sig av en hjälpmetod för
// djupetförstsökning genom en graf.
    public boolean pathExists(T node1, T node2) {
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return false;
        } else { //om båda noder skapas ett Set som skickas som paramter till hjälpmetoden boolean pathChecker().
            Set<T> path = new HashSet<>();
            return pathChecker(node1, node2, path); //denna metod returnerar true eller false beroende på om det finns en path.
        }
    }
    private boolean pathChecker(T node1, T node2, Set<T> path) { //hjälpmetod till pathExists som gör själva depth-frirst sökningen:
        if (node1.equals(node2)) { //recursiveConnect
            return true;
        }
        path.add(node1); //lägg till första noden i ett Set
        for (Edge<T> edge : adjacencyList.get(node1)) { //för varje edge som node1 har:
            if (!path.contains(edge.getDestination())) { //om noden i settet inte är destination för den kanten man för nuvaranda är på:
                if (pathChecker(edge.getDestination(), node2, path)) { //då görs ett självanrop vilket skapar en rekursiv loop.
                    return true;
                }
            }
        }
        return false;
    }

    @Override //klar
    public List<Edge<T>> getPath(T startNode, T destinationNode) {
        if (!pathExists(startNode, destinationNode)) { //om det inte finns någon path mellan noder:
            return null;
        } else {
            Map<T, Double> distance = new HashMap<>();
            Map<T, T> path = new HashMap<>();
            Set<T> nodes = new HashSet<>();

            for (T node : adjacencyList.keySet()) {
                distance.put(node, Double.POSITIVE_INFINITY);
                path.put(node, null);
                nodes.add(node);
            }
            distance.put(startNode, 0.0); //innan while-loopen börjar är avståndet från startNode 0.0, till en annan nod (pga inte angiven än).
            //innan kodblocket under börjar exekveras kan man tänka dvs.

            while (!nodes.isEmpty()) {
                T currentNode = null; //placeholder variabel
                double posInf = Double.POSITIVE_INFINITY; //största möjliga double, samt samma värde som var Nod i distance har.
                for (T node : nodes) { //for-loopen isolerar den nod vars distance är minst at the time, från setet nodes.
                    double currentDistance = distance.get(node);
                    if (currentDistance < posInf) {
                        currentNode = node;
                        //posInf = currentDistance;
                    }
                }
                if (currentNode.equals(destinationNode)) { //loopen kör tills destinationNode hittas.
                    break;
                }
                nodes.remove(currentNode);

                //den nod vars distance är minst, går vidare till en ny for-loop där dens edges jämförs.
                for (Edge<T> edge : adjacencyList.get(currentNode)) {
                    // om nodes innehåller en annan node som currentNode delar en edge med:
                    if (nodes.contains(edge.getDestination())) {
                        //plussa ihop currentNodes distance med den andra nodens distance.
                        double weight = distance.get(currentNode) + edge.getWeight(); //weight är samma som distance i kontextet.
                        //om summan nodernas vikt är mindre:
                        if (weight < distance.get(edge.getDestination())) {
                            //läggs nästa nod, samt avståndet till i distance
                            distance.put(edge.getDestination(), weight);
                            //och noderna kan läggas till i path.
                            path.put(edge.getDestination(), currentNode);
                        }
                    }
                }
            }
            LinkedList<Edge<T>> pathAsList = new LinkedList<>();
            T node = destinationNode;
                while (node != null) {
                    T nextNode = path.get(node);
                    if (nextNode != null) {
                        Edge<T> edge = getEdgeBetween(nextNode, node);
                        pathAsList.addFirst(edge);
                    }
                    node = nextNode;
                }
            return pathAsList;
        }
    }
    //– tar emot två noder och returnerar en lista (java.util.List)
    //med kanter som representerar en väg mellan dessa noder genom grafen,
    //eller null om det inte finns någon väg mellan dessa två noder.
    // I den enklaste varianten räcker det alltså att metoden returnerar någon väg mellan
    //de två noderna. En frivillig utökning av uppgiften är att implementera
    //en lösning som returnerar den kortaste vägen (i antalet kanter som måste
    //passeras) eller den snabbaste vägen (med hänsyn tagen till kanternas vikter).

    @Override
    public String toString() { //inte klar
    //returnerar en sträng sammansatt av strängar hämtade från
    //alla de ingående nodernas toString-metoder och deras kanters toStringmetoder,
    // gärna med radbrytningar så att man får information om en nod
    //per rad får förbättrad läsbarhet.
        StringBuilder builder = new StringBuilder("Nodes");
        builder.append("\n");
        for (Map.Entry<T, Set<Edge<T>>> nodeAndEdgeSet : adjacencyList.entrySet()) { //entrySet skapar ett set med både keys och values
            builder.append(nodeAndEdgeSet.getKey()).append(": ").append(nodeAndEdgeSet.getValue()).append("\n");
        }
        return builder.toString();
    }
}
