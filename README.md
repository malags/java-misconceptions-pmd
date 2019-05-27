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

Edit the `get-assignment-repos.py` script to put your GitHub personal access token in there (in the line `g = Github(...)`):

```
cd yaclu
vi get-assignment-repos.py
cd ..
```

Create directory to hold cloned student repositories:

```
mkdir student-repos
```
