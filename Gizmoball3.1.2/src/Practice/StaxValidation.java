package Practice;

import javax.xml.validation.*;
import javax.xml.transform.stax.*;
import javax.xml.stream.*;
import javax.xml.*;
import java.io.*;

public class StaxValidation {

    public static void main (String args[]) throws Exception {

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream("src/Practice/board1.xml"));

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File("src/Practice/gb_level.xsd"));

        Validator validator = schema.newValidator();
        validator.validate(new StAXSource(reader));

        //no exception thrown, so valid
        System.out.println("Document is valid");

    }
}