import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunSequencer {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        // the stub is the sequencer
        Sequencer sequencer = new SequencerImpl(); // if the server can't create the sequencer, it will throw an exception
        Registry registry = LocateRegistry.createRegistry(1099); // like a phone book. and we want to put the sequencer in the phone book
        registry.bind("sequencer", sequencer); // put the sequencer in the phone book under the name "sequencer"
        System.out.println("Sequencer started...");

    }
}
