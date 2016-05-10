# !/bin/bash

# Downloads and extracts NetTex

PROFILE_PATH="./profile"

if [ -f master.zip ]; then
  rm master.zip
fi

wget -q "https://github.com/rutebanken/NeTEx-XML/archive/master.zip"

echo "Removing contents in $PROFILE_PATH"
rm -rf $PROFILE_PATH/*

unzip master.zip "NeTEx-XML-master/schema/1.03/xsd/*" -d $PROFILE_PATH

rm master.zip
