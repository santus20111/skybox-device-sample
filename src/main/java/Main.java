import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        DeviceRouter deviceRouter = DeviceRouter.getInstance();
        Device led = new SimpleLampDevice(-1L, new SimpleLampDevice.LampState(), 1);
        deviceRouter.addDevice(led);
        WebSocketHandler c = new WebSocketHandler( new URI( "ws://localhost:9000/ws" ), deviceRouter);
        c.connect();
    }
}
