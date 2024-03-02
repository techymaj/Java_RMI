import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {

    private Sequencer sequencer;
    private Scanner scanner;
    private SequencerImpl si;

    public RMIClient() throws RemoteException {
        si = new SequencerImpl();
    }

    public void start() throws RemoteException, NotBoundException {
        // start the client
        // connect to the serer
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        // get the sequencer from the server
        sequencer = (Sequencer) registry.lookup("sequencer");
        aClientJoins();
        label:
        while (true) {
            scanner = new Scanner(System.in);
            var input = scanner.nextLine();
            switch (input) {
                case "exit" -> {
                    // tell sequencer that "client" will no longer need its services
                    sequencer.leave("client");
                    scanner.close();
                    break label;
                }
                case "heartbeat" -> {
                    si.heartbeat("client", SequencerImpl.lastSequenceReceived);
                }
                default -> {
                    var messageToSend = input.getBytes();
                    long msgID = 1L;
                    long lastSequenceReceived = SequencerImpl.lastSequenceReceived;
                    lastSequenceReceived++;
                    si.send("client", messageToSend, msgID, lastSequenceReceived);
                }
            }
        }
    }

    private void aClientJoins() {
        try {
            // call the join method on the sequencer
            SequencerJoinInfo joinInfo = sequencer.join("client");
            System.out.println("Joined: " + joinInfo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}