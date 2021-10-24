import json
import sys


def main():

	file_name = sys.argv[1]

	with open(file_name, "r") as reportFile:
		report = json.load(reportFile)
		deps = report["current"]["dependencies"]

		filtered = []

		for dependency in deps:
		    item = {
		        "group": dependency["group"],
		        "name": dependency["name"],
		        "version": dependency["version"],
		    }
		    filtered.append(item)

		tranformed = {
		    "dependencies": filtered
		}

		with open("appsweep.json", "w") as transformedFile:
		    json.dump(tranformed, transformedFile)

if __name__ == "__main__":
	main()
