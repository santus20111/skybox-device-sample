import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceRouter implements Iterable<Device> {
    private Map<Long, Device> map = new ConcurrentHashMap<>();
    private static DeviceRouter deviceRouter = new DeviceRouter();

    private DeviceRouter() {
    }

    public static DeviceRouter getInstance() {
        return deviceRouter;
    }

    public void addDevice(Device device) {
        map.put(device.getEmbeddedId(), device);
    }

    public Device getDevice(Long embeddedId) {
        return map.get(embeddedId);
    }

    public int getSize() {
        return map.size();
    }

    @Override
    public Iterator<Device> iterator() {
        return map.values().iterator();
    }
}
