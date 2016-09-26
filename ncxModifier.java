/**
 * @author Digvijay Singh Katoch
 * @date 15th Sep 2016
 * @description This class will be used to read a raw ncx file as an input and
 * check whether any navpoints have been removed. If so, then it would regenerate
 * navpoint id and playorder for all the nodes, so as to maintain order.
 */

package com.chd.digvijay;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadNcx {

	//Provide location for the input file

	private static String inputLoc="D:\\Ncxx\\toc_Input.ncx";

	public static void main(String argv[]) {

		try {
			//Checking if a file exists in the given location

			boolean check = new File(inputLoc).exists();

			if(check==false){
				throw new FileNotFoundException();
			}

			// Reading the file

			File fXmlFile = new File(inputLoc);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			//Parsing file information

			Document doc = dBuilder.parse(fXmlFile);

			//Normalizing content so that continuous data is read together even if it goes to new line
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("navPoint");
			
			int nodeCount = nList.getLength();
			Integer nCount = new Integer(nodeCount);

			System.out.println("The number of navpoints are: " + nodeCount);

			System.out.println("----------------------------");
			
			String id;
			String playOrder;

			for (int temp = 0; temp < nodeCount; temp++) {

				Node nNode = nList.item(temp);
				Integer tCount= new Integer(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				
				if (nNode.getNodeType()== Node.ELEMENT_NODE){
					Element eElement = (Element) nNode;
					id = eElement.getAttribute("id");
					playOrder = eElement.getAttribute("playOrder");
					System.out.println("Id is: "+id + " and playOrder is : " + playOrder);
					if(!(playOrder.equals(tCount.toString()))){
						id = "toc" + tCount.toString();
						eElement.setAttribute("id", id);
						eElement.setAttribute("playOrder", tCount.toString());
						System.out.println("playOrder for nav " + nodeCount + " has been changed to " + tCount.toString());
						
					}
					
				}
				
				// write the content back into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(inputLoc));
				transformer.transform(source, result);

				System.out.println("Done");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
