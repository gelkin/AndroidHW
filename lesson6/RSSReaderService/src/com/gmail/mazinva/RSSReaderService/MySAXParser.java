package com.gmail.mazinva.RSSReaderService;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MySAXParser {

    private InputSource inputSource;

    MySAXParser(InputStream inputStream, String encoding) throws IOException {
        Reader reader;
        if (encoding != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        } else {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        inputSource = new InputSource(reader);
    }

    public List<RSSItem> parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MyHandler handler = new MyHandler();
        saxParser.parse(inputSource, handler);
        List<RSSItem> list = handler.getItems();
        return list;
    }

    private class MyHandler extends DefaultHandler {
        List<RSSItem> items;
        RSSItem item;
        boolean checker;
        StringBuilder stringBuilder;

        public MyHandler() {
            items = new ArrayList<RSSItem>();
            stringBuilder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            name = name.toLowerCase();
            super.startElement(uri, localName, name, attributes);
            if (name.equals("item") || name.equals("entry")) {
                item = new RSSItem();
                return;
            }

            if (item != null) {
                if (name.equals("title")) {
                    item.key = 1;
                    checker = true;
                }

                if (name.equals("description") || name.equals("content") || name.equals("summary")) {
                    item.key = 2;
                    checker = true;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (checker) {
                stringBuilder.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            super.endElement(uri, localName, name);
            name = name.toLowerCase();
            if (checker) {
                String s = stringBuilder.toString();
                s = s.trim();
                checker = false;
                item.set(s);
                stringBuilder.setLength(0);
            }
            if (name.equals("entry") || name.equals("item")) {
                items.add(item);
                item = null;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if (items.size() == 0) {
                throw new SAXException();
            }
        }

        private List<RSSItem> getItems() {
            return items;
        }
    }

}
