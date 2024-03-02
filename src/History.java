import java.net.InetAddress;

public record History(String client, InetAddress address, Message message) {
}
