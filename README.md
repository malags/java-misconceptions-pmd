# java-misconceptions-pmd
Detecting misconceptions in Java code with PMD


# Setup:
Install [PyGithub](https://pygithub.readthedocs.io/):

```
pip3 install PyGithub
```


On your GitHub account, go to "Developer Settings" and create a "Personal Access Token". You will use this token to get programmatic access to Github (via the PyGithub API):

[https://github.com/settings/tokens](https://github.com/settings/tokens)

Create directory structure and get necessary tools (classroom-util and yaclu):
```
mkdir pf2-2019-analysis
cd pf2-2019-analysis
git clone https://github.com/geoffryan/classroom-util.git
git clone git@bitbucket.org:igormoreno/yaclu.git
```

Edit the `get-assignment-repos.py` script, put your GitHub personal access token in there (in the line `g = Github(...)`):

```
cd yaclu
vi get-assignment-repos.py
cd ..
```

Edit the `run.py`: script, insert the correct organisation name in the line org='YOUR_ORG_NAME_HERE'

```
mkdir student-repos
```

# Requirements
For `run.py` to work there are several requirements:
* A Github organisation (for clone and pull functionality)
* The students repos must be named with the scheme :lab-[0-9]+-  (i.e. lab-01-example is a repo for lab01)


# Usage 1
To run PMD on several folders it is possible to utilise `run.py`:
```
./run.py lab01 -check -report
```
the results will be in the folder `lab01`, each student's folder will also have its results.

# Usage 2
It is also possible to run PMD without, from the root of this repository use:
```
CLASSPATH=pf2_custom_rules.jar ./pmd-bin-6.14.0/bin/run.sh pmd -no-cache -f text -d target_folder -R myrule.xml
```
this will run PMD for the target folder, change the CLASSPATH to `pf2_custom_rules_enabler.jar` to run with the enablers

#Flags
There are several flags, the standard ones are those seen in the Usage section but
there are more available:
* -clone    : clone the repos corresponding to the given lab
* -pull     : pull the repos corresponding to the given lab
* -check    : run PMD on all the repos
* -report   : create a file with the report
* -enablers : run with the enablers instead of the detectors
