#!/bin/bash
cd "$(dirname "$0")"
javac -d build -cp './../pmd-bin-6.14.0/lib/*' src/*.java

cp src/myrule.xml build/
cp ../../enablingConditions.xml build/
jar -cf pf2_custom_rules_enabler.jar -C build .
