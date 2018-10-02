
public class MainOperations implements IMainOperations {
	
	IDataRepository datarepo;
	
	public MainOperations(IDataRepository _datarepo){
		this.datarepo = _datarepo;
	}
	
	
	@Override
	public String getObservations(int id, int ts) {
		if (datarepo.idExists(id)) {
			String result = datarepo.getDataFromObs(id, ts);
			return result;
		}
		return "ERR: Identifier " + id +  " does not exist.";
	}

	@Override
	public String createEntry(int id, int ts, String d) {
		
		if (datarepo.idExists(id)) {
			return "ERR: A history already exists for identifier " + id ;
		}
		if (datarepo.isIDinBounds(id)) {
			String result = datarepo.createNew(id, ts, d);
			return result;
		}
		return "ERR: Timestamp " + ts +  " is out of bounds.";
	}

	@Override
	public String updateExistingEntry(int id, int ts, String d) {

		if (datarepo.idExists(id)) {
			String result = datarepo.update(id, ts, d);
			return result;
		}		
		return "ERR: Identifier " + id +  " does not exist.";
	}
	

	@Override
	public String getLatestObs(int id) {
		if (datarepo.idExists(id)) {
			String result =datarepo.obsWithLatestTS(id);
			return result;
		}
		return "ERR: Identifier " + id +  " does not exist.";
	}
	

	@Override
	public String deleteObsTs(int id, int ts) {
		
		if (datarepo.idExists(id)) {
			String result = datarepo.deleteObservationsTS(id, ts);
			return result;
		}
		return "ERR: Identifier " + id +  " does not exist.";
	}
	
	@Override
	public String deleteObsNoTs(int id) {
		
		if (datarepo.idExists(id)) {
			String result = datarepo.deleteObservationsNOts(id);
			return result;
		}
		return "ERR: Identifier " + id +  " does not exist";
	}
	
	@Override
	public boolean checkTable() {
		return datarepo.isEmptyTable();
	}
}
