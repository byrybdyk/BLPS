package com.zarubovandlevchenko.lb1.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class UserRegistration {
    public void registerUser(String id, String username, String password,
                             String cardNumber, String phoneNumber) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/resources/users.xml"));
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        Element user = doc.createElement("user");
        Element userId = doc.createElement("id");
        userId.appendChild(doc.createTextNode(id));
        user.appendChild(userId);
        Element roles = doc.createElement("roles");
        Element roleElement = doc.createElement("role");
        roleElement.appendChild(doc.createTextNode("ROLE_USER"));
        roles.appendChild(roleElement);
        user.appendChild(roles);
        Element credentials = doc.createElement("credentials");
        Element card = doc.createElement("card");
        Element cardNumberElement = doc.createElement("number");
        cardNumberElement.appendChild(doc.createTextNode(cardNumber));
        Element otpElement = doc.createElement("otp");
        otpElement.appendChild(doc.createTextNode("12345"));
        card.appendChild(cardNumberElement);
        card.appendChild(otpElement);
        credentials.appendChild(card);
        Element login = doc.createElement("login");
        Element usernameElement = doc.createElement("username");
        usernameElement.appendChild(doc.createTextNode(username));
        Element passwordElement = doc.createElement("password");
        passwordElement.appendChild(doc.createTextNode(password));
        login.appendChild(usernameElement);
        login.appendChild(passwordElement);
        credentials.appendChild(login);
        Element phone = doc.createElement("phone");
        Element phoneNumberElement = doc.createElement("number");
        phoneNumberElement.appendChild(doc.createTextNode(phoneNumber));
        Element phonePasswordElement = doc.createElement("password");
        phonePasswordElement.appendChild(doc.createTextNode(password));
        phone.appendChild(phoneNumberElement);
        phone.appendChild(phonePasswordElement);
        credentials.appendChild(phone);
        user.appendChild(credentials);
        root.appendChild(user);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/main/resources/users.xml"));
        transformer.transform(source, result);
    }
}
