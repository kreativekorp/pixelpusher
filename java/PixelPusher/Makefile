SRCFILES := $(shell find src/com -type f \! -path \*/.\* -name \*.java)
RESFILES := $(shell find src/com -type f \! -path \*/.\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src/com/kreative/pixelpusher -type d \! -path \*/.\* | sed s/^src\\/// | tr / .)

all: clean bin doc PixelPusherCLI.jar

eclipseall: eclipseclean PixelPusherCLI.jar

clean:
	rm -rf bin
	rm -rf doc
	rm -rf PixelPusherCLI.jar

eclipseclean:
	rm -rf PixelPusherCLI.jar

bin: src
	mkdir -p bin
	javac -classpath dep/nrserial.jar:dep/PixelPusher.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc -classpath dep/nrserial.jar:dep/PixelPusher.jar -sourcepath src $(PACKAGES) -d doc

PixelPusherCLI.jar: src bin
	export COPYFILE_DISABLE=true
	find src -name .DS_Store -delete
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF PixelPusherCLI.jar -C src META-INF -C bin com/kreative/pixelpusher

 .PHONY: all eclipseall clean eclipseclean
 