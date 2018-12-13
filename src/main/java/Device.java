import org.json.JSONObject;

public interface Device {
	Long getEmbeddedId();
//	boolean getOnOffState();
	void executeCommand(JSONObject command) throws IllegalArgumentException;
	JSONObject getState();
}
