import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

public class SequencerImpl implements Sequencer {

    private static final int MAX_MSG_LENGTH = 1024;
    static long lastSequenceReceived;
    ArrayList<Message> clientMessages;

    public SequencerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.clientMessages = new ArrayList<>();
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
        clientMessages.add(new Message(msg, lastSequenceReceived));
    }

    @Override
    public void leave(String sender) throws RemoteException {
        // tell sequencer that "sender" will no longer need its services
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException {
        // getMissing -- ask sequencer for the message whose sequence number is "sequence"
        for (var message : clientMessages) {
            if (message.sequence() == sequence) {
                var msg = message.msg();
//                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(msg.length);
//                DataOutputStream dataOutputStream = new DataOutputStream(byteStream);
//                dataOutputStream.writeLong(); // marshals a Long into the byte array underlying bstream
                System.out.println(new String(msg));
// …
                return message.msg();
            }
        }
        System.out.println("Message with sequence " + sequence + " not found!");
        return null;
    }

    @Override
    public void heartbeat(String sender, long lastSequenceReceived) throws RemoteException {
        // heartbeat -- we have received messages up to number "lastSequenceReceived"
        System.out.println("Heartbeat from " + sender + " with lastSequenceReceived: " + lastSequenceReceived);
    }

    public static record Message(byte[] msg, long sequence ) {
    }
}
