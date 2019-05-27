#!/bin/bash
cd "$(dirname "$0")"
CLASSPATH=pf2_custom_rules_enabler.jar ./../pmd-bin-6.14.0/bin/run.sh pmd -no-cache -f text -d testsrc -R myrule.xml
