from flask import Flask, request, jsonify, send_from_directory
import csv
import os

app = Flask(__name__)

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CSV_FILE = os.path.join(BASE_DIR, "data", "properties.csv")


def load_properties():
    properties = []

    with open(CSV_FILE, "r", encoding="utf-8") as file:
        reader = csv.DictReader(file)

        for row in reader:
            properties.append(row)

    return properties


@app.route("/")
def home():
    return send_from_directory(".", "index.html")

@app.route("/search")
def search():
    location = request.args.get("location", "Any")
    max_rent = request.args.get("max_rent", "999999999")
    bedrooms = request.args.get("bedrooms", "Any")
    water = request.args.get("water", "Any")
    parking = request.args.get("parking", "Any")
    sort_option = request.args.get("sort", "1")

    try:
        max_rent = int(max_rent)
    except ValueError:
        return jsonify({"error": "Maximum rent must be a number."}), 400

    properties = load_properties()
    results = []

    for property in properties:

        if location.lower() == "any" or location.lower() in property["location"].lower():
            matches_location = True
        else:
            matches_location = False

        matches_rent = int(property["rent"]) <= max_rent

        if bedrooms.lower() == "any":
            matches_bedrooms = True
        else:
            matches_bedrooms = int(property["bedrooms"]) == int(bedrooms)

        matches_water = (
            water.lower() == "any"
            or property["water"].lower() == water.lower()
        )

        matches_parking = (
            parking.lower() == "any"
            or property["parking"].lower() == parking.lower()
        )

        if (
            matches_location
            and matches_rent
            and matches_bedrooms
            and matches_water
            and matches_parking
        ):
            results.append(property)

    if sort_option == "1":
        results.sort(key=lambda property: int(property["rent"]))

    elif sort_option == "2":
        results.sort(
            key=lambda property: int(property["rent"]),
            reverse=True
        )

    elif sort_option == "3":
        results.sort(
            key=lambda property:
            float(property["distance_to_town"].replace(" km", ""))
        )

    return jsonify(results)


@app.route("/images/<filename>")
def images(filename):
    image_folder = os.path.join(BASE_DIR, "data", "images")
    return send_from_directory(image_folder, filename)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)