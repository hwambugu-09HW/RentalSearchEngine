import csv
import sys

file_path = "../data/properties.csv"

properties = []

with open(file_path, "r") as file:
    reader = csv.DictReader(file)

    for row in reader:
        properties.append(row)


def search_properties(location, max_rent, bedrooms, water, parking):

    results = []

    for property in properties:

        if location.lower() == "any":

            matches_location = True

        else:

            matches_location = (
                location.lower()
                in property["location"].lower()
            )

        matches_rent = (
            int(property["rent"])
            <= max_rent
        )

        if bedrooms.lower() == "any":

            matches_bedrooms = True

        else:

            matches_bedrooms = (
                int(property["bedrooms"])
                == bedrooms
            )

        matches_water = (
            water.lower() == "any"
            or property["water"].lower()
            == water.lower()
        )

        matches_parking = (
            parking.lower() == "any"
            or property["parking"].lower()
            == parking.lower()
        )

        if (
            matches_location
            and matches_rent
            and matches_bedrooms
            and matches_water
            and matches_parking
        ):

            results.append(property)

    return results


# ==========================================
# CHECK SEARCH INFORMATION
# ==========================================

if len(sys.argv) < 7:

    print(
        "ERROR: Missing search information."
    )

    sys.exit(1)


# ==========================================
# GET SEARCH VALUES
# ==========================================

location = sys.argv[1]

max_rent = int(sys.argv[2])

bedrooms = sys.argv[3]

if bedrooms.lower() != "any":
    bedrooms = int(bedrooms)

water = sys.argv[4]

parking = sys.argv[5]

sort_option = sys.argv[6]


# ==========================================
# SEARCH
# ==========================================

results = search_properties(
    location,
    max_rent,
    bedrooms,
    water,
    parking
)


# ==========================================
# SORT RESULTS
# ==========================================

if sort_option == "1":

    # Cheapest first

    results.sort(
        key=lambda property:
        int(property["rent"])
    )


elif sort_option == "2":

    # Most expensive first

    results.sort(
        key=lambda property:
        int(property["rent"]),
        reverse=True
    )


elif sort_option == "3":

    # Closest to town

    results.sort(
        key=lambda property:
        float(
            property["distance_to_town"]
            .replace(" km", "")
        )
    )


# ==========================================
# SEND RESULTS TO JAVA
# ==========================================

for property in results:

    print(
        f'{property["id"]}|'
        f'{property["location"]}|'
        f'{property["estate"]}|'
        f'{property["type"]}|'
        f'{property["rent"]}|'
        f'{property["bedrooms"]}|'
        f'{property["bathrooms"]}|'
        f'{property["water"]}|'
        f'{property["parking"]}|'
        f'{property["security"]}|'
        f'{property["furnished"]}|'
        f'{property["distance_to_town"]}|'
        f'{property["phone"]}|'
        f'{property["description"]}|'
	f'{property["image"]}'
    )