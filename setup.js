//set up
const fs = require('fs')
const { exec } = require("child_process");
const path = process.argv[1];
const type = process.argv[2];

const setupAndroid = () => {
	const androidConfigPath = path.slice(0, path.lastIndexOf("/") + 1) + "android/app/src/main/res/raw/config.properties";

	console.log('androidConfigPath', androidConfigPath)

	exec("ipconfig getifaddr en0", (error, ip, stderr) => {
		if (error) {
			console.log(`error: ${error.message}`);
			return;
		}
		if (stderr) {
			console.log(`stderr: ${stderr}`);
			return;
		}

		const conetent = `socket_url=http://${ip.slice(0, -1)}:3000`		//erase lineFeed

		fs.writeFile(androidConfigPath, conetent, "utf8", (err) => {
			if (err) {
				console.error(err)
			}
			console.log('config url for android sucessfully')
		})
	});
}

const setupIOS = () => {
	const iosConfigPath = path.slice(0, path.lastIndexOf("/") + 1) + "ios/WhereAreU/Utilities/Configs.swift";

	console.log('iosConfigPath', iosConfigPath)

	exec("ipconfig getifaddr en0", (error, ip, stderr) => {
		if (error) {
			console.log(`error: ${error.message}`);
			return;
		}
		if (stderr) {
			console.log(`stderr: ${stderr}`);
			return;
		}

		const conetent = `public let SOCKET_URI = "http://${ip.slice(0, -1)}:3000";`		//erase lineFeed

		fs.writeFile(iosConfigPath, conetent, "utf8", (err) => {
			if (err) {
				console.error(err)
			}
			console.log('config url for ios sucessfully')
		})
	});
}

const gitCommit = () => {
	const message = process.argv.slice(3).join(" ")
	exec(`git add . && git commit -m \"${message}\"`, (error, ip, stderr) => {
		if (error) {
			console.log(`error: ${error.message}`);
			return;
		}
		if (stderr) {
			console.log(`stderr: ${stderr}`);
			return;
		}
	});

}

console.log('type: ', type)
switch (type) {
	case 'android':
		setupAndroid();
		break;
	case 'ios':
		setupIOS();
		break;
	case 'commit':
		gitCommit()
		break;
}