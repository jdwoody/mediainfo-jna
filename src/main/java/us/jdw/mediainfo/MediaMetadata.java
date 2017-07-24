package us.jdw.mediainfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Simple data object to hold MediaInfo inform results.
 */
@XmlType
public final class MediaMetadata {
    @XmlElement
    String type;
    
    @XmlElement
    Map<String, String> metaData;

    /* JAXB */
    MediaMetadata() {
    }
    
    public MediaMetadata(String type, Map<String, String> metaData) {
        this.type = type;
        this.metaData = Collections.unmodifiableMap(new LinkedHashMap<>(metaData));
    }
    
    /**
     * The meta info type (General, Video, Audio, etc)
     * @return 
     */
    public String getType() {
        return type;
    }

    /**
     * The meta data information for this type. The underlying implementation
     * is an ordered map, but order may not be maintained if the object is 
     * constructed with an unordered Map.
     * @return Unmodifiable meta map
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }
    
    /**
     * Returns a single meta data value from the meta map.
     * @param key
     * @return value if present or null
     */
    public String getValue(final String key) {
        return metaData.get(key);
    }

    @Override
    public String toString() {
        return "MediaMetadata{" + "type=" + type + ", metaData=" + metaData + '}';
    }
    
}
