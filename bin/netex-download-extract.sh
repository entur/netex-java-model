#!/bin/bash

# Download and extract NetTex
: ${ZIP_PATH_TO_EXTRACT:="${NETEX_REPO}-${GIT_BRANCH}/xsd/*"}

validate () {
    NAME=$1
    VALUE=$2

    if [ -z ${VALUE} ];
    then
        echo "$NAME not set"
        exit 1
    fi
}

validate "ZIP_PATH_TO_EXTRACT" ${ZIP_PATH_TO_EXTRACT}
validate "GITHUB_URL" ${GITHUB_URL}
validate "DESTINATION_PATH" ${DESTINATION_PATH}

ZIP_FILE=downloaded.zip

echo "NETEX repo github URL: $GITHUB_URL"

echo "Removing any existing contents in $DESTINATION_PATH"
rm -rf ${DESTINATION_PATH}/*
mkdir -p ${DESTINATION_PATH}

if [ -f ${ZIP_FILE} ]; then
  echo "Removing existing file $ZIP_FILE"
  rm ${ZIP_FILE}
fi

WGET_URL="${GITHUB_URL}"
echo "About to download from $WGET_URL"
wget -q ${WGET_URL} -O ${ZIP_FILE}

if [ -f ${ZIP_FILE} ]; then
    echo "Done"
    {
    echo "Create ${DESTINATION_PATH}" &&
    mkdir -p ${DESTINATION_PATH} &&

    echo "Unzip path ${ZIP_PATH_TO_EXTRACT} from zip file ${ZIP_FILE} to ${DESTINATION_PATH}" &&
    unzip -q ${ZIP_FILE} ${ZIP_PATH_TO_EXTRACT} -d ${DESTINATION_PATH} &&

    echo "Remove zipfile ${ZIP_FILE}" &&
    rm ${ZIP_FILE} &&

    REMOVE_FOLDER=${ZIP_PATH_TO_EXTRACT} &&

    echo "Remove intermediate folder ${REMOVE_FOLDER}" &&

    mv ${DESTINATION_PATH}/${REMOVE_FOLDER} ${DESTINATION_PATH} &&
    rm -rf "${DESTINATION_PATH}/${REMOVE_FOLDER}" &&

    echo "XSD extracted to $DESTINATION_PATH"
    } ||
    {
        (>&2 echo "Error extracting zip file $ZIP_FILE from $WGET_URL. See my previous output for details")
        exit 1
    }
else
    (>&2 echo "Error downloading zip from $WGET_URL")
    exit 1
fi

