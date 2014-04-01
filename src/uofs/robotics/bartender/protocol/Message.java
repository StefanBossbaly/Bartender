package uofs.robotics.bartender.protocol;

public class Message {

	private int content[];

	public Message() {
		this.content = new int[Protocol.MSG_SIZE];

		content[Protocol.I_START] = Protocol.MSG_START;
		content[Protocol.I_END] = Protocol.MSG_END;
	}

	public Message(int content[]) {

		if (content.length != Protocol.MSG_SIZE)
			throw new MalformedMessageException("Content of a message must be 32 in size");

		if (content[Protocol.I_START] != Protocol.MSG_START && content[Protocol.I_END] != Protocol.MSG_END)
			throw new MalformedMessageException("Message does not contain either the start or stop byte");

		this.content = content;
	}

	public static Message buildCommand(int command) {
		Message packet = new Message();

		packet.setType(Protocol.TYPE_CMD);
		packet.setCommand(command);
		packet.setResponseCode(Protocol.BLANK);

		return packet;
	}

	public static Message buildResponse(int command, int responseCode) {
		Message packet = new Message();

		packet.setType(Protocol.TYPE_RSP);
		packet.setCommand(command);
		packet.setResponseCode(responseCode);

		return packet;
	}

	public int getType() {
		return content[Protocol.I_TYPE];
	}

	public void setType(int type) {
		content[Protocol.I_TYPE] = type;
	}

	public boolean isCommand() {
		return content[Protocol.I_TYPE] == Protocol.TYPE_CMD;
	}

	public boolean isResponse() {
		return content[Protocol.I_TYPE] == Protocol.TYPE_RSP;
	}

	public int getCommand() {
		return content[Protocol.I_CMD];
	}

	public void setCommand(int command) {
		content[Protocol.I_CMD] = command;
	}

	public int getResponseCode() {
		return content[Protocol.I_RSP_CODE];
	}

	public void setResponseCode(int code) {
		content[Protocol.I_RSP_CODE] = code;
	}

	public byte[] getContent() {
		byte[] buffer = new byte[Protocol.MSG_SIZE];

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = (byte) (content[i] & 0xFF);

		return buffer;
	}
}
