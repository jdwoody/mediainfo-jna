package us.jdw.mediainfo;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Class to wrap calls to LibMediaInfo via JNA.
 * This class takes either a file path or a SeekableByteChannel and calls to 
 * LibMediaInfo to generate meta info. This class <b>must be closed</b>
 * to properly release Native resources. It implements <code>AutoCloseable</code> to allow
 * use try with resources.
 */
public class MediaInfo implements AutoCloseable {
    
    private Pointer pointer;

    private MediaInfo() {
        pointer = LibMediaInfo.INSTANCE.New();
    }
    
    public MediaInfo(String fileName) throws IOException {
        this();
        int opened = LibMediaInfo.INSTANCE.Open(pointer, new WString(fileName));
        if (opened==0) {
            throw new IOException("Unable to open file with libmediainfo: " + fileName);
        }
    }
    
    public MediaInfo(SeekableByteChannel channel) throws IOException {
        this();
        // open, read and finalize the data in the buffer
        long len = channel.size();
        final byte[] buff = new byte[64*1024];
        final ByteBuffer dst = ByteBuffer.wrap(buff);
        int read;
        LibMediaInfo.INSTANCE.Open_Buffer_Init(pointer, len, 0L);
        while ((read = channel.read(dst))!=-1) {
            int status = LibMediaInfo.INSTANCE.Open_Buffer_Continue(pointer, buff, read);
            if ((status & Status.Finalized.val) == Status.Finalized.val) {
                break;
            }
            long seekPos = LibMediaInfo.INSTANCE.Open_Buffer_Continue_GoTo_Get(pointer);
            if (seekPos != -1) {
                channel.position(seekPos);
                LibMediaInfo.INSTANCE.Open_Buffer_Init(pointer, len, seekPos);
            }
            dst.clear();
        }
        LibMediaInfo.INSTANCE.Open_Buffer_Finalize(pointer);
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
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind streamKind, int streamNumber, String parameter) {
        return get(streamKind, streamNumber, parameter, InfoKind.Text, InfoKind.Name);
    }
    
    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in Kind of Stream (first, second...)
     * @param parameterIndex Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @return information requested, empty string if not found
     */
    public String get(StreamKind streamKind, int streamNumber, int parameterIndex) {
        return get(streamKind, streamNumber, parameterIndex, InfoKind.Text);
    }

    /**
     * Get a piece of information about a file (parameter is a string).
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @return information requested, empty string if not found
     */
    public String get(StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind) {
        return get(streamKind, streamNumber, parameter, infoKind, InfoKind.Name);
    }

    /**
     * Get a piece of information about a file (parameter is a string).
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in Kind of Stream (first, second...)
     * @param parameter Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in string format ("Codec", "Width"...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @param searchKind Where to look for the parameter
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind, InfoKind searchKind) {
        return LibMediaInfo.INSTANCE.Get(pointer, streamKind.ordinal(), streamNumber, new WString(parameter), infoKind.ordinal(), searchKind.ordinal()).toString();
    }

    /**
     * Get a piece of information about a file (parameter is an integer).
     *
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in Kind of Stream (first, second...)
     * @param parameterIndex Parameter you are looking for in the Stream (Codec,
     * width, bitrate...), in integer format (first parameter, second
     * parameter...)
     * @param infoKind Kind of information you want about the parameter (the
     * text, the measure, the help...)
     * @return a string about information you search, an empty string if there
     * is a problem
     */
    public String get(StreamKind streamKind, int streamNumber, int parameterIndex, InfoKind infoKind) {
        return LibMediaInfo.INSTANCE.GetI(pointer, streamKind.ordinal(), streamNumber, parameterIndex, infoKind.ordinal()).toString();
    }

    /**
     * Count of Streams of a Stream kind (StreamNumber not filled), or count of
     * piece of information in this Stream.
     *
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @return number of Streams of the given Stream kind
     */
    public int getCount(StreamKind streamKind) {
        //We should use NativeLong for -1, but it fails on 64-bit
        //int Count_Get(Pointer Handle, int StreamKind, NativeLong StreamNumber);
        //return MediaInfoDLL_Internal.INSTANCE.Count_Get(Handle, StreamKind.ordinal(), -1);
        //so we use slower Get() with a character string
        String streamCount = get(streamKind, 0, "StreamCount");
        if (streamCount == null || streamCount.length() == 0) {
            return 0;
        }
        return Integer.parseInt(streamCount);
    }

    /**
     * Count of Streams of a Stream kind in the Stream Number.
     *
     * @param streamKind Kind of Stream (general, video, audio...)
     * @param streamNumber Stream number in this kind of Stream (first,
     * second...)
     * @return number of Streams of the given Stream kind
     */
    public int getCount(StreamKind streamKind, int streamNumber) {
        return LibMediaInfo.INSTANCE.Count_Get(pointer, streamKind.ordinal(), streamNumber);
    }

    /**
     * Configure or get information about MediaInfo.
     *
     * @param option The name of option
     * @return Depends on the option: by default "" (nothing) means No, other
     * means Yes
     */
    public String option(String option) {
        return option(option, "");
    }

    /**
     * Configure or get information about MediaInfo.
     *
     * @param option The name of option
     * @param value The value of option
     * @return Depends on the option: by default "" (nothing) means No, other
     * means Yes
     */
    public String option(String option, String value) {
        return LibMediaInfo.INSTANCE.Option(pointer, new WString(option), new WString(value)).toString();
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
        m.option("Inform", "XML");
    }
}
