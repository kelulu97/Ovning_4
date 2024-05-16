// PROG2 VT2024, Inlämningsuppgift, del 1
// Grupp 127
// Kerstin Lundgren kelu9328
// Alex Wagner xxxx1234
// William Skyllberg yyyy5678
public class Edge<T> {
    //en "Edge" i en graph är den linjen som kopplar ihop noder. Dessa linjer har
    //namn, vikt och destination.
    private final String name;
    private int weight;
    private final T destination;

    public Edge (String name, int weight, T destination) {
        this.name = name;
        this.weight = weight;
        this.destination = destination;
    }
    public T getDestination() { // getDestination – returnerar den nod som kanten pekar till.
        return destination;
    }

    public int getWeight() {
        // getWeight – returnerar den aktuella kantens vikt
        return weight;
    }
    public int setWeight(int weight) {
        // setWeight – tar ett heltal och sätter kantens vikt till detta heltal. Om
        // vikten är negativ skall undantaget IllegalArgumentException genereras.
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        } else {
            this.weight = weight;
            return weight;
        }
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return "till " + destination + " med " + name + " tar " + weight;
        // toString – returnerar en sträng med information om kanten
    }
}
