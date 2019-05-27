#!/usr/bin/env python3

from pathlib import Path
import sys
import subprocess
import re
import argparse

#TODO: set correct values
org='usi-pf2-2019'
pmd_pos = '/Users/stefano/Desktop/bachelor-project/JavaRules/pmd-bin-6.14.0/bin'
pos_yaclu = '/Users/stefano/Desktop/bachPF2/pf2-2019-analysis/yaclu'
root = '/Users/stefano/Desktop/bachPF2/pf2-2019-analysis/student-repos'
pos_classroom_sh = '/Users/stefano/Desktop/bachPF2/pf2-2019-analysis/classroom-util'
pos_root_bachelor = '/Users/stefano/Desktop/bachelor-project'

#commands
checkstyle_cmd = 'echo "repo,checkstyle violation" > checkstyle_violations.csv; for f in lab*; do cd "$f";echo "$f,`mvn checkstyle:check|grep WARN|wc -l`"|cut -c 8- >> ../checkstyle_violations.csv;cd ..; done; say "done";csvlook checkstyle_violations.csv'
all_reports_cmd = ''


parser = argparse.ArgumentParser(description='Use to download and run tests on git repos')
parser.add_argument('lab', help='the lab to utilise i.e. lab01')
parser.add_argument('-clone', action='store_true', help='clone the repositories for the lab')
parser.add_argument('-pull', action='store_true', help='pull the repositories for the lab')
parser.add_argument('-verify', action='store_true', help='run mvn:verify for the repositories in the lab')
parser.add_argument('-check', action='store_true', help='run mvn:check for the repositories in the lab')
parser.add_argument('-report_rules_total', action='store_true', help='create a report with the total of the violation per pmd rule, need -check beforehand')
parser.add_argument('-print', action='store_true', help='print the results of the report on the terminal (not nice), needs -report_rules_total beforehand')
parser.add_argument('-enablers', action='store_true', help='use jar for enablers')
args = parser.parse_args()


#assume path is correct
def get_all_folders(root):
    p = Path(root);
    folders = [];
    for folder in p.iterdir():
        if folder.is_dir():
            folders.append(folder.name);
    return folders;

def run_pmd(root,folders,command):
    for folder in folders:
        subprocess.run('cd '+root+'/'';'+command +' '+folder, shell=True);

def run_pmd_check(root,folders):
    subprocess.run('cd '+root+'; echo "" > violation.csv', shell=True);
    for folder in folders:
        if not args.enablers:
            jar = '/JavaRules/pmd-examples/pf2_custom_rules.jar'
            en = 'v'
        else:
            jar = '/RulesEnabler/pmd-examples/pf2_custom_rules_enabler.jar'
            en = 'enablerV'
        subprocess.run('cd ' + root + '/ ;CLASSPATH=' + pos_root_bachelor + jar +' ' + pmd_pos +'/run.sh pmd -no-cache -f csv -d ' + folder + ' -R myrule.xml -t 8 | grep -v "Test.java" | tee '+root+'/'+folder+ '/' + en + 'iolation.csv >> violation.csv', shell=True);

def run_pmd_output(root,folders,command):
    out = {};
    for folder in folders:
        p = subprocess.check_output('cd '+root+'/'+folder+';'+command, shell=True);
        p = p.decode("utf-8");
        p = p.split('\n');

        for part in p:
            if len(part) < 2:
                continue;
            rule_name = part.split(' ')[4][5:];
            if rule_name in out:
                out[rule_name] = out[rule_name] + 1;
            else:
                out[rule_name] = 1;
    return out;

if(len(sys.argv) < 2):
    print("need a lab");
    exit();

path = sys.argv[1];

if(not re.match("lab[0-9]+",path)):
    print("invalid lab ",path,' should be like lab[0-1]+');
    exit();

#convert to absolute path
lab = path.replace('lab','lab-');
path = root + '/' + path;

if(not Path(path).exists()):
    print('creating folder ' + lab);
    subprocess.run('mkdir ' + path, shell=True);

if(not Path(path).is_dir()):
    print('need a folder name as first argument');
    exit();

subfolders = get_all_folders(path);


if(args.clone):
    subfolders = get_all_folders(path);
     #python3 ../../yaclu/get-assignment-repos.py usi-pf2-2019 lab-01 > meta.txt
    command = 'python3 ' + pos_yaclu + '/get-assignment-repos.py ' + org + ' ' + lab + '> ' + path + '/meta.txt';
    print(command);
    subprocess.run(command, shell=True);
    command = 'cd ' + path + ' ; ' + pos_classroom_sh + '/classroom.sh collect ' +path + '/meta.txt ' + lab;
    print(command);
    subprocess.run(command, shell=True)

if(args.pull):
    command = 'cd ' + path + ' ; ' + pos_classroom_sh + '/classroom.sh collect ' +path + '/meta.txt ' + lab;
    print(command);
    subprocess.run(command, shell=True)

if(args.verify):
    run_pmd(path,subfolders,'mvn verify');

if('-checkstyle' in sys.argv):
    command = 'cd ' + path + '; ' + checkstyle_cmd;
    subprocess.run(command, shell=True);

if(args.check):
    run_pmd_check(path,subfolders);

if(args.print):
    run_pmd(path,subfolders,'echo "\n\n"; pwd;cat pmd_failures');

if(args.report_rules_total):
    F = open(path+'/violation.csv','r')
    #already checked student
    done = False

    violationsTotal = {};
    violationsStudent = {};
    lastRule = ''

    for line in F:

        pieces = line.split(',')
        if(len(pieces) < 7):
            continue
        rule_name = pieces[7]
        if 'Test.java' in line:
            continue
        if rule_name == '"Rule"\n':
            continue
        #total count
        if rule_name in violationsTotal:
            violationsTotal[rule_name] = violationsTotal[rule_name] + 1;
        else:
            violationsTotal[rule_name] = 1;

        student_name = re.search('lab-\d+-((\d|[a-z]|[A-Z])+)',  pieces[2]).group(1)

        if rule_name in violationsStudent:
            if not student_name in violationsStudent[rule_name]:
                violationsStudent[rule_name].append(student_name);
        else:
            violationsStudent[rule_name] = [student_name];




    #out = run_pmd_output(path,subfolders,'cat pmd_failures');
    csv = open(path+'/report_rules_total.csv', "w");
    csv.write('Rule, Number\n');
    for key in violationsTotal:
        csv.write(key.strip()+','+str(violationsTotal[key])+'\n');
    csv.close();

    csv = open(path+'/report_rules_total_nr_students.csv', "w");
    csv.write('Rule, Number\n');
    for key in violationsStudent:
        csv.write(key.strip()+','+str(len(violationsStudent[key]))+'\n');
    csv.close();
    print()
