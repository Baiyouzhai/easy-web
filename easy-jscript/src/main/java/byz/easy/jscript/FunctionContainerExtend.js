// 必须先引入JavaImport.js
// 必须先引入OverloadContainer.js
// package -> name -> version -> argNames -> function
const FunctionContainer = function (pathJoin) {
	var pathJoin = pathJoin;
	var root = new HashMap();
	var getPackagePath = function (path) {
		return Array.isArray(path) ? path.join(this.pathJoin) : path;
	}
	return {
		root: root,
		getPackagePath: getPackagePath,
		put: function (package, name, version, argNames, functionBody) {
			let path = getPackagePath(package);
			if (!root.get(path))
				root.put(path, new HashMap());
			let temp = root.get(path);
			if (!temp.get(name))
				temp.put(name, new HashMap());
			temp = temp.get(name);
			if (!temp.get(version))
				temp.put(version, new OverloadContainer());
			temp = temp.get(version);
			return temp.put(argNames, functionBody);
		},
		get: function (package, name, version, argNames) {
			let path = getPackagePath(package);
			let temp = root.get(path);
			if (temp)
				if ((temp = temp.get(name)))
					if ((temp = temp.get(version)))
						if ((temp = temp.get(argNames)))
							return temp;
			return null;
		},
		remove: function (package, name, version, argNames) {
			let path = getPackagePath(package);
			let temp = root.get(path);
			if (temp)
				if ((temp = temp.get(name)))
					if ((temp = temp.get(version)))
						if ((temp = temp.get(argNames)))
							return temp.remove(argNames);
			return null;
		}
	}
}