const debounce = require('lodash/debounce')
const { parseSafe } = require('./utils')
const { Rooms, Users } = require('./model')


const usersMap = {};
const rooms = {};

const log = () => {
	console.log("@@@ LOG: ", JSON.stringify({
		usersMap,
		rooms,
	}, null, 3));
}

const handleLocationDataUser = (socket, res) => {
	const data = typeof (res) === 'object' ? res : parseSafe(res) || {}
	Users.modifyUserData({
		id: socket.id,
		...(data?.latitude && data?.longitude ? {
			latitude: data.latitude,
			longitude: data.longitude
		} : {}),
	})
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

		console.log('on received new location', parseSafe(res))

		handleLocationDataUser(socket, res)

		const userRoom = Users.getRoomById(socket.id)

		if (!userRoom) {
			console.error("cannot find this room")
			return
		}

		const parsedData = queryDataFromRoom(userRoom)

		console.log(`sending to Room ${userRoom}: ${JSON.stringify(parseSafe(parsedData), null, 2)}`)

		socket.to(userRoom).emit("server_data", (parsedData));
		socket.emit("server_data", (parsedData));
	}, 100))


	socket.on("disconnect", (socket) => {
		console.log("User left");
	});
}

module.exports = { socketListener }
