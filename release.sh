#!/bin/bash
#
# Note: this uses commands from https://github.com/mbezjak/poly-devel

set -o errexit

declare -r version="$1"
release-pre "$version"
release-post "$version"

exit 0
