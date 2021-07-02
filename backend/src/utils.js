
const parseSafe = (data) => {
	let result = null;
	if (data) {
		if (typeof data === 'object') {
			return data;
		}
		try {
			result = JSON.parse(data);
		} catch (error) {
			console.logError(`[ParseSafe] data ${data}`);
		}
	}
	return result;
}

const extractNotNullKey = (obj) => {
	const res = {}
	Object.keys(obj || {}).forEach(key => {
		if (obj[key]) {
			res[key] = obj[key]
		}
	})
	return res;
}

const notNullUnion = (arrayItem) => {
	let res = {}
	arrayItem?.forEach?.(item => {
		res = {
			...res,
			...extractNotNullKey(item)
		}
	})
	return res;
}

const log = (header, content) => {
	const textColor = TEXT_COLORS[++cnt % TEXT_COLORS.length]
	console.log(textColor, `>>>>> ${header} <<<<<`)
	console.log(textColor, JSON.stringify(content, null, 3))
	console.log(textColor, "-------------------------\n\n")
}

let cnt = 0;
const TEXT_COLORS = [
	"\x1b[32m",
	"\x1b[33m",
	"\x1b[34m",
	"\x1b[35m",
	"\x1b[36m",
	"\x1b[37m",
]

module.exports = { parseSafe, extractNotNullKey, notNullUnion, log }