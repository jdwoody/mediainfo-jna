package us.jdw.mediainfo;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.lang.reflect.Method;
import static java.util.Collections.singletonMap;

/**
 * JNA Wrapper for MediaInfo.
 */
public interface LibMediaInfo extends Library {
    
    static final String LIB_NAME = "mediainfo";
    
    final LibMediaInfo INSTANCE = (LibMediaInfo) Native.loadLibrary(LIB_NAME, LibMediaInfo.class, singletonMap(OPTION_FUNCTION_MAPPER, (FunctionMapper) (NativeLibrary lib, Method method) -> "MediaInfo_" + method.getName()));

    
    /* Constructor */
    Pointer New();
    
    /* Deconstructor */
    void Delete(Pointer Handle);

    /* Opens a file for inspection */
    int Open(Pointer Handle, WString file);
    /* Closes the file upon completion */
    void Close(Pointer Handle);
    
    /* Opens a buffered read for the specified length beginning at offset */
    int Open_Buffer_Init(Pointer handle, long length, long offset);
    
    int Open_Buffer_Continue(Pointer handle, byte[] buffer, int size);
    
    long Open_Buffer_Continue_GoTo_Get(Pointer handle);
    /* release buffer resources (close) */
    int Open_Buffer_Finalize(Pointer handle);
    
    

    /* return information in various ways */
    WString Inform(Pointer Handle, int Reserved);
    WString Get(Pointer Handle, int StreamKind, int StreamNumber, WString parameter, int infoKind, int searchKind);
    WString GetI(Pointer Handle, int StreamKind, int StreamNumber, int parameterIndex, int infoKind);
    int     Count_Get(Pointer Handle, int StreamKind, int StreamNumber);

    /* Set options */
    WString Option(Pointer Handle, WString option, WString value);
}
