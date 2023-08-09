package com.example.telegramm.service;

import com.example.telegramm.model.Valute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

public class CurrencyService {
    public static ArrayList<Valute> getCurrencyRate() throws ParseException, IOException, ParserConfigurationException, SAXException {
        URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), "cp1251"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line);
        }
        r.close();
        String body = response.toString();
        DocumentBuilder builder = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(body.getBytes());
        Document document = builder.parse(is);

        NodeList valuteItems = document.getElementsByTagName("Valute");
        ArrayList<Valute> valutes = new ArrayList<>();
        for (int i = 0; i < valuteItems.getLength(); i++) {
            Element valute = (Element) valuteItems.item(i);
            Valute tmp = new Valute();
            tmp.setValID(valute.getAttribute("ID"));
            tmp.setCharCode(valute.getElementsByTagName("NumCode").item(0).getTextContent());
            tmp.setCharCode(valute.getElementsByTagName("CharCode").item(0).getTextContent());
            tmp.setNominal(valute.getElementsByTagName("Nominal").item(0).getTextContent());
            tmp.setValue(valute.getElementsByTagName("Value").item(0).getTextContent());
            tmp.setName(valute.getElementsByTagName("Name").item(0).getTextContent());
            valutes.add(tmp);
        }
        return valutes;

    }

}
