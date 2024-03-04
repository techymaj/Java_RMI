package server;

import client.History;
import client.Message;
import shared.Sequencer;
import shared.SequencerJoinInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SequencerImpl implements Sequencer {

    public static long lastSequenceReceived;
    ArrayList<Message> clientMessages;
    ArrayList<History> histories;
    private InetAddress address;
    static final int MAX_ENTRIES = 1024;

    public SequencerImpl() throws RemoteException {
        // make object remotely available
        UnicastRemoteObject.exportObject(this, 0);
        this.clientMessages = new ArrayList<>();
        this.histories = new ArrayList<>();
    }

    @Override
    public SequencerJoinInfo join(String sender) throws RemoteException, UnknownHostException {
        // request for "sender" to join sequencer's multicasting service;
        // returns an object specifying the multicast address and the first sequence number to expect
        address = InetAddress.getLocalHost();
        long sequence = 0L;
        return new SequencerJoinInfo(address, sequence);
    }

    @Override
    public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived) throws RemoteException {
        // "sender" supplies the msg to be sent, its identifier,
        // and the sequence number of the last received message
        SequencerImpl.lastSequenceReceived = lastSequenceReceived;
        Message message = new Message(msg, lastSequenceReceived);
        clientMessages.add(message);
        History history = new History(sender, address, message);
        histories.add(history);
        messageHistoryManager();
    }

    @Override
    public void leave(String sender) throws RemoteException {
        // tell sequencer that "sender" will no longer need its services
        UnicastRemoteObject.unexportObject(this, false);
    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException {
        // getMissing -- ask sequencer for the message whose sequence number is "sequence"
        for (var message : clientMessages) {
            if (message.sequence() == sequence) {
                var msg = message.msg();
                System.out.println("Result > " + new String(msg));
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

    private void messageHistoryManager() {
        if (lastSequenceReceived == MAX_ENTRIES) {
            clientMessages.removeIf(message -> message.sequence() < MAX_ENTRIES);
        }
    }
}
