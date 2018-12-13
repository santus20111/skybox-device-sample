import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLampDevice implements Device {
	private Logger logger = LoggerFactory.getLogger(SimpleLampDevice.class);
	private Long embeddedId; // -1L if main device
	private LampState state;
	private int pin;
	//private Pi4jCommandInvoker invoker;

	public static class LampState {
		public Boolean switched = false;
	}

	public SimpleLampDevice(/*Pi4jCommandInvoker invoker, */Long embeddedId, LampState startState, int pin) {
		this.embeddedId = embeddedId;
		this.state = startState;
		//this.invoker = invoker;
		this.pin = pin;
/*		if(state != null){ todo
			invoker.setDigitalGPIOLevel(pin, true);
		}*/
	}

	@Override
	public Long getEmbeddedId() {
		return embeddedId;
	}

	synchronized public void executeCommand(JSONObject command) throws IllegalArgumentException {
		try {
			int type = command.getInt("type");
			if (type == 0) {
				boolean value = command.getBoolean("value");
				state.switched = value;
				//invoker.setDigitalGPIOLevel(pin, value); todo
				logger.info(embeddedId + ": turned light " + (value ? "on" : "off"));
			}
		} catch (JSONException j){
			logger.error(embeddedId + ": json exception: " + command);
		}
	}

	@Override
	public JSONObject getState() {
		JSONObject jsonObject = new JSONObject()
				.put("type", 0)
				.put("value", state.switched);
		if(embeddedId != -1L){
			jsonObject.put("embeddedDeviceId", embeddedId);
		}
		return jsonObject;
	}
}
