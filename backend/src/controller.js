const debounce = require('lodash/debounce')
const { parseSafe } = require('./utils')


const usersMap = {};
const rooms = {};

const log = () => {
	console.log("@@@ LOG: ", JSON.stringify({
		usersMap,
		rooms,
	}, null, 3));
}


const socketListener = (socket) => {
	// console.log(socket.id, "connected");

	socket.on("request_join", (room) => {
		console.log(`${socket.id} requests to join ${room}`)

		if (!rooms[room]) {
			rooms[room] = [socket.id]
		} else {
			rooms[room].push(socket.id);
		}
		usersMap[socket.id] = room
		// log()

		socket.join(room)
	});


	const handleDataChange = debounce((res) => {
		const parsedData = JSON.stringify(parseSafe(res))
		console.log('on have new stroke', parseSafe(res))
		socket.to(usersMap[socket.id]).emit("server_data", (parsedData));
		socket.emit("server_data", (parsedData));
	}, 10)

	socket.on("device_data", handleDataChange)

	socket.on("disconnect", (socket) => {
		console.log("User left");
	});
}

module.exports = { socketListener }
