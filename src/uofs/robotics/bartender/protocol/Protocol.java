package uofs.robotics.bartender.protocol;

public class Protocol {
	
	// ---------------------------------------------------------------------------------
	// General Section
	// ---------------------------------------------------------------------------------
	public static final int MSG_SIZE = 32;
	public static final byte BLANK = 0x00;
	
	// ---------------------------------------------------------------------------------
	// Start and Stop Bytes
	// ---------------------------------------------------------------------------------
	public static final int I_START = 0x00;
	public static final int I_END = 0x1F;
	
	public static final int MSG_START = -1;
	public static final int MSG_END = -2;

	// ---------------------------------------------------------------------------------
	// Type Section
	// ---------------------------------------------------------------------------------
	
	public static final int I_TYPE = 0x01;
	
	public static final int TYPE_RSP = 0x01;
	public static final int TYPE_CMD = 0x02;

	
	// ---------------------------------------------------------------------------------
	// Command Section
	// ---------------------------------------------------------------------------------
	public static final int I_CMD = 0x02;
	
	public static final int CMD_STOP = 0x01;
	public static final int CMD_MOVE = 0x02;
	
	public static final int PARAM_LOC = 0x04;
	
	public static final int CMD_POUR = 0x03;
	
	public static final int PARAM_SHOTS = 0x04;
	
	public static final int CMD_STATUS = 0x04;
	public static final int CMD_LOCATION = 0x05;
	
	// ---------------------------------------------------------------------------------
	// Response Section
	// ---------------------------------------------------------------------------------
	public static final int I_RSP_CODE = 0x03;
	
	public static final int RSP_OK = 0x01;
	public static final int RSP_ERROR = 0x02;
	public static final int RSP_MAL_MSG = 0x03;
	public static final int RSP_UNK_CMD = 0x04;
}
