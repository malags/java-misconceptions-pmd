https://github.com/pmd/pmd-examples/tree/java-without-maven

basic methods:
https://static.javadoc.io/net.sourceforge.pmd/pmd-core/6.13.0/net/sourceforge/pmd/lang/ast/Node.html

To build rules:

cd pmd-examples
javac -d build -cp './../pmd-bin-6.12.0/lib/*' src/*.java

cp src/myrule.xml build/
jar -cf pf2_custom_rules.jar -C build .


Run:
from folder pmd-examples
CLASSPATH=pf2_custom_rules.jar ./../pmd-bin-6.11.0/bin/run.sh pmd -no-cache -f text -d testsrc -R myrule.xml
