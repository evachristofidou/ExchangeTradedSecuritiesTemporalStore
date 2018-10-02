import java.util.HashMap;
import java.util.List;

public class ExchangeTradedSecurityLazyThreadSafe {

	public HashMap<Integer, List<Observation>> SecuritiesTable = new HashMap <Integer, List<Observation>>();

	private static ExchangeTradedSecurityLazyThreadSafe instance;

		  private ExchangeTradedSecurityLazyThreadSafe() {
		    // protect against instantiation via reflection
		    if (instance == null) {
		      instance = this;
		    } else {
		      throw new IllegalStateException("Already initialized.");
		    }
		  }

		  /**
		   * The instance gets created only when it is called for first time. Lazy-loading
		   */
		  public static synchronized ExchangeTradedSecurityLazyThreadSafe getInstance() {
		    if (instance == null) {
		      instance = new ExchangeTradedSecurityLazyThreadSafe();
		    }

		    return instance;
		  }
		  public void put(Integer Key, List<Observation> Obs) {
			  SecuritiesTable.put(Key,Obs);       
		    }
		  public List<Observation> get(Integer Id) {
			return  SecuritiesTable.get(Id);
		  }
		  public boolean idExists(int id) {
				return SecuritiesTable.containsKey(id);
				}
		  
		  public void remove(Integer Key) {
			  SecuritiesTable.remove(Key);       
		    }
	
}
