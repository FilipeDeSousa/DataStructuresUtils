package programs.xsd2table;

import java.util.Iterator;
import java.util.Map;

import groovy.xml.slurpersupport.Node;

class Row {
	String name;
	String parent = "";
	int level;
	String occurance;
	String type;
	String minLength = "";
	String maxLength = "";
	String description;
	
	@SuppressWarnings("unchecked")
	public void addXSDNode(Node node, int level) throws Exception {
		this.level = level;
		Map<String, String> attributes = node.attributes();
		name = attributes.get("name");
		Node grandParent = node.parent().parent();
		if(grandParent!=null)
			parent = (String) grandParent.parent().attributes().get("name");
		occurance = getOccurence(attributes);
		Iterator<Node> it = node.childNodes();
		while(it.hasNext()) {
			Node child = it.next();
			String childName = child.name();
			if(childName.equals("annotation")) {
				Node grandChild = (Node) child.childNodes().next();
				String grandChildName = grandChild.name();
				if(!grandChildName.equals("documentation"))
					throw new Exception("Invalid XSD - node name != documentation - name = "+grandChildName);
				description = grandChild.text();
			}
			if(childName.equals("complexType")) {
				type = "Complex";
			}
			if(child.name().equals("simpleType")) {
				Node grandChild = (Node) child.childNodes().next();
				String grandChildName = grandChild.name();
				if(!grandChildName.equals("restriction"))
					throw new Exception("Invalid XSD - node name != restriction - name = "+grandChildName);
				Map<String, String> grandChildAttributes = grandChild.attributes();
				type = grandChildAttributes.get("base");
				type = type.substring(4,5).toUpperCase()+type.substring(5);
				Iterator<Node> greatChildren = grandChild.childNodes();
				while(greatChildren.hasNext()) {
					Node greatChild = greatChildren.next();
					String greatChildName = greatChild.name();
					if(greatChildName.equals("minLength"))
						minLength = (String) greatChild.attributes().get("value");
					if(greatChildName.equals("maxLength"))
						maxLength = (String) greatChild.attributes().get("value");
				}
			}
		}
		
	}
	
	private String getOccurence(Map<String, String> attributes) {
		String min = attributes.get("minOccurs"), max = attributes.get("maxOccurs");
		if(min == null)
			min = "1";
		if(max == null)
			max = "1";
		if(min.equals(max))
			return min;
		return min+".."+max;
	}

	public String toString() {
		return name+";"+parent+";"+level+";"+occurance+";"+type+";"+minLength+";"+maxLength+";"+description;
	}

}
