import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


public class DataRepository implements IDataRepository {
	
	private ExchangeTradedSecurityLazyThreadSafe exTrSecurity = ExchangeTradedSecurityLazyThreadSafe.getInstance();

	DataRepository(){
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean idExists(int id) {
		return exTrSecurity.idExists(id);
	}
	@Override
	public boolean isEmptyTable() {
		return exTrSecurity.SecuritiesTable.isEmpty();
	}
	
	
			@Override
			public boolean isIDinBounds(int id) {
		return id <= Math.pow(2,31) - 1 && id >= 0;
	}
	
	public boolean timestampInBounds(int ts) {
		return ts <= Math.pow(2,63) - 1 && ts >= 0;		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String createNew(int id, int ts, String data)
	{
		//List<Observation> obslist = this.exTrSecurity.get(id);
						
		Observation ob = new Observation(ts, data);
		ob.SetTs(ts);
		ob.SetData(data);
		
		List<Observation> newlistObs = new ArrayList<Observation>();
		newlistObs.add(ob);
		exTrSecurity.put(id, newlistObs);
		
		return "OK: New identifier " + id + " has been successfully created with an observation " + data + ".";
		
	}
	
	@Override
	public String setObsForExistingID(int id, int ts, String d) {
		if (timestampInBounds(ts)) {
			for ( Entry<Integer, List<Observation>> entry : exTrSecurity.SecuritiesTable.entrySet()) {
			    Integer key_id = entry.getKey();
			    List<Observation> listobs = entry.getValue();
			    if (key_id == id) {
			    	Observation ob = new Observation(ts, d);
					ob.SetTs(ts);
					ob.SetData(d);
			    	ArrayList<Integer> sortedts = sortedTs(id);
			    	
			    	if (sortedts.contains(ts)) {
			    		String valuse = null;
			    		Iterator<Observation> iterator = exTrSecurity.get(id).iterator();
				      	while(iterator.hasNext()){
				      		Observation ref = iterator.next();
				      		if (ref.GetTs() == ts) {  			
				      			valuse = ref.GetData();
				      			iterator.remove();					            
				      		}
				      	}
					    listobs.add(ob);
					    return "OK: Observation as-of timestamp " + ts + " is " + valuse + ".";
					    
			    	}
			    	
			    	else {
			    		if (listobs.size() < 1000) {
			    			//get asof ts
					    	int asOf = asOfTs(sortedts, id, ts);
					    	for (Observation o : this.exTrSecurity.get(id)) {
								if (o.GetTs() == asOf) {
									listobs.add(ob);
									return "OK: Observation as-of timestamp " + ts + " is " + o.GetData() + ".";
								}
							}
			    		}
			    		else {
			    			return "ERR: No more observation entries for id " + id + " are permitted. Max limit of 1000 reached.";
			    		}
			    		
			    	}
			    }
			}
		}
		return null;
	}
		
	
	@Override
	public String update(int id, int ts, String data) {
		if (timestampInBounds(ts) && exTrSecurity.idExists(id)){
			return setObsForExistingID(id, ts, data);
			
		}
		
		return "ERR: Timestamp " + ts + "  is out of bounds";
	}
	
	
	@Override
	public String deleteObservationsNOts(int id) {
		String d = "";
		if (exTrSecurity.idExists(id)) {
			//sort ts
			ArrayList<Integer> sortedTs = sortedTs(id);
			//get data from max ts
			int max = sortedTs.get(sortedTs.size()-1);
			for (Observation o : exTrSecurity.get(id)) {
    			if (o.GetTs() == max) {
    				d = o.GetData();
    			}
    		}		
			
			
			Iterator<Entry<Integer, List<Observation>>> it = exTrSecurity.SecuritiesTable.entrySet().iterator();
		    while (it.hasNext())
		    {
		       Entry<Integer, List<Observation>> item = it.next();
		       if (item.getKey() == id) {
		    	   exTrSecurity.remove(item.getKey());
		    	   
		    	   return "OK: Entry of id " + id + " has been deleted. Obervation from the greatest timestamp from the history of id " + id + " is " + d;
		       }
		    }
			
		}
		return null;
		
	}

	
	@Override
	public String deleteObservationsTS(int id, int timestamp) {
		for (Entry<Integer, List<Observation>> entry : exTrSecurity.SecuritiesTable.entrySet()) {
		    Integer key_id = entry.getKey();
		    if (key_id == id) {
		    	
				
				ArrayList<Integer> sortedts = sortedTs(id);
				int size = sortedts.size();		    	
				int ind = sortedts.indexOf(timestamp);
				
				if (sortedts.get(0) == timestamp) {
					Iterator<Entry<Integer, List<Observation>>> it = exTrSecurity.SecuritiesTable.entrySet().iterator();
				    while (it.hasNext())
				    {
				       Entry<Integer, List<Observation>> item = it.next();
				       if (item.getKey() == id) {
				    	   exTrSecurity.remove(item.getKey());
				    	   
				    	   return "ERR: No prior available observation. ";
				       }
				    }
				}
				
				if (sortedts.contains(timestamp)) {
					for (int i = ind; i < size; i++) {
						Iterator<Observation> iterator = exTrSecurity.get(id).iterator();
				      	while(iterator.hasNext()){
				      		if (iterator.next().GetTs() == sortedts.get(i)) {
				      			
				      			iterator.remove();					            
				      		}
				      	}					      					
					}
					ArrayList<Integer> sortedts2 = sortedTs(id);
					for (Observation o : exTrSecurity.get(id)) {
		    			if (o.GetTs() == sortedts2.get(sortedts2.size()-1)) {
		    				return "OK: Data of as-of the given timestamp, which is " + o.GetTs() + ", is " + o.GetData() + ".";
		    			}
		    		}
					
				}
				
				else {
										
					for (int i = 0; i < sortedts.size(); i++) {
						
						
						if (sortedts.size() == 1 && sortedts.get(i) < timestamp ) {
							Iterator<Observation> iterator = exTrSecurity.get(id).iterator();
					      	while(iterator.hasNext()){
					      		Observation ref = iterator.next();
					      		if (ref.GetTs() == sortedts.get(i)) {
					      			return "ERR:  Only timestamp available is less than " + timestamp + " and has the data " + ref.GetData() + ". No observations have been deleted.";				            
					      		}
					      	}	
						}
						if(sortedts.get(0) > timestamp ) {
							Iterator<Entry<Integer, List<Observation>>> it = exTrSecurity.SecuritiesTable.entrySet().iterator();
						    while (it.hasNext())
						    {
						       Entry<Integer, List<Observation>> item = it.next();
						       if (item.getKey() == id) {
						    	   exTrSecurity.remove(item.getKey());
						    	   if (exTrSecurity.SecuritiesTable.isEmpty()){
						    		   return "OK: Entry of id " + id + " has been deleted due to current ts smaller than the smallest available timestamp. exTrSecurity empty since this was the last entry available.";
						    	   }
						    	   return "OK: Entry of id " + id + " has been deleted due to current ts smaller than the smallest available timestamp. ";
						       }
						    }
						}
				    	if (sortedts.size() >= 1 && sortedts.get(i) > timestamp ) {
				    		Iterator<Observation> iterator = exTrSecurity.get(id).iterator();
					      	while(iterator.hasNext()){
					      		Observation ref = iterator.next();
					      		if (ref.GetTs() == sortedts.get(i)) {
					      			iterator.remove();
					      			
					      		}
					      	}
					      	ArrayList<Integer> sortedts2 = sortedTs(id);
							for (Observation o : exTrSecurity.get(id)) {
				    			if (o.GetTs() == sortedts2.get(sortedts2.size()-1)) {
				    				return "OK:  Data of as-of the given timestamp, which is " + o.GetTs() + " is " + o.GetData();
				    			}
				    		}
					      	
				    	}
						
				    	
					}
					
				}								
				
				
		    }
		}
		return null;
			
	}
	
	
	@Override
	public String getDataFromObs(int id, int timestamp) {
		if (exTrSecurity.idExists(id)) {
			if (exTrSecurity.get(id).size() == 0) {
				return  "ERR: No available observations for id " + id + " found.";
			}
			else {
				ArrayList<Integer> sortedts = sortedTs(id);
				if (sortedts.contains(timestamp)) {
						for (Observation ob : this.exTrSecurity.get(id)) {
							if (ob.GetTs() == timestamp) {
								return "OK: Observations for id " + id + " for timestamp " + timestamp + " is " + ob.GetData() ;
							}
						}
					
				}
				else if (!sortedts.contains(timestamp)) {
					if (sortedts.get(0) > timestamp)
					{
						for (Observation ob : this.exTrSecurity.get(id)) {
							if (ob.GetTs() == sortedts.get(0))	{
								return "ERR: No observations as-of the given timestamp for id " + id + ". Smallest timestamp " + sortedts.get(0) + " is larger than the input timestamp " + timestamp ;
					    	}
					    }
					 }
					 if (sortedts.get(sortedts.size()-1) < timestamp)
					 {
						 for (Observation ob : this.exTrSecurity.get(id)) {
					    		if (ob.GetTs() == sortedts.get(sortedts.size()-1))	{
					    			return "OK:  Observations for id " + id + " as-of the given timestamp " + timestamp + " is " + ob.GetData() ;
					    		}
					    	}
					 }
					 
					 
					 for (int i = 0; i < sortedts.size(); i++) {
					    	if (sortedts.size() >= 2 && sortedts.get(i) < timestamp && timestamp < sortedts.get(i+1) ) {
					    		for (Observation ob : this.exTrSecurity.get(id)) {
					    			if (ob.GetTs() == sortedts.get(i)) {
					    				return "OK: Observations for id " + id + " for timestamp " + timestamp + " is " + ob.GetData() ;
					    			}
					    		}
					    	}
					    	if (sortedts.size() <= 2 && sortedts.get(sortedts.size()-1) < timestamp) {
					    		for (Observation ob : this.exTrSecurity.get(id)) {
					    			if (ob.GetTs() == sortedts.get(sortedts.size()-1)) {
					    				return "OK: Observations for id " + id + " for timestamp " + timestamp + " is " + ob.GetData() ;
					    			}
					    		}
					    	}
					    		
					 }		
				}
			}
		}	
		return  null;
	}
	
	
	
	//return the observation for the last timestamp
	@Override
	public String obsWithLatestTS(int id) {
		if (exTrSecurity.idExists(id)) {
			ArrayList<Integer> sorted_allts = sortedTs(id);
			int max = sorted_allts.get(sorted_allts.size()-1);
			for (Observation ob : this.exTrSecurity.get(id)) {
				if (ob.GetTs() == max) {
					return "OK: Latest observation for id " + id + " with timestamp " + max + " is: " + ob.GetData() + ".";
				}
			}
		}
		return  "ERR: Identifier " + id +  " does not exist";
	}
	
	
	
	
	

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ArrayList<Integer> sortedTs(int id){
		ArrayList<Integer> allts = new ArrayList<Integer>();
		for (Entry<Integer, List<Observation>> entry : exTrSecurity.SecuritiesTable.entrySet()) {
		    Integer key_id = entry.getKey();
		    if ( key_id == id) {
		    	for (Observation ob : this.exTrSecurity.get(id)) {
					allts.add(ob.GetTs());
				}
		    	Collections.sort(allts);
		    }
		}
		return allts;
	}
	
	private int asOfTs(ArrayList<Integer> allts, int id, int timestamp) {
		Collections.sort(allts);
		if (Collections.max(allts) < timestamp) {
			for (Observation ob : this.exTrSecurity.get(id)) {
				if (ob.GetTs() == Collections.max(allts)) {
					return ob.GetTs();
				}
			}
		}
    	for (int i = 0; i < allts.size(); i++) {
    		if (allts.size() >= 3 && allts.get(i) < timestamp && timestamp < allts.get(i+1) ) {
    			for (Observation ob : this.exTrSecurity.get(id)) {
    				if (ob.GetTs() == allts.get(i)) {
    					return ob.GetTs();
    				}
    			}
    		}
    		if (allts.size() <= 2 && allts.get(allts.size()-1) < timestamp) {
    			for (Observation ob : this.exTrSecurity.get(id)) {
    				if (ob.GetTs() == allts.get(allts.size()-1)) {
    					return ob.GetTs();
    				}
    			}
    		}
    	}
		return 0;
	}
	
	public boolean isEmptyexTrSecurity() {
		return exTrSecurity.SecuritiesTable.isEmpty();
	}
	
}
