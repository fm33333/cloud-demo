package com.demo.feign.utils;

import com.demo.feign.data.official.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {

    /**
     * 将xml转换成map
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> parseXML(String xml) throws DocumentException {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xml.getBytes(Charset.forName("utf-8"))));
        Element root = document.getRootElement();
        List<Element> list = root.elements();
        for (Element element : list) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }


    /**
     * 组装XML
     *
     * @param textMessage
     * @return
     */
    public static String getXmlString(TextMessage textMessage) {
        String xml = "";
        if (textMessage != null) {
            xml = "<xml>";
            xml += "<ToUserName><![CDATA[";
            xml += textMessage.getToUserName();
            xml += "]]></ToUserName>";
            xml += "<FromUserName><![CDATA[";
            xml += textMessage.getFromUserName();
            xml += "]]></FromUserName>";
            xml += "<CreateTime>";
            xml += textMessage.getCreateTime();
            xml += "</CreateTime>";
            xml += "<MsgType><![CDATA[";
            xml += textMessage.getMsgType();
            xml += "]]></MsgType>";
            xml += "<Content><![CDATA[";
            xml += textMessage.getContent();
            xml += "]]></Content>";
            xml += "</xml>";
        }
        return xml;
    }


}
