package com.peramdy.config.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @author peramdy on 2018/8/31.
 */
public class XMLParser {

    private static XMLParser sInstance = new XMLParser();

    private XMLParser() {
    }

    public static XMLParser getInstance() {
        return sInstance;
    }

    public Element getRoot(File target) {
        if(target == null) {
            return null;
        } else {
            String fileName = target.getName();
            String[] token = fileName.split("\\.");
            String pf = token[1];
            if(!pf.equals("xml")) {
                return null;
            } else {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(target);
                    Element root = doc.getDocumentElement();
                    return root;
                } catch (Exception var9) {
                    return null;
                }
            }
        }
    }

}
