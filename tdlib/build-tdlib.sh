#!/bin/bash

git clone --recursive https://github.com/yukari-sys/tdlib builder
cd builder
OUTPUT_DIR=../bin ./build.sh
