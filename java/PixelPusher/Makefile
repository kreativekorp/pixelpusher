SRCFILES := $(shell find src/com/kreative/unipixelpusher -type f \! -path \*/.\* -name \*.java)
RESFILES := $(shell find src/com/kreative/unipixelpusher -type f \! -path \*/.\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src/com/kreative/unipixelpusher -type d \! -path \*/.\* | sed s/^src\\/// | tr / .)

all: clean bin doc UniPixelPusher.jar UniPixelPusher.app

eclipseall: eclipseclean UniPixelPusher.jar UniPixelPusher.app

clean:
	rm -rf bin
	rm -rf doc
	rm -rf UniPixelPusher.jar
	rm -rf UniPixelPusher.app

eclipseclean:
	rm -rf UniPixelPusher.jar
	rm -rf UniPixelPusher.app

bin: src
	mkdir -p bin
	javac -classpath dep/imagetool.jar:dep/nrserial.jar:dep/PixelPusher.jar:dep/orange-extensions.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc -classpath dep/imagetool.jar:dep/nrserial.jar:dep/PixelPusher.jar:dep/orange-extensions.jar -sourcepath src $(PACKAGES) -d doc

UniPixelPusher.jar: src bin
	export COPYFILE_DISABLE=true
	find src -name .DS_Store -delete
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF UniPixelPusher.jar -C src META-INF -C bin com/kreative/unipixelpusher

UniPixelPusher.app: dep UniPixelPusher.jar
	mkdir -p UniPixelPusher.app/Contents/MacOS
	mkdir -p UniPixelPusher.app/Contents/Resources/Java
	cp -f dep/PkgInfo UniPixelPusher.app/Contents
	cp -f dep/Info.plist UniPixelPusher.app/Contents
	cp -f dep/universalJavaApplicationStub UniPixelPusher.app/Contents/MacOS/UniPixelPusher
	cp -f dep/*.jar UniPixelPusher.app/Contents/Resources/Java
	cp -Rf presets UniPixelPusher.app/Contents/Resources/Presets
	cp -f UniPixelPusher.jar UniPixelPusher.app/Contents/Resources/Java

.PHONY: all eclipseall clean eclipseclean
