#!/bin/bash
set -x

readonly work_dir="$(dirname $(readlink --canonicalize-existing "${0}"))"
readonly dist_dir="${work_dir}/dist"
readonly jar_file="ProductionPanGUI.jar"
cp --force "${HOME}/inputs/FAB Load by WC Leo.xls" dist/
cp --force "${HOME}/inputs/Age  by WC.xls" dist/
cp --force "${HOME}/inputs/part-machine.csv" dist/

(
	cd "${work_dir}/dist"
	java -jar "${dist_dir}/${jar_file}"
)

exit 0