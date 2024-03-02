import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SequencerImpl implements Sequencer {

    static long lastSequenceReceived;

    public SequencerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public SequencerJoinInfo join(String sender) throws RemoteException, UnknownHostException {
        // request for "sender" to join sequencer's multicasting service;
        // returns an object specifying the multicast address and the first sequence number to expect
        InetAddress address = InetAddress.getLocalHost();
        long sequence = 0L;
        return new SequencerJoinInfo(address, sequence);
    }

    @Override
    public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived) throws RemoteException {
        // "sender" supplies the msg to be sent, its identifier,
        // and the sequence number of the last received message
        SequencerImpl.lastSequenceReceived = lastSequenceReceived;
    }

    @Override
    public void leave(String sender) throws RemoteException {
        // tell sequencer that "sender" will no longer need its services
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException, SequencerException {
        return new byte[0];
    }

    @Override
    public void heartbeat(String sender, long lastSequenceReceived) throws RemoteException {
        // heartbeat -- we have received messages up to number "lastSequenceReceived"
        System.out.println("Heartbeat from " + sender + " with lastSequenceReceived: " + lastSequenceReceived);
    }
}
