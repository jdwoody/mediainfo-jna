# mediainfo-jna README

This is a packaged build of MediaInfo for use in Java environments. The source is based on the JNA example contained in MediaLib (see below for MediaInfo details).

## Building

* Java jdk 8
* Maven 3

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

Current versions included
* Linux library - Shared library that indludes MediaInfo v0.7.97, Zen version v0.4.35, and zlib builtin
* Mac library - MediaInfo v0.7.97 downloaded from mediaarea.net
* Windows -Testing and verification required prior to inclusion

Based on the project MediaInfo:
This product uses [MediaInfo](http://mediaarea.net/MediaInfo) library, Copyright (c) 2002-2017 [MediaArea.net](mailto:Info@MediaArea.net) SARL
https://mediaarea.net/MediaInfo

Source code
https://github.com/MediaArea/MediaInfoLib

### MediaLib Disclaimer
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

## Test files

Several audio and video files are included for test purposes. These were obtained
from the web and were marked as free for commercial use and/or open source. If you feel that
any of these files should be removed, please contact us via Github issue.

Video files were obtained from:
- [small.*](http://standaloneinstaller.com/blog/big-list-of-sample-videos-for-testers-124.html)

Audio files obtained from freesound.org and discovered through the [test audio](https://github.com/ArtskydJ/test-audio.git) project:
- [cf_FD_bloibb.mp3](http://www.freesound.org/people/cfork/sounds/8000/)
- [drippy.flac](http://www.freesound.org/people/Corsica_S/sounds/30047/)
- [drips2.ogg](http://www.freesound.org/people/smcameron/sounds/50775/)
- [drip2.wav](http://www.freesound.org/people/Neotone/sounds/75344/)

Images devaintart.net
- [album.jpg](http://orig15.deviantart.net/bece/f/2014/311/5/7/buster_s_adventures_4___scary_encounter_by_busterthefox-d85numc.jpg)
