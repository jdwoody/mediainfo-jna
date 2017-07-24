package us.jdw.mediainfo;

/**
 * Status codes when reading from a buffer.
 */
public enum Status {
    None(0x00),
    Accepted(0x01),
    Filled(0x02),
    Updated(0x04),
    Finalized(0x08);
    
    public final int val;
    Status(int val) {
        this.val = val;
    }
}
