package programs.xsd2table;

import java.util.ArrayList;

class Table {
	//Atributes
	ArrayList<Row> rows = new ArrayList<Row>();
	
	void add(Row row) {
		rows.add(row);
	}
	
	public String toString() {
		String s = "Name;Parent;Level;Occurance;Type;Min;Max;Description";
		for(Row r: rows)
			s += '\n'+r.toString();
		return s;
	}
}
