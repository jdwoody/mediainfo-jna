package us.jdw.mediainfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */
public class MediaInfoTest {
    
    static final String COMP_NAME = "Complete name";
    
    String[] paths = new String[] {"target/test-classes/videos",
        "target/test-classes/audio",
        "target/test-classes/images"};
    
    boolean isBuffered = false;
    
    @Test
    public void testInform() throws Exception {
        for (String path : paths) {
            Path p = Paths.get(path);
            Files.list(p).filter(m -> !m.toString().endsWith("xml")).forEach(m -> testPath(m));
        }
    }
    
    public void testPath(Path p) {
        try (MediaInfo mi = new MediaInfo(p.toString())) {
            Map<String,MediaMetadata> expected = InformResult.getResult(p.toString() + ".xml").getResults();
            Map<String,MediaMetadata> results = InformParser.parse(mi.inform());
            validate(results,expected);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        isBuffered = true;
        try (MediaInfo mi = new MediaInfo(Files.newByteChannel(p, StandardOpenOption.READ))) {
            Map<String,MediaMetadata> expected = InformResult.getResult(p.toString() + ".xml").getResults();
            Map<String,MediaMetadata> results = InformParser.parse(mi.inform());
            validate(results,expected);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }

    private void validate(Map<String, MediaMetadata> results, Map<String, MediaMetadata> expected) {
        Assert.assertTrue("Results and Expected have equal elements", results.size()==expected.size());
        for (Map.Entry<String, MediaMetadata> entry : results.entrySet()) {
            MediaMetadata rMD = entry.getValue();
            MediaMetadata eMD = expected.get(entry.getKey());
            Assert.assertTrue("No null Metadata objects", rMD!=null&&eMD!=null);
            Assert.assertTrue("Types equal", rMD.getType().equals(eMD.getType()));
            Assert.assertTrue("Expected contains all result keys",eMD.getMetaData().keySet().containsAll(rMD.getMetaData().keySet()));
            if (!isBuffered) {
                Assert.assertTrue("Results contains all expected keys",rMD.getMetaData().keySet().containsAll(eMD.getMetaData().keySet()));
            }
            Assert.assertTrue("All values are consistent", checkValues(rMD.getMetaData(),eMD.getMetaData()));
        }
    }

    private boolean checkValues(Map<String, String> results, Map<String, String> expected) {
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            String eVal = expected.get(key);
            if (COMP_NAME.equals(key)) {
                if (!isBuffered) {
                    Assert.assertTrue("Complete name incorrect. " + key + " expected:" + eVal + " actual:" + val, val.endsWith(eVal));
                }
            } else {
                Assert.assertTrue("Values unequal. " + key + " expected:" + eVal + " actual:" + val, val.equals(eVal));
            }
        }
        return true;
    }

    
    
}
