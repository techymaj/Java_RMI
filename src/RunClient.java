import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {

    public static void main(String[] args) throws NotBoundException, RemoteException {

        RMIClient client = new RMIClient();
        client.start();
    }
}
