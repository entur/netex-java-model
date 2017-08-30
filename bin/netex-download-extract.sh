# !/bin/bash

# Downloads and extracts NetTex

PROFILE_PATH="./profile"

BRANCH="1.07"
ZIPFILE="$BRANCH.zip"

if [ -f $ZIPFILE ]; then
  rm $ZIPFILE
fi

wget -q "https://github.com/rutebanken/NeTEx-XML/archive/$ZIPFILE"

echo "Removing contents in $PROFILE_PATH"
rm -rf $PROFILE_PATH/*

unzip $ZIPFILE "NeTEx-XML-$BRANCH/schema/xsd/*" -d $PROFILE_PATH

rm $ZIPFILE
