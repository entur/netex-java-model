#!/bin/bash

# Downloads and extracts NetTex

# Note. These are only default values. Change properties in pom file for overrides
: ${NETEX_REPO:="NeTEx"}
: ${GITHUB_URL:="https://github.com/entur/$NETEX_REPO/archive/master.zip"}
: ${GIT_BRANCH:="master"}
: ${DESTINATION_PATH:="./profile"}
: ${ZIP_PATH_TO_EXTRACT:="${NETEX_REPO}-${GIT_BRANCH}/xsd/*"}

ZIPFILE=downloaded.zip

echo "NETEX repo github URL: $GITHUB_URL Github branch: $GIT_BRANCH"


echo "Removing any existing contents in $DESTINATION_PATH"
rm -rf ${DESTINATION_PATH}/*
mkdir -p ${DESTINATION_PATH}


if [ -f ${ZIPFILE} ]; then
  echo "Removing existing file $ZIPFILE"
  rm ${ZIPFILE}
fi

WGET_URL="${GITHUB_URL}"
echo "About to download from $WGET_URL"
wget -q ${WGET_URL} -O ${ZIPFILE}

if [ -f ${ZIPFILE} ]; then
    echo "Done"
    {
    echo "Create ${DESTINATION_PATH}" &&
    mkdir -p ${DESTINATION_PATH} &&

    echo "Unzip path ${ZIP_PATH_TO_EXTRACT} from zip file ${ZIPFILE} to ${DESTINATION_PATH}" &&
    unzip -q ${ZIPFILE} ${ZIP_PATH_TO_EXTRACT} -d ${DESTINATION_PATH} &&

    echo "Remove zipfile ${ZIPFILE}" &&
    rm ${ZIPFILE} &&

    REMOVE_FOLDER=${ZIP_PATH_TO_EXTRACT} &&

    echo "Remove intermediate folder ${REMOVE_FOLDER}" &&

    mv ${DESTINATION_PATH}/${REMOVE_FOLDER} ${DESTINATION_PATH} &&
    rm -rf "${DESTINATION_PATH}/${REMOVE_FOLDER}" &&

    echo "XSD extracted to $DESTINATION_PATH"
    } ||
    {
        (>&2 echo "Error extracting zip file $ZIPFILE from $WGET_URL. See my previous output for details")
        exit 1
    }
else
    (>&2 echo "Error downloading zip from $WGET_URL")
    exit 1
fi

