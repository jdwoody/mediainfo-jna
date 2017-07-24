package us.jdw.mediainfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple parser to adapt inform response to meta objects.
 * The default MediaInfo inform call returns data in the form<br>
 * <pre>General
Complete name                            : /path/to/input/file.ext
Format                                   : Windows Media
...

Video
ID                                       : 1
Format                                   : FMT
Codec ID                                 : CODECID
...

Audio
ID                                       : 2
Format                                   : MP3
...

 * </pre>
 */
public class InformParser {

    static final String DELIM = ":";
    
    /**
     * 
     * @param inform A MediaInfo formatted inform string
     * @return Map containing parsed meta data.
     */
    public static Map<String, MediaMetadata> parse(String inform) {
        LinkedHashMap<String, MediaMetadata> results = new LinkedHashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(inform));
            String line;
            Map<String,String> pairs = new LinkedHashMap<>();
            // first read should always be the type
            while ((line=reader.readLine())!=null) {
                if (!line.isEmpty()) {
                    results.put(line,createMM(line, reader, pairs));
                }
            }
        } catch (IOException ex) {
            // shouldn't happen since we're reading from a String
            // TODO add logging?
        }
        return results;
    }
    
    static MediaMetadata createMM(String type, BufferedReader r, Map<String,String> pairs) throws IOException {
        pairs.clear();
        String meta;
        while ((meta=r.readLine())!=null&&!meta.isEmpty()) {
            int idx = meta.indexOf(DELIM);
            if (idx == -1) {
                throw new IllegalStateException("Parser received unexpected data, should contain ':'. Type=" + type + " line=" + meta);
            }
            String key = meta.substring(0,idx).trim();
            String value = meta.substring(++idx).trim();
            pairs.put(key, value);
        }
        return new MediaMetadata(type, pairs);
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar .jar [FILE]...");
            System.exit(1);
        }
        for (String arg : args) {
            try (MediaInfo mi = new MediaInfo(arg)) {
                Map<String,MediaMetadata> results = InformParser.parse(mi.inform());
                System.out.println("Inform " + arg + "\n" + results + "\n");
            }
        }

    }
}
