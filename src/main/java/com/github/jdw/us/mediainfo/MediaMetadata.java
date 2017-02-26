package com.github.jdw.us.mediainfo;

/**
 * Main class to print MediaInfo Meta Data
 */
public class MediaMetadata {
  
    public static void main(String[] args) throws Exception {
        if (args.length==0) {
            System.out.println("com.github.jdw.us.mediainfo.MediaMetadata.main() Pass in a fully qualified path to a file");
            System.exit(1);
        }
        try (MediaInfo mi = new MediaInfo(args[0])) {
            MediaInfo.setXMLOutput(mi);
            System.out.println("com.github.jdw.us.mediainfo.MediaMetadata.main() " + mi.inform());
        }
        
    }
}
