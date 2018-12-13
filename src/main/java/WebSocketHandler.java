import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

public class WebSocketHandler extends WebSocketClient {

    URI serverURI = null;
    DeviceRouter deviceRouter;
    public WebSocketHandler(URI serverURI) {
        super(serverURI);
        this.serverURI = serverURI;
    }

    public WebSocketHandler(URI serverURI, DeviceRouter deviceRouter) {
        super(serverURI);
        this.serverURI = serverURI;
        this.deviceRouter = deviceRouter;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send(new JSONArray().put(new JSONObject()
                .put("type", 4)
                .put("value", new JSONObject()
                        .put("login", "1")
                        .put("password", "qwerty-qwerty-qwerty"))).toString());
    }

    @Override
    public void onMessage(String message) {
        JSONArray inputArr = new JSONArray(message);
        for(Object o: inputArr) {
            JSONObject inputObj = (JSONObject) o;
            if(inputObj.getInt("type") == 6) {
                JSONArray answer = new JSONArray();
                for(Device device: deviceRouter) {
                    answer.put(device.getState());
                }
                send(answer.toString());
            } else if(inputObj.getInt("type") == 0) {
                Long embeddedId = inputObj.optLong("embeddedDeviceId", -1L);
                Device d = deviceRouter.getDevice(embeddedId);
                d.executeCommand(inputObj);
                send(new JSONArray().put(d.getState()).toString());
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("close");
        new WebSocketHandler(serverURI, deviceRouter).connect();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
