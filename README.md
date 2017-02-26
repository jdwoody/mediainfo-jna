# mediainfo-jna README

This is a packaged build of MediaInfo for use in Java environments. The source is based on the JNA example contained in MediaLib (see below for MediaInfo details).

## Building

Java jdk 8
Maven 3

```sh
mvn clean install
```
## Running

To run from the command line
```sh
java -jar `ls -1 mediainfo-jna-*jar-with-dependencies.jar` [your file]
```

Currently tested on CentOS 7 and macOS Seirra.

## MediaLib Information

Based on the project MediaInfo:
https://mediaarea.net/MediaInfo

Source code
https://github.com/MediaArea/MediaInfoLib

Current versions included
Linux library - Zen version v0.4.34 and MediaInfo v0.7.92
Mac library - MediaInfo v0.7.92.1 downloaded from mediaarea.net
