const debounce = require('lodash/debounce')
const { parseSafe, log } = require('./utils')
const { Rooms, Users } = require('./model')

const handleLocationDataUser = (socket, res) => {
	const data = typeof (res) === 'object' ? res : parseSafe(res) || {}
	Users.modifyUserData(data)
}

const queryDataFromRoom = (roomId) => {
	return JSON.stringify(Rooms.getDataFromRoom(roomId))
}

const socketListener = (socket) => {
	// console.log(socket.id, "connected");

	socket.on("request_join", (room) => {
		console.log(`${socket.id} requests to join ${room}`)

		// if (!rooms[room]) {
		// 	rooms[room] = [socket.id]
		// } else {
		// 	rooms[room].push(socket.id);
		// }
		// usersMap[socket.id] = room
		// // log()

		Users.modifyUserData({
			id: socket.id,
			roomId: room
		})

		socket.join(room)
	});


	socket.on("device_data", debounce((res) => {

		// console.log('on received new location', paseSafe(res))

		handleLocationDataUser(socket, res)

		const userRoom = Users.getRoomById(socket.id)

		if (!userRoom) {
			console.error("\x1b[34m", "cannot find this room")
			return
		}
		const parsedData = queryDataFromRoom(userRoom)

		log(" 👉Sending: ", parseSafe(parsedData))

		socket.to(userRoom).emit("server_data", (parsedData));
		socket.emit("server_data", (parsedData));
	}, 100))


	socket.on("disconnect", () => {
		console.log(`${socket.id} left`);
		Users.deleteUser(socket.id)
	});
}

module.exports = { socketListener }
