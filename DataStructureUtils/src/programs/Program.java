package programs;

import programs.xsd2table.XSD2Table;

public abstract class Program {
	//Constructor
	protected Program(String[] args){
		
	}

	public static Program build(String program, String[] args) {
		Program prog=null;
		switch(program.toUpperCase()) {
		case "XSD2TABLE":
			prog = new XSD2Table(args);
			break;
		}		
		return prog;
	}
	
	public abstract void run();
}
