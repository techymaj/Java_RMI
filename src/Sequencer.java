import java.net.UnknownHostException;
import java.rmi.*;

// this interface represents all the methods that the client can call on the sequencer
public interface Sequencer extends Remote {
    // join -- request for "sender" to join sequencer's multicasting service;
    // returns an object specifying the multicast address and the first sequence number to expect
    SequencerJoinInfo join(String sender)
            throws RemoteException, SequencerException, UnknownHostException;

    // send -- "sender" supplies the msg to be sent, its identifier,
    // and the sequence number of the last received message
    void send(String sender, byte[] msg, long msgID, long lastSequenceReceived)
            throws RemoteException;

    // leave -- tell sequencer that "sender" will no longer need its services
    void leave(String sender)
            throws RemoteException;

    // getMissing -- ask sequencer for the message whose sequence number is "sequence"
    byte[] getMissing(String sender, long sequence)
            throws RemoteException, SequencerException;

    // heartbeat -- we have received messages up to number "lastSequenceReceived"
    void heartbeat(String sender, long lastSequenceReceived)
            throws RemoteException;
}
