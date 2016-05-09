# !/bin/bash

# Downloads and extracts NetTex

if [ -f master.zip ]; then
  rm master.zip
fi

wget "https://github.com/rutebanken/NeTEx-XML/archive/master.zip"

rm -rf profile/*

unzip -j master.zip "NeTEx-XML-master/schema/1.03/xsd/*" -d profile

rm master.zip
