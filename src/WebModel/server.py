import json
import socket
from threading import Thread
from random import randint

from flask import Flask, send_from_directory, request, render_template
from flask_socketio import SocketIO

import eventlet

eventlet.monkey_patch()


app = Flask(__name__)
socket_server = SocketIO(app)
#
# scala_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# # scala_socket.connect(('localhost', 8000))
#
# delimiter = "~"
#
# def listen_to_scala(the_socket):
#     buffer = ""
#     while True:
#         buffer += the_socket.recv(1024).decode()
#         while delimiter in buffer:
#             message = buffer[:buffer.find(delimiter)]
#             buffer = buffer[buffer.find(delimiter) + 1:]
#             get_from_scala(message)
#
# def get_from_scala(data):
#     socket_server.emit('gamestate', data, broadcast=True)
#
# def send_to_scala(data):
#     scala_socket.sendall((json.dumps(data) + delimiter).encode())
#
# #
# # Thread(target=listen_to_scala, args=(scala_socket,)).start()
#
# #sidToUsername = {}
# #health = {}
# #speed = {}
# #strength = {}
#
@socket_server.on('connect')
def got_message():
    print(request.sid + " connected")
    message = {"username": request.sid, "action": "connected"}
#    send_to_scala(message)

#
#
# @socket_server.on('disconnect')
# def disconnect():
#     print(request.sid + " closed")
#     message = {"username": request.sid, "action": "closed"}
#     send_to_scala(message)


@app.route('/')
def index():
    return send_from_directory('static', 'index.html')


@app.route('/game', methods=["POST", "GET"])
def game():
    if request.method == "POST":
        username = request.form.get('username')
    else:
        username = "guest" + str(randint(0, 100000))
    return render_template('game.html', username=username)


@app.route('/<path:filename>')
def static_files(filename):
    return send_from_directory('static', filename)

if __name__ == "__main__":
  #app.run(host= '0.0.0.0') #Not sure if this functions when connected with Scala
    socket_server.run(app, port=60000)
