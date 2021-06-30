
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

module.exports = { parseSafe }