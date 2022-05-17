#!/bin/bash
# This script is configured for *nix systems
# Java download code by SimerLol: https://replit.com/@SimerLol/Minecraft-Server-Template?v=1#Startup.sh

# javac compiles java classes
# java runs java classes
# mvn is used to execute Maven goals

# To check the version of any of the above three, do `[file] -version` without the brackets
# To find the path leading to java, javac, or mvn, do `command -v [file]` without the brackets

set -e
root=$PWD

# set JAVA_HOME to the folder shown by where `printenv JAVA_HOME` is located
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
# export MAVEN_HOME=/usr/share/maven -> failed attempt at changing MAVEN_HOME, doesn't do anything

# set PATH to bin of JAVA_HOME 
export PATH=$JAVA_HOME/bin:$PATH

download() {
    set -e
    echo By executing this script you agree to the JRE License
    echo and the licenses of all packages used \in this project.
    echo Press Ctrl+C \if you \do not agree to any of these licenses.
    echo Press Enter to agree.
    read -s agree_text
    echo Thank you \for agreeing, the download will now begin.
    wget -O jre.tar.gz "https://javadl.oracle.com/webapps/download/AutoDL?BundleId=242050_3d5a2bb8f8d4428bbe94aed7ec7ae784"
    tar -zxf jre.tar.gz
    rm -rf jre.tar.gz
    mv ./jre* ./jre
    echo "Download complete" 
}

require() {
    if [ ! $1 $2 ]; then
        echo $3
        echo "Running download..."
        download
    fi
}
require_file() { require -f $1 "File $1 required but not found"; }
require_dir()  { require -d $1 "Directory $1 required but not found"; }
require_env() {
  if [[ -z "${!1}" ]]; then
    echo "Environment variable $1 not set. "
    echo "Make a new secret called $1 and set it to $2"
    exit
  fi
}
require_executable() {
    require_file "$1"
    chmod +x "$1"
}

# install Java
require_dir "jre"
require_executable "jre/bin/java"

# Set PATH to present working directory (?)
PATH=$PWD/jre/bin:$PATH

echo "Exit code $?"
