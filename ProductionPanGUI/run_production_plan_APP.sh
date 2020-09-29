#!/bin/bash
set -x

readonly work_dir="$(dirname $(readlink --canonicalize-existing "${0}"))"
readonly dist_dir="${work_dir}/dist"
readonly jar_file="ProductionPanGUI.jar"
readonly report_save_directory="${work_dir}/output"

cp --recursive --force "${work_dir}"/run/* dist/

(
	cd "${work_dir}/dist"
	java -jar "${dist_dir}/${jar_file}" "${report_save_directory}"
)

exit 0
