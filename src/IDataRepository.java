
public interface IDataRepository {
   String createNew(int id, int ts, String data);
   String setObsForExistingID(int id, int ts, String d);
   String update(int id, int ts, String data);
   String deleteObservationsNOts(int id);
   String deleteObservationsTS(int id, int timestamp);
   String getDataFromObs(int id, int timestamp);
   String obsWithLatestTS(int id);
   boolean isIDinBounds(int id);
   boolean idExists(int id);
   boolean isEmptyTable();
}
