package myproject.repository;

import myproject.entities.Entity;
import myproject.validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractXMLRepository<ID,E extends Entity<ID>> extends AbstractFileRepository {
    String filepath;
    StudentXMLRepository srep;
    TemaXMLRepository trep;
    public AbstractXMLRepository(Validator validator, String fileName) {
        super(validator, fileName);
        this.filepath=fileName;
        loadData();
    }
    public AbstractXMLRepository(Validator validator, String fileName,String fS,String fT) {
        super(validator, fileName);
        this.filepath=fileName;
        this.srep=new StudentXMLRepository(fS);
        this.trep=new TemaXMLRepository(fT);
        loadData();
    }

    @Override
    protected void writeData() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Pattern pattern = Pattern.compile(".*[\\\\]([a-zA-Z0-9]+)(FileXML).*[.][a-zA-Z0-9]+$");
            Matcher matcher = pattern.matcher(filepath);
            matcher.matches();


            Element root = document.createElement(matcher.group(1).toLowerCase()+"s");
            document.appendChild(root);
            super.findAll().forEach(s -> {
                Element e = createElementfromEntity(document, (E) s);
                root.appendChild(e);
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            //write Document to file

            Source source=new DOMSource(document);

            transformer.transform(source,
                    new StreamResult(filepath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(this.filepath);

            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node entityElement = children.item(i);
                if (entityElement instanceof Element) {
                    E entity = createEntityFromElement((Element) entityElement);
                    super.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Element createElementfromEntity(Document document, E entity);

    protected abstract E createEntityFromElement(Element entityElement);
}
