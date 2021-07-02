
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

module.exports = { parseSafe, extractNotNullKey, notNullUnion }