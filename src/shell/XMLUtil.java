package shell;


import java.io.FileInputStream;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.apache.xerces.parsers.DOMParser;
import java.util.Hashtable;


public class XMLUtil {
	public static void buildMap(Hashtable<String, String> map, String source) {
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(
					new FileInputStream(source)));
			Document doc = parser.getDocument();
			NodeList commands = doc.getElementsByTagName("command");
			
			for (int i=0;i<commands.getLength();i++) {
				Node node = commands.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String first = null, second = null;
					for (int j=0;j<node.getChildNodes().getLength();j++) {
						if (node.getChildNodes().item(j).getNodeName().equals("name"))
							first = node.getChildNodes().item(j).getTextContent();
						if (node.getChildNodes().item(j).getNodeName().equals("file"))
							second = node.getChildNodes().item(j).getTextContent();
					}
					if (first != null && second != null) {
						map.put(first,second);
					}
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}		
	}
}
