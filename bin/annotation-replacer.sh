# !/bin/bash

# Replaces annotations in NetEX xmls
# See https://java.net/jira/browse/JAXB-420

if ! type xmlstarlet > /dev/null;
    then echo "you need xmlstarlet for this to run";
    exit 1;
fi

XSD_FOLDER=../profile/NeTEx-XML-master/schema/1.03/xsd

find $XSD_FOLDER -name "*.xsd" -exec xmlstarlet  ed --inplace  -d "//xsd:annotation" {} \;
