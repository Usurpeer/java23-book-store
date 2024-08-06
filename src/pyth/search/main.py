import bertModel
import googl2

from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route("/api/pyth-1-search/<string:search>", methods=["POST"])
def search1_books(search):
    books = request.json
    b = googl2.searching(search, books)
    return jsonify(b)

@app.route("/api/pyth-2-search/<string:search>", methods=["POST"])
def search2_books(search):
    books = request.json
    b = bertModel.searching(search, books)
    return jsonify(b)

if __name__ == "__main__":
    app.run(debug=True, port=8081, host="localhost")