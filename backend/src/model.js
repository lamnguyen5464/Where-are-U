const { notNullUnion } = require('./utils');

const Rooms = {
	data: {},
	checkExistedRoom: (roomId) => {
		return (Rooms.data[roomId]);
	},
	_createNewRoom: (roomId) => {
		Rooms.data[roomId] = {}
	},
	modifyUserToRoom: (userData) => {
		const _roomId = userData.roomId
		if (!Rooms.checkExistedRoom(_roomId)) {
			Rooms._createNewRoom(_roomId)
		}

		Rooms.data[_roomId][userData.id] = notNullUnion([
			Rooms.data[_roomId][userData.id],
			userData
		])
	},
	removeUserFromRoom: (userId, roomId) => {
		if (!userId || !roomId) {
			return
		}
		delete Rooms.data[roomId]?.[userId]
	},
	removeFromPreviousRoom: (userData, previousRoomId) => {
		if (userData.roomId !== previousRoomId)
			Rooms.removeUserFromRoom(userData.id, previousRoomId)
	},
	getDataFromRoom: (roomId) => {
		const _res = [];
		const _listUsers = Rooms.data[roomId] || []
		Object.keys(_listUsers).forEach(userId => {
			_res.push(_listUsers[userId])
		})

		return _res;
	}
}

const Users = {
	data: {},
	modifyUserData: (data) => {
		const { id, latitude, longitude, roomId } = data
		Rooms.removeFromPreviousRoom(data, Users.data[id]?.roomId)
		Users.data[id] = notNullUnion([Users.data[id], data])
		Rooms.modifyUserToRoom(Users.data[id]);
	},
	getUser: (userId) => {
		return Users.data[userId] || {}
	},
	getRoomById: (userId) => {
		return Users.data[userId]?.roomId
	},
	deleteUser: (userId) => {
		const user = Users.getUser(userId)
		Rooms.removeUserFromRoom(user.id, user.roomId)
		delete Users.data[userId]
	}

}

// Users.modifyUserData({ id: "111", latitude: 1, longitude: 1, roomId: "1" })
// Users.modifyUserData({ id: "222", latitude: 2, longitude: 2, roomId: "1" })

// console.log(Rooms.getDataFromRoom("1"))

module.exports = { Rooms, Users }