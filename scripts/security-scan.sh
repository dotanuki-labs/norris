
#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly api_key="$1"
readonly target_apk_path="$2"

echo

if [[ -z "$target_apk_path" ]]; then
	echo "Path to target APK not provided. Aborting"
	exit 1
fi

if [[ -z "$api_key" ]]; then
	echo "mobSF api key not provided. Aborting"
	exit 1
fi

readonly mobsf_server="https://mobsf-dotanuki.koyeb.app/api/v1"
readonly auth="Authorization:$api_key"
readonly target_apk=$(basename "$target_apk_path")
readonly reports_dir="mobsf-reports"
readonly scorecard_file="$reports_dir/mobsf-security-scorecard.json"
readonly report_file="$reports_dir/mobsf-security-report.pdf"

rm -rf "$reports_dir"
mkdir "$reports_dir"

curl_flags="-sS --fail -H "$auth" --retry 3 --retry-max-time 180"

echo "→ Uploading '$target_apk' to MobSF server"
hash=$(curl $curl_flags -F "file=@$target_apk_path" "$mobsf_server/upload" | jq '.hash' | sed 's/\"//g')

echo "→ Starting static analysis. This may take a while ..."
curl $curl_flags -X POST --url "$mobsf_server/scan" --data "scan_type=apk&file_name=$target_apk&hash=$hash" >/dev/null

echo "→ Downloading security report (PDF)"
rm -rf "$report_file"
curl $curl_flags -X POST --url "$mobsf_server/download_pdf" --data "hash=$hash" >>"$report_file"

rm -rf "$scorecard_file"
echo "→ Downloading security scorecard (json)"
curl $curl_flags -X POST --url "$mobsf_server/scorecard" --data "hash=$hash" | jq >>"$scorecard_file"

echo "→ Evaluating scorecard ..."
echo

high_risk_issues=$(cat "$scorecard_file" | jq -r '.high | length')
echo "Found $high_risk_issues high risk issues"

echo
echo "🔥 Done"
echo
