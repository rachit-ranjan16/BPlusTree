JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        KeyValPair.java \
        BaseNode.java \
        KeyNode.java \
        DictPairNode.java \
        BTree.java \
        FileParser.java \
        treesearch.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class
