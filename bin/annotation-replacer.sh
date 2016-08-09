# !/bin/bash

# Replaces annotations in NetEX xmls
# See https://java.net/jira/browse/JAXB-420

if ! type xmlstarlet > /dev/null;
    then echo "you need xmlstarlet for this to run";
    exit 1;
fi

BRANCH="1.04beta"

XSD_FOLDER="./profile/NeTEx-XML-$BRANCH/schema/xsd"

find $XSD_FOLDER -name "*.xsd" -exec xmlstarlet  ed --inplace  -d "//xsd:annotation" {} \;
