package launch;

import java.io.File;

import global.CmdOption;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import task.AMCModelRunningTask;

/**
 * The main entry of the program.
 */
public class MainEntry {
	public static void main(String[] args) {
		CmdOption cmdOption = new CmdOption();
		CmdLineParser parser = new CmdLineParser(cmdOption);

		try {
			long startTime = System.currentTimeMillis();
			System.out.println("Program Starts.");

			// Parse the arguments.
			parser.parseArgument(args);
			// Check if the input directory is valid.
			if (new File(cmdOption.input100ReviewCorporeaDirectory).listFiles() == null
					|| new File(cmdOption.input1000ReviewCorporeaDirectory)
							.listFiles() == null) {
				System.err
						.println("Input directory is not correct, program exits!");
				return;
			}

			// Run the proposed method on the multiple domains.
			AMCModelRunningTask task = new AMCModelRunningTask(cmdOption);
			task.run();

			System.out.println("Program Ends.");
			long endTime = System.currentTimeMillis();
			showRunningTime(endTime - startTime);
		} catch (CmdLineException cle) {
			System.out.println("Command line error: " + cle.getMessage());
			showCommandLineHelp(parser);
			return;
		} catch (Exception e) {
			System.out.println("Error in program: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private static void showCommandLineHelp(CmdLineParser parser) {
		System.out.println("java [options ...] [arguments...]");
		parser.printUsage(System.out);
	}

	private static void showRunningTime(long time) {
		System.out.println("Elapsed time: "
				+ String.format("%.3f", (time) / 1000.0) + " seconds");
		System.out.println("Elapsed time: "
				+ String.format("%.3f", (time) / 1000.0 / 60.0) + " minutes");
		System.out.println("Elapsed time: "
				+ String.format("%.3f", (time) / 1000.0 / 3600.0) + " hours");
	}
}
