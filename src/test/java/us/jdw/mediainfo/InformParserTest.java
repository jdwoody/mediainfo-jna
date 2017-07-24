package us.jdw.mediainfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class InformParserTest {
    
    @Test
    public void testParse() throws Exception {
        
        Map<String,MediaMetadata> results = InformParser.parse(getFileAsString("target/test-classes/us/jdw/mediainfo/InformParserTest.dat"));
        assertTrue(results.size()==3);
        IllegalStateException ise = null;
        try {
            InformParser.parse(getFileAsString("target/test-classes/us/jdw/mediainfo/InformParserTest.bad"));
            assertFalse("Should have IllegalStateException", true);
        } catch (IllegalStateException ex) {
            ise = ex;
        }
        assertTrue(ise!=null);
    }
    
    private String getFileAsString(String path) throws Exception {
        StringBuilder b = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path))));
        String line;
        while ((line=reader.readLine())!=null) {
            b.append(line).append('\n');
        }
        return b.toString();
    }

    
}
