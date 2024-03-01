import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SequencerImpl implements Sequencer {

    public SequencerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public SequencerJoinInfo join(String sender) throws RemoteException, SequencerException {
        return null;
    }

    @Override
    public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived) throws RemoteException {

    }

    @Override
    public void leave(String sender) throws RemoteException {

    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException, SequencerException {
        return new byte[0];
    }

    @Override
    public void heartbeat(String sender, long lastSequenceReceived) throws RemoteException {

    }
}
