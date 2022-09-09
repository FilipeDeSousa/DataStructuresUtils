package main;

import java.util.Arrays;

import programs.Program;

public class DataStructureUtils {

	public static void main(String[] args) {
		Program p=Program.build(args[0], Arrays.copyOf(args,1));
		p.run();
	}

}
