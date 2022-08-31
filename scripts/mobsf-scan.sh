#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

readonly target_apk_path="$1"

readonly api_key="dotanuki"
readonly target_apk=$(echo $target_apk_path | sed "s/release-apk\//g")
readonly mobsf_server="http://localhost:8000/api/v1"
readonly auth="Authorization:$api_key"

readonly reports_dir="mobsf-reports"
readonly scorecard_file="$reports_dir/mobsf-security-scorecard.json"
readonly report_file="$reports_dir/mobsf-security-report.pdf"

echo

rm -rf "$reports_dir"
mkdir "$reports_dir"

echo "→ Uploading '$target_apk' to MobSF server"
hash=$(curl -sS -H "$auth" -F "file=@$dir/$target_apk_path" "$mobsf_server/upload" | jq '.hash' | sed 's/\"//g')

echo "→ Starting static analysis. This may take a while ..."
curl -sS -X POST -H "$auth" --url "$mobsf_server/scan" --data "scan_type=apk&file_name=$target_apk&hash=$hash" >/dev/null

echo "→ Downloading security report (PDF)"
rm -rf "$report_file"
curl -sS -X POST -H "$auth" --url "$mobsf_server/download_pdf" --data "hash=$hash" >>"$report_file"

rm -rf "$scorecard_file"
echo "→ Downloading security scorecard as(json)"
curl -sS -X POST -H "$auth" --url "$mobsf_server/scorecard" --data "hash=$hash" | jq >>"$scorecard_file"

echo "→ Evaluating scorecard ..."
echo

high_risk_issues=$(cat "$scorecard_file" | jq -r '.high | length')
echo "Found $high_risk_issues high risk issues"

score=$(cat "$scorecard_file" | jq '.security_score')

if [[ "$score" > "95" ]]; then 
	echo "Security score ("$score") meets the quality standards"
else
	echo "Security score ("$score") does not meet the quality standards"
fi

echo
echo "🔥 Done"
echo
