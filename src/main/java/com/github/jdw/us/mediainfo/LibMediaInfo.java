package com.github.jdw.us.mediainfo;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.lang.reflect.Method;
import static java.util.Collections.singletonMap;

/**
 *
 * @author justin
 */
public interface LibMediaInfo extends Library {
    static final String LIB_NAME = "mediainfo";
    
    LibMediaInfo INSTANCE = (LibMediaInfo) Native.loadLibrary(LIB_NAME, LibMediaInfo.class, singletonMap(OPTION_FUNCTION_MAPPER, (FunctionMapper) (NativeLibrary lib, Method method) -> "MediaInfo_" + method.getName()));


    //Constructor/Destructor
    Pointer New();
    void Delete(Pointer Handle);

    //File
    int Open(Pointer Handle, WString file);
    int Open_Buffer_Init(Pointer handle, long length, long offset);
    int Open_Buffer_Continue(Pointer handle, byte[] buffer, int size);
    long Open_Buffer_Continue_GoTo_Get(Pointer handle);
    int Open_Buffer_Finalize(Pointer handle);
    void Close(Pointer Handle);

    //Infos
    WString Inform(Pointer Handle, int Reserved);
    WString Get(Pointer Handle, int StreamKind, int StreamNumber, WString parameter, int infoKind, int searchKind);
    WString GetI(Pointer Handle, int StreamKind, int StreamNumber, int parameterIndex, int infoKind);
    int     Count_Get(Pointer Handle, int StreamKind, int StreamNumber);

    //Options
    WString Option(Pointer Handle, WString option, WString value);
}
