#!/bin/bash
set -x

readonly work_dir="$(dirname $(readlink --canonicalize-existing "${0}"))"
readonly dist_dir="${work_dir}/dist"
readonly jar_file="ProductionPanGUI.jar"

cp --force "${work_dir}"/run/* dist/

(
	cd "${work_dir}/dist"
	java -jar "${dist_dir}/${jar_file}"
)

exit 0
