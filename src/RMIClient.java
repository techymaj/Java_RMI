import java.rmi.registry.LocateRegistry;

public class RMIClient {

    private Sequencer sequencer;

    public RMIClient() {
    }

    public void start() {
        try {
            // start the client
            // connect to the serer
            var registry = LocateRegistry.getRegistry("localhost", 1099);
            // get the sequencer from the server
            sequencer = (Sequencer) registry.lookup("sequencer");
            // call the join method on the sequencer
            var joinInfo = sequencer.join("client");
            System.out.println("Joined: " + joinInfo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}