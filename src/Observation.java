
public class Observation {
	
	public int ts;
	public String data;
	
	Observation(int ts, String data){
		this.ts = ts;
		this.data = data;
	}
	
	public int GetTs() {
		return this.ts;
	}
	
	public String GetData() {
		return this.data;
	}
	
	public void SetTs(int t) {
		this.ts = t;
	}
	
	public void SetData(String d) {
		this.data = d;
	}

}

