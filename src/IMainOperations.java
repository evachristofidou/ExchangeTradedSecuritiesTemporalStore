
public interface IMainOperations {
	
	String getObservations(int a, int b);
	
	String createEntry(int a, int b, String c);
	
	String updateExistingEntry(int a, int b, String c);
	
	String getLatestObs(int a);
	
	String deleteObsTs(int a, int b);
	
	String deleteObsNoTs(int a);
	
	boolean checkTable();
}
