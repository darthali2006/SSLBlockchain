JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		certificate.java \
		PutCertificate.java \
		blocks.java \
		CheckBlockchain.java \


default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class