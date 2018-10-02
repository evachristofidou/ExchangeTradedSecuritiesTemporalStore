import java.util.Scanner;

public class App {

	 
	public static void main(String[] args) {
		
		boolean quit = false;
		Scanner sc = new Scanner(System.in);
		String cmd = "NoCommand";
		String[] cmds = null;
		MainOperations operations = new MainOperations(new DataRepository());
		
		
		while (!quit) {
			
			if (sc.hasNext()) {
				 // Command Name
				cmd = sc.nextLine();
				// splits all the commands if there is more than one
				cmds = cmd.split("\\s+"); 
										 

				switch (cmds[0].toUpperCase()) {
				case "GET":
					System.out.println("Getting...");
					System.out.println(operations.getObservations(
							Integer.parseInt(cmds[1]),
							Integer.parseInt(cmds[2]))
							+ "\r");
					
					break;

				case "CREATE":
					System.out.println("Creating...");
					System.out.println(operations.createEntry(
							Integer.parseInt(cmds[1]),
							Integer.parseInt(cmds[2]), cmds[3])
							+ "\r");
					break;

				case "UPDATE":
					System.out.println("Updating...");
					System.out.println(operations.updateExistingEntry(
							Integer.parseInt(cmds[1]),
							Integer.parseInt(cmds[2]), cmds[3])
							+ "\r");
					break;

				case "LATEST":
					System.out.println("Getting latest...");
					System.out.println(operations.getLatestObs(Integer
							.parseInt(cmds[1])) + "\r");
					break;

				case "DELETE":
					if (cmds.length == 3) {
						System.out.println("Deleting...");
						System.out.println(operations.deleteObsTs(
								Integer.parseInt(cmds[1]),
								Integer.parseInt(cmds[2]))
								+ "\r");
						
						if (operations.checkTable()) {
							System.out.println("QUIT");
							quit = true;
						}
						
						break;
					}else if (cmds.length == 2) {
						System.out.println("Deleting...");
						System.out.println(operations.deleteObsNoTs(
								Integer.parseInt(cmds[1]))
								+ "\r");
						
						if (operations.checkTable()) {
							System.out.println("QUIT");
							quit = true;
						}
						
						break;
					}						

				case "QUIT":
					quit = true;
					break;

				default:
					System.out.println("The command you requested is not valid, please try again");
					break;
				}
			}
		}

		sc.close();
	}
}
