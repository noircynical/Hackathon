from flask import Flask
from flask import request, jsonify
app = Flask(__name__)
app.config['DEBUG'] = True

# Note: We don't need to call run() since our application is embedded within
# the App Engine WSGI application server.

dataBase = []
date = None

@app.route('/')
def hello():
    """Return a friendly HTTP greeting."""
    return 'Hello World!!!'

@app.route('/add', methods=['POST'])
def add():
    dataset = {}
    dataset['num'] = request.form['num']
    dataset['imgurl'] = request.form['imgurl']
    dataset['name'] = request.form['name']
    dataset['price'] = request.form['price']
    dataBase.append(dataset)
    return jsonify(dataset)

@app.route('/sendDate', methods=['POST'])
def getdate():
    date = request.get_json('date')
    return jsonify(date)

@app.route('/printData')
def printData():

    return jsonify(dataBase)

# @app.route('/delete', methods=['GET'])
# def deletedata():
#     number = request.args['number']
#     numberint = int(number)
#     dataBase.remove(numberint)
#     return "index "+ number+" is removed"

@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, nothing at this URL.', 404