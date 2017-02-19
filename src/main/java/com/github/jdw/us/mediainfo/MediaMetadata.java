package com.github.jdw.us.mediainfo;

/**
 *
 */
public class MediaMetadata {
  
    public static void main(String[] args) throws Exception {
        if (args.length==0) {
            System.out.println("com.github.jdw.us.mediainfo.MediaMetadata.main() Pass in a fully qualified path to a file");
            System.exit(1);
        }
        try (MediaInfo mi = new MediaInfo()) {
            mi.Open(args[0]);
            System.out.println("com.github.jdw.us.mediainfo.MediaMetadata.main() " + mi.Inform());
        }
        
    }
}
