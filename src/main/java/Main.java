import execute.NewMain;

/**
 * Created by Szymon on 2015-05-23.
 */
public class Main {
	public static void main(String args[]) {
		System.out.println("Program started");

		NewMain nm = new NewMain();
		System.out.println("Program executed successfully: " + nm.start());
	}
}
