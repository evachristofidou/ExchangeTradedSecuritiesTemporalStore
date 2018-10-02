import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CommandsExecutionTest {
	
	MainOperations operations = new MainOperations(new DataRepository());
	
	@Test
	public void test() {
		
		Assert.assertEquals("OK: New identifier 1 has been successfully created with an observation 2.", operations.createEntry(1, 1, "2"));
		Assert.assertEquals("OK: Latest observation for id 1 with timestamp 1 is: 2.",operations.getLatestObs(1));
		Assert.assertEquals("OK: Observation as-of timestamp 1 is 2.",operations.updateExistingEntry(1, 1, "1"));
		Assert.assertEquals("OK: Observation as-of timestamp 2 is 1.",operations.updateExistingEntry(1, 2, "2"));
		Assert.assertEquals("OK: Observation as-of timestamp 3 is 2.",operations.updateExistingEntry(1, 3, "3"));
		Assert.assertEquals("ERR: Identifier 2 does not exist.",operations.getLatestObs(2));
		Assert.assertEquals("OK: Latest observation for id 1 with timestamp 3 is: 3.",operations.getLatestObs(1));
		Assert.assertEquals("OK: Data of as-of the given timestamp, which is 1, is 1.", operations.deleteObsTs(1, 2));
		Assert.assertEquals("OK: Latest observation for id 1 with timestamp 1 is: 1.", operations.getLatestObs(1));
		Assert.assertEquals("OK: Observation as-of timestamp 5 is 1.", operations.updateExistingEntry(1, 5, "hello"));
		Assert.assertEquals("OK: Observations for id 1 for timestamp 5 is hello", operations.getObservations(1, 5));
		
	}

}
