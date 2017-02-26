package com.github.jdw.us.mediainfo;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.io.IOException;

/**
 *
 */
public class MediaInfo implements AutoCloseable {
    
    private Pointer pointer;

    public MediaInfo(String fileName) throws IOException {
        pointer = LibMediaInfo.INSTANCE.New();
        int opened = LibMediaInfo.INSTANCE.Open(pointer, new WString(fileName));
        if (opened==0) {
            throw new IOException("Unable to open file with libmediainfo: " + fileName);
        }
    }

    /**
     * Get all details about a file.
     *
     * @return All details about a file in one string
     */
    public String inform() {
        return LibMediaInfo.INSTANCE.Inform(pointer, 0).toString();
    }

    /**
     * Get a piece of information about a file (parameter is a string).
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind StreamKind, int StreamNumber, String parameter) {
        return get(StreamKind, StreamNumber, parameter, InfoKind.Text, InfoKind.Name);
    }
    
    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameterIndex Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind StreamKind, int StreamNumber, int parameterIndex) {
        return get(StreamKind, StreamNumber, parameterIndex, InfoKind.Text);
    }

    /**
     * Get a piece of information about a file (parameter is a string).
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     */
    public String get(StreamKind StreamKind, int StreamNumber, String parameter, InfoKind infoKind) {
        return get(StreamKind, StreamNumber, parameter, infoKind, InfoKind.Name);
    }

    /**
     * Get a piece of information about a file (parameter is a string).
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @param searchKind Where to look for the parameter
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind StreamKind, int StreamNumber, String parameter, InfoKind infoKind, InfoKind searchKind) {
        return LibMediaInfo.INSTANCE.Get(pointer, StreamKind.ordinal(), StreamNumber, new WString(parameter), infoKind.ordinal(), searchKind.ordinal()).toString();
    }

    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameterIndex Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind StreamKind, int StreamNumber, int parameterIndex, InfoKind infoKind) {
        return LibMediaInfo.INSTANCE.GetI(pointer, StreamKind.ordinal(), StreamNumber, parameterIndex, infoKind.ordinal()).toString();
    }

    /**
     * Count of Streams of a Stream kind (StreamNumber not filled), or count of
     * piece of information in this Stream.
     *
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @return number of Streams of the given Stream kind
     */
    public int countGet(StreamKind StreamKind) {
        //We should use NativeLong for -1, but it fails on 64-bit
        //int Count_Get(Pointer Handle, int StreamKind, NativeLong StreamNumber);
        //return MediaInfoDLL_Internal.INSTANCE.Count_Get(Handle, StreamKind.ordinal(), -1);
        //so we use slower Get() with a character string
        String StreamCount = get(StreamKind, 0, "StreamCount");
        if (StreamCount == null || StreamCount.length() == 0) {
            return 0;
        }
        return Integer.parseInt(StreamCount);
    }

    /**
     * Count of Streams of a Stream kind (StreamNumber not filled), or count of
     * piece of information in this Stream.
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in this kind of Stream (first,
     * second...)
     * @return number of Streams of the given Stream kind
     */
    public int countGet(StreamKind StreamKind, int StreamNumber) {
        return LibMediaInfo.INSTANCE.Count_Get(pointer, StreamKind.ordinal(), StreamNumber);
    }

    /**
     * Configure or get information about MediaInfo.
     *
     * @param Option The name of option
     * @return Depends on the option: by default "" (nothing) means No, other
     * means Yes
     */
    public String Option(String Option) {
        return Option(Option, "");
    }

    /**
     * Configure or get information about MediaInfo.
     *
     * @param Option The name of option
     * @param Value The value of option
     * @return Depends on the option: by default "" (nothing) means No, other
     * means Yes
     */
    public String Option(String Option, String Value) {
        return LibMediaInfo.INSTANCE.Option(pointer, new WString(Option), new WString(Value)).toString();
    }
    /**
     * Closes the underlying file handle, and releases the native instance.
     * @throws Exception 
     */
    @Override
    public void close() throws Exception {
        if (pointer!=null) {
            LibMediaInfo.INSTANCE.Close(pointer);
            LibMediaInfo.INSTANCE.Delete(pointer);
            pointer = null;
        }
    }
    
    public static void setXMLOutput(MediaInfo m) {
        m.Option("Inform", "XML");
    }
}
