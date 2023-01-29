package net.tickmc.lccutils;

import net.tickmc.lccutils.managers.updates.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class is used to manually increment the version of the project in the pom.xml.
 * Do not use this class.
 */
public final class VersionIncrementer {

    private static final String POM_PATH = "pom.xml";
    private static final Version.Identifier INCREMENT = Version.Identifier.MINOR;

    public static void increment() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        String versionString = getVersion();
        Version version = new Version(versionString);
        version.increment(INCREMENT, 1);
        System.out.println("Incremented version! New version: " + version);

        String newVersionString = version.toString();

        System.out.println("Writing file...");
        // read the pom.xml file and save it to a string
        String pom = new String(Files.readAllBytes(new File(POM_PATH).toPath()));
        // replace the version with the new version
        pom = pom.replaceFirst(
            "<!-- VERSION_INCREMENT --> <version>" + versionString + "</version>",
            "<!-- VERSION_INCREMENT --> <version>" + newVersionString + "</version>");
        // write the new pom.xml file
        Files.write(new File(POM_PATH).toPath(), pom.getBytes());
        Files.write(new File("version.json").toPath(), version.toJSONString().getBytes());
        System.out.println("Done!");

    }

    private static Document getXml() throws ParserConfigurationException, IOException, SAXException {
        File pomFile = new File(POM_PATH).getAbsoluteFile();
        System.out.println("Incrementing version in " + pomFile);

        System.out.println("Reading file...");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(pomFile);
        return document;
    }

    private static String getVersion() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        Document document = getXml();

        System.out.println("Read file! Contents: " + document.getTextContent());

        XPath xPath = XPathFactory.newInstance().newXPath();
        System.out.println("Finding version node...");
        Node versionNode = (Node) xPath.compile("/project/version").evaluate(document, XPathConstants.NODE);

        System.out.println("Found version node! Contents: " + versionNode.getTextContent());
        return versionNode.getTextContent();
    }

    public static void main(String[] args) {
        try {
            increment();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
