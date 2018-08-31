package com.peramdy.config.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author peramdy on 2018/8/17.
 */
public class XmlUtil {

    public static Element root(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Element element = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            element = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return element;
    }

}
