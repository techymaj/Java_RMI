package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Broadcast extends Remote {
    void update(byte[] result) throws RemoteException;
}
