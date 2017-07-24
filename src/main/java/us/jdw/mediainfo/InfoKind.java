package us.jdw.mediainfo;

/**
 *
 */
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
