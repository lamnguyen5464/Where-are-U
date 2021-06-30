const socket = require("socket.io");
const express = require("express");
const { socketListener } = require('./controller')

const PORT = process.env.PORT || 3000;

//set up server
const server = express().listen(PORT, () => {
     console.log(`Listening on port ${PORT}...`);
});


//set up socketIO
const io = socket(server);
io.on("connect", socketListener);
