package programs.xsd2table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import groovy.xml.XmlSlurper;
import groovy.xml.slurpersupport.GPathResult;
import groovy.xml.slurpersupport.Node;
import programs.Program;

public class XSD2Table extends Program{
	//Attribute
	private String file;
	
	public XSD2Table(String[] args) {
		super(args);
		file = args[0];
	}

	@Override
	public void run() {
		try {
			XmlSlurper sluper = new XmlSlurper();
			GPathResult doc = sluper.parse(new File(file));
			Table table = new Table();
			
			//read xsd
			if(!doc.name().equals("schema"))
				throw new Exception("Invalid XSD - Root node != schema");
			Node root = ((Node)doc.childNodes().next()).parent();
			readElements(table, root, 0);
			
			//test - print table
			System.out.println(table);
			
			FileWriter writer = new FileWriter(file+"-out.csv");
			writer.write(table.toString());
			writer.close();
			
			//System.exit(0);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readElements(Table table, Node parent, int level) throws Exception {
		Iterator<Node> it = parent.childNodes();
		while(it.hasNext()) {
			Node node = it.next();
			String name = node.name();
			if(name.equals("element")) {
				Row row=new Row();
				row.addXSDNode(node, level);
				table.add(row);
				Iterator<Node> children = node.childNodes();
				while(children.hasNext()) {
					Node child = children.next();
					if(child.name().equals("complexType")) {
						Node grandChild = (Node)child.childNodes().next();
						String grandChildName = grandChild.name();
						if(!grandChildName.equals("sequence"))
							throw new Exception("Invalid XSD - node name != sequence - name = "+grandChildName);
						readElements(table, grandChild, level+1);
					}
				}
			}
		}
	}

	//Test
	public static void main(String[] args) {
		(new XSD2Table(args)).run();
	}

}
