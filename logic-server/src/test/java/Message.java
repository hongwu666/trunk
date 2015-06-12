import java.util.Map;


public class Message {
	private String act;
	private int rc;
	private String reqId;
	private long rt;
	private Map<String, Object> dt;
	
//	protected Message data = new Message(1000);

	public Message() {
		
	}
	
	public Message(int rc) {
		this.rc = 1000;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}

	public Map<String, Object> getDt() {
		return dt;
	}

	public void setDt(Map<String, Object> dt) {
		this.dt = dt;
	}
}