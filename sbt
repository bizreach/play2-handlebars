#!/bin/bash

BASE_DIR=`dirname $0`

${BASE_DIR}/.sbt/bin/sbt "$@"
