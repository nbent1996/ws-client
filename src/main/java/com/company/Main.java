package com.company;

import jakarta.xml.soap.*;
import jakarta.xml.ws.Dispatch;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.soap.SOAPBinding;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
	consumirWebService();
    }
    private static void consumirWebService() {
        try {
            String endpointUrl = "http://localhost:9080/WS-/CalculatorEXTService/CalculatorEXTBean?wsdl";

            QName serviceName = new QName("http://Stateless/",
                    "CalculatorEXTService");
            QName portName = new QName("http://Stateless/",
                    "CalculatorEXTBeanPort");

/** Create a service and add at least one port to it. **/

            Service service;
            service = Service.create(serviceName);
            service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointUrl);

/** Create a Dispatch instance from a service.**/
            Dispatch<SOAPMessage> dispatch = service.createDispatch(portName,
                    SOAPMessage.class, Service.Mode.MESSAGE);

/** Create SOAPMessage request. **/
// compose a request message
            MessageFactory mf = null;
            mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
// Create a message.  This example works with the SOAPPART.
            SOAPMessage request = mf.createMessage();
            SOAPPart part = request.getSOAPPart();

// Obtain the SOAPEnvelope and header and body elements.
            SOAPEnvelope env = part.getEnvelope();
            SOAPHeader header = env.getHeader();
            SOAPBody body = env.getBody();

// Construct the message payload.
            SOAPElement operation = body.addChildElement("multiplicacion", "ns1",
                    "http://Stateless/");
            SOAPElement value1 = operation.addChildElement("arg0");
            SOAPElement value2 = operation.addChildElement("arg1");
            value1.addTextNode("25");
            value2.addTextNode("25");
            request.saveChanges();

/** Invoke the service endpoint. **/
            SOAPMessage response = dispatch.invoke(request);
            /** Process the response. **/
            SOAPBody bodyFinal = response.getSOAPBody();
            SOAPElement elemento = getFirstBodyElement(bodyFinal);
            String resultado = elemento.getTextContent();
            System.out.println("Resultado de multiplicar arg0 x arg1 (25x25) = " + resultado);

        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    public static SOAPElement getFirstBodyElement(SOAPBody body) {
        for (Iterator<?> iterator = body.getChildElements(); iterator.hasNext(); ) {
            Object child = iterator.next();
            if (child instanceof SOAPElement) {
                return (SOAPElement) child;
            }
        }
        return null;
    }
}
