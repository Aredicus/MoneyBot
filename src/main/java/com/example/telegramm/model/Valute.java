package com.example.telegramm.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Valute {
    @XmlAttribute(name = "ID")
    String ValID;
    @XmlElement(name = "NumCode")
    String NumCode;
    @XmlElement(name = "CharCode")
    String CharCode;
    @XmlElement(name = "Nominal")
    String Nominal;
    @XmlElement(name = "Name")
    String Name;
    @XmlElement(name = "Value")
    String Value;

    public String toString() {
        return "RUB:"+getValue()+"\n"+getCharCode()+":"+getNominal();
    }
}
