package com.github.jdw.us.mediainfo;

import com.sun.jna.Pointer;
import com.sun.jna.WString;

/**
 *
 */
public class MediaInfo implements AutoCloseable {
    
    public enum StreamKind {
        General,
        Video,
        Audio,
        Text,
        Other,
        Image,
        Menu;
    }

    public enum InfoKind {
        /**
         * Unique name of parameter.
         */
        Name,
        /**
         * Value of parameter.
         */
        Text,
        /**
         * Unique name of measure unit of parameter.
         */
        Measure,
        Options,
        /**
         * Translated name of parameter.
         */
        Name_Text,
        /**
         * Translated name of measure unit.
         */
        Measure_Text,
        /**
         * More information about the parameter.
         */
        Info,

        /**
         * How this parameter is supported, could be N (No), B (Beta), R (Read only), W
         * (Read/Write).
         */
        HowTo,

        /**
         * Domain of this piece of information.
         */
        Domain;
    }

    public enum Status {
        None        (0x00),
        Accepted    (0x01),
        Filled      (0x02),
        Updated     (0x04),
        Finalized   (0x08);
        
        private int value;
        private Status(int value) {this.value = value;}
        public  int getValue(int value) {return value;}
    }
    
    private Pointer pointer;

    public MediaInfo() {
        pointer = LibMediaInfo.INSTANCE.New();
    }

    /**
     * Open a file and collect information about it (technical information and
     * tags).
     *
     * @param fileName full name of the file to open
     * @return 1 if file was opened, 0 if file was not not opened
     */
    public int Open(String fileName) {
        return LibMediaInfo.INSTANCE.Open(pointer, new WString(fileName));
    }

    public int Open_Buffer_Init(long length, long offset) {
        return LibMediaInfo.INSTANCE.Open_Buffer_Init(pointer, length, offset);
    }

    /**
     * Open a stream and collect information about it (technical information and
     * tags) (By buffer, Continue)
     *
     * @param buffer pointer to the stream
     * @param size Count of bytes to read
     * @return a bitfield bit 0: Is Accepted (format is known) bit 1: Is Filled
     * (main data is collected) bit 2: Is Updated (some data have beed updated,
     * example: duration for a real time MPEG-TS stream) bit 3: Is Finalized (No
     * more data is needed, will not use further data) bit 4-15: Reserved bit
     * 16-31: User defined
     */
    public int Open_Buffer_Continue(byte[] buffer, int size) {
        return LibMediaInfo.INSTANCE.Open_Buffer_Continue(pointer, buffer, size);
    }

    public long Open_Buffer_Continue_GoTo_Get() {
        return LibMediaInfo.INSTANCE.Open_Buffer_Continue_GoTo_Get(pointer);
    }

    public int Open_Buffer_Finalize() {
        return LibMediaInfo.INSTANCE.Open_Buffer_Finalize(pointer);
    }

    /**
     * Close a file opened before with Open().
     *
     */
    public void Close() {
        LibMediaInfo.INSTANCE.Close(pointer);
    }

    //Information
    /**
     * Get all details about a file.
     *
     * @return All details about a file in one string
     */
    public String Inform() {
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
    public String Get(StreamKind StreamKind, int StreamNumber, String parameter) {
        return Get(StreamKind, StreamNumber, parameter, InfoKind.Text, InfoKind.Name);
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
     */
    public String Get(StreamKind StreamKind, int StreamNumber, String parameter, InfoKind infoKind) {
        return Get(StreamKind, StreamNumber, parameter, infoKind, InfoKind.Name);
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
    public String Get(StreamKind StreamKind, int StreamNumber, String parameter, InfoKind infoKind, InfoKind searchKind) {
        return LibMediaInfo.INSTANCE.Get(pointer, StreamKind.ordinal(), StreamNumber, new WString(parameter), infoKind.ordinal(), searchKind.ordinal()).toString();
    }

    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind StreamKind, int StreamNumber, int parameterIndex) {
        return Get(StreamKind, StreamNumber, parameterIndex, InfoKind.Text);
    }

    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param StreamKind Kind of Stream (general, video, audio...)
     * @param StreamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String Get(StreamKind StreamKind, int StreamNumber, int parameterIndex, InfoKind infoKind) {
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
    public int Count_Get(StreamKind StreamKind) {
        //We should use NativeLong for -1, but it fails on 64-bit
        //int Count_Get(Pointer Handle, int StreamKind, NativeLong StreamNumber);
        //return MediaInfoDLL_Internal.INSTANCE.Count_Get(Handle, StreamKind.ordinal(), -1);
        //so we use slower Get() with a character string
        String StreamCount = Get(StreamKind, 0, "StreamCount");
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
    public int Count_Get(StreamKind StreamKind, int StreamNumber) {
        return LibMediaInfo.INSTANCE.Count_Get(pointer, StreamKind.ordinal(), StreamNumber);
    }

    //Options
    /**
     * Configure or get information about MediaInfo.
     *
     * @param Option The name of option
     * @return Depends on the option: by default "" (nothing) means No, other
     * means Yes
     */
    public String Option(String Option) {
        return LibMediaInfo.INSTANCE.Option(pointer, new WString(Option), new WString("")).toString();
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

    @Override
    public void close() throws Exception {
        if (pointer!=null) {
            Close();
            LibMediaInfo.INSTANCE.Delete(pointer);
            pointer = null;
        }
    }
}
