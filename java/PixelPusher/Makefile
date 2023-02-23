SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

all: clean bin doc UniPixelPusher.jar UniPixelPusher.app

eclipseall: eclipseclean UniPixelPusher.jar UniPixelPusher.app

clean:
	rm -rf bin
	rm -rf doc
	rm -rf UniPixelPusher*.jar
	rm -rf UniPixelPusher*.app

eclipseclean:
	rm -rf UniPixelPusher*.jar
	rm -rf UniPixelPusher*.app

bin: src
	mkdir -p bin/META-INF/services
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -classpath dep/imagetool.jar:dep/joda-time-2.1.jar:dep/nrserial.jar:dep/ual.jar:dep/PixelPusher.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -classpath dep/imagetool.jar:dep/joda-time-2.1.jar:dep/nrserial.jar:dep/ual.jar:dep/PixelPusher.jar -sourcepath src $(PACKAGES) -d doc

UniPixelPusher.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF UniPixelPusher.jar -C bin META-INF -C bin com/kreative/unipixelpusher
	chmod +x UniPixelPusher.jar

UniPixelPusher.app: UniPixelPusher-Pre10.15.app UniPixelPusher-MacOS10.15.app UniPixelPusher-MacOS11.0.app

UniPixelPusher-Pre10.15.app: dep UniPixelPusher.jar
	mkdir -p UniPixelPusher-Pre10.15.app/Contents/MacOS
	mkdir -p UniPixelPusher-Pre10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo UniPixelPusher-Pre10.15.app/Contents
	cp -f dep/Info.plist UniPixelPusher-Pre10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-Pre10.15 UniPixelPusher-Pre10.15.app/Contents/MacOS/UniPixelPusher
	cp -f dep/*.icns UniPixelPusher-Pre10.15.app/Contents/Resources
	cp -f dep/*.jar UniPixelPusher-Pre10.15.app/Contents/Resources/Java
	cp -Rf presets UniPixelPusher-Pre10.15.app/Contents/Resources/Presets
	cp -f UniPixelPusher.jar UniPixelPusher-Pre10.15.app/Contents/Resources/Java

UniPixelPusher-MacOS10.15.app: dep UniPixelPusher.jar
	mkdir -p UniPixelPusher-MacOS10.15.app/Contents/MacOS
	mkdir -p UniPixelPusher-MacOS10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo UniPixelPusher-MacOS10.15.app/Contents
	cp -f dep/Info.plist UniPixelPusher-MacOS10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS10.15 UniPixelPusher-MacOS10.15.app/Contents/MacOS/UniPixelPusher
	cp -f dep/*.icns UniPixelPusher-MacOS10.15.app/Contents/Resources
	cp -f dep/*.jar UniPixelPusher-MacOS10.15.app/Contents/Resources/Java
	cp -Rf presets UniPixelPusher-MacOS10.15.app/Contents/Resources/Presets
	cp -f UniPixelPusher.jar UniPixelPusher-MacOS10.15.app/Contents/Resources/Java

UniPixelPusher-MacOS11.0.app: dep UniPixelPusher.jar
	mkdir -p UniPixelPusher-MacOS11.0.app/Contents/MacOS
	mkdir -p UniPixelPusher-MacOS11.0.app/Contents/Resources/Java
	cp -f dep/PkgInfo UniPixelPusher-MacOS11.0.app/Contents
	cp -f dep/Info.plist UniPixelPusher-MacOS11.0.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS11.0 UniPixelPusher-MacOS11.0.app/Contents/MacOS/UniPixelPusher
	cp -f dep/*.icns UniPixelPusher-MacOS11.0.app/Contents/Resources
	cp -f dep/*.jar UniPixelPusher-MacOS11.0.app/Contents/Resources/Java
	cp -Rf presets UniPixelPusher-MacOS11.0.app/Contents/Resources/Presets
	cp -f UniPixelPusher.jar UniPixelPusher-MacOS11.0.app/Contents/Resources/Java

.PHONY: all eclipseall clean eclipseclean UniPixelPusher.app
