package io.lippia.api.extractor.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.beust.jcommander.internal.Lists;

public class XmlArrayValueExtractor {

	public static List<Object> handle(String xmlString, String xmlPath)throws ParserConfigurationException, SAXException, IOException {

		String[] xmlPaths = xmlPath.split("\\.");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(xmlString));
		Document doc = dBuilder.parse(is);

		int index = -1;
		Node xmlObject = doc.getElementsByTagName(xmlPaths[0]).item(0);
		for (int i = 1; i < xmlPaths.length; i++) {

			Element xmlElement = (Element)xmlObject;
			String[] once = xmlPaths[i].split("\\[");

			int value = 0;

			if(index != -1) {
				value = index;
				index = -1;
			}

			if(once.length > 1) {
				NodeList nl = xmlElement.getElementsByTagName(once[0]); 
				xmlObject = nl.item(value);
				index = Integer.parseInt(once[1].replace("]",""));

				if(i+1 == xmlPaths.length) {
					return Lists.newArrayList();
				}
				continue;
			}		

			if(i+1 == xmlPaths.length) {
				// Dejo esto para no perder el hilo, es el objeto que se usara para el return
//				xmlObject = xmlElement.getElementsByTagName(once[0]).item(value);
				return Lists.newArrayList();
			}

			xmlObject = xmlElement.getElementsByTagName(once[0]).item(value);
		}

		return Lists.newArrayList();
	}

}
