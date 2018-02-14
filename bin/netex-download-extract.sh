#!/bin/bash

# Downloads and extracts NetTex

# Note. These are only default values. Change properties in pom file for changing config.
: ${NETEX_REPO:="NeTEx"}
: ${GITHUB_URL:="https://github.com/entur/$NETEX_REPO"}
: ${GIT_BRANCH:="master"}
: ${GIT_BRANCH:="master"}
: ${PROFILE_PATH:="./profile"}

echo "NETEX full github URL: $GITHUB_URL"

echo "Removing any existing contents in $PROFILE_PATH"
rm -rf ${PROFILE_PATH}/*

ZIPFILE="${GIT_BRANCH}.zip"

if [ -f ${ZIPFILE} ]; then
  echo "Removing existin file $ZIPFILE"
  rm ${ZIPFILE}
fi

WGET_URL="${GITHUB_URL}/archive/${ZIPFILE}"
wget -q ${WGET_URL}

if [ -f ${ZIPFILE} ]; then
    echo "Unzip to ${PROFILE_PATH}"
    unzip ${ZIPFILE} "${NETEX_REPO}-${GIT_BRANCH}/xsd/*" -d ${PROFILE_PATH}
    rm ${ZIPFILE}
    REMOVE_FOLDER="${NETEX_REPO}-${GIT_BRANCH}"
    echo "Remove intermediate folder ${PROFILE_PATH}/${REMOVE_FOLDER}"
    mv "${PROFILE_PATH}/${REMOVE_FOLDER}/xsd" ${PROFILE_PATH}
    rm -rf "${PROFILE_PATH}/${REMOVE_FOLDER}"
    echo "Profile extracted to $PROFILE_PATH"
else
    (>&2 echo "Error downloading zip file $ZIPFILE from $WGET_URL")
    exit 1
fi

