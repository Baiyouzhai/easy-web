// 必须先引入JavaImport.js
// 必须先引入FunctionContainer.js
var Runner = function (path, args) {
	return FunctionContainer.get(path).apply(null, args);
}