package client;

import server.SequencerImpl;
import shared.Sequencer;
import shared.SequencerException;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Group implements Runnable {

    private final Sequencer sequencer;

    public interface MsgHandler {
        void handle(int count, byte[] msg);
    }

    public Group(String host, MsgHandler handler, String senderName) throws GroupException {
        // contact Sequencer on "host" to join group,
        try {
            sequencer = new SequencerImpl();
            sequencer.join(senderName);
        } catch (RemoteException | SequencerException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // create MulticastSocket and thread to listen on it,
        // perform other initialisations
    }

    public void send(byte[] msg) throws GroupException {
        // send the given message to all instances of Group using the same sequencer
        System.out.println("Sending message: " + new String(msg));
        try {
            sequencer.send("client", msg, 1L, SequencerImpl.lastSequenceReceived);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void leave() {
        // leave group
    }

    public void run() {
        // repeatedly: listen to MulticastSocket created in constructor, and on receipt
        // of a datagram call "handle" on the instance
        // of client.Group.MsgHandler which was supplied to the constructor
    }

    public static class GroupException extends Exception {
        public GroupException(String s) {
            super(s);
        }
    }

    public class HeartBeater extends Thread {
        // This thread sends heartbeat messages when required
    }
}
