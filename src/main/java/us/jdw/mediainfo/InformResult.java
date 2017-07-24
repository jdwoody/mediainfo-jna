package us.jdw.mediainfo;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Wrapper to perform JAXB Serialization
 */
@XmlRootElement
public class InformResult {
    @XmlTransient
    private static final JAXBContext CTX; 
    
    @XmlElement
    LinkedHashMap<String, MediaMetadata> results = new LinkedHashMap<>();

    public InformResult() {
    }

    public InformResult(Map<String, MediaMetadata> map) {
        results.putAll(map);
    }

    public LinkedHashMap<String, MediaMetadata> getResults() {
        return results;
    }
    
    static {
        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(InformResult.class);
        } catch (JAXBException ex) {
            // should never happen
            ctx = null;
        }
        CTX = ctx;
    }
    
    public static InformResult getResult(String file) throws JAXBException {
        return (InformResult)CTX.createUnmarshaller().unmarshal(new File(file));   
    }
    
    public static void saveResult(InformResult ir, String filename) throws JAXBException {
        Marshaller m = CTX.createMarshaller();
        m.marshal(ir, new File(filename));
    }
    
}
