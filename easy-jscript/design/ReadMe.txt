Jscript(interface)		是javascript脚本的抽象类, codeBlock(代码块) 可以想象成<javascript></javascript>标签中的内容, 可以是一段, 也可以是全部
	例如: 
		// 定义变量
		Jscript1 -> codeBlock = "var a = 1;";
		// 接Jscript1的, 重新赋值
		Jscript2 -> codeBlock = "a = 2;";
		// 定义函数
		Jscript3 -> codeBlock = "function test(){
			return a;
		}";
		// 多内容
		Jscript4 -> codeBlock = "
			var test1 = 11;
			var test2 = 12;
			var test3 = 14;
		";
		Jscript5 -> codeBlock = "var test4 = function (name) {
			return 'Hello World! ' + name;
		}";
	等同于:
		<javascript>
			var a = 1;
			a = 2;
			function test(){
				return a;
			}
			var test1 = 11;
			var test2 = 12;
			var test3 = 14;
			var test4 = function (name) {
				return 'Hello World! ' + name;
			}
		</javascript>

JscriptEngine(interface)		是Jscript的运行容器, 可以想象成<javascript></javascript>标签
	基础方法
		execute(String) 可以执行多数功能, 直接运行String拼写内容
			例如:
				定义变量 execute("var a = 11;"); // return null;
							  execute("a = 11;"); // return 11;
							  execute("var a = {name:'张三'};"); // return null;
							  execute("a = {name:'张三'};"); // return Object(javascript对象需要转换);
				定义函数 execute("var test4 = function (name) {return 'Hello World! ' + name;}"); // return null;
							  execute("test4 = function (name) {return 'Hello World! ' + name;}"); // return Object(javascript函数需要转换);
							  execute("function test4(name) {return 'Hello World! ' + name;}"); // return Object(javascript函数需要转换);
				获取变量 execute("var a = 11;"); execute("a"); // return 11;
							  execute("var a = {name:'张三'};"); execute("a"); // return Object(javascript对象需要转换);
				获取函数 execute("test4"); // return Object; // javascript函数需要转换
				调用函数 execute("test4('张三')"); // return "Hello World! 张三";
							  String name = "李四"; execute("test4('"+name+"')"); // return "Hello World! 李四";
		getNames() 获取全部变量名(函数名也是变量名), 由于javascript是弱类型, 同一变量名容易被覆盖, 可通过此方法查看已有变量名
	javascript, 可通过Jscript构建
		定义变量, 例如 codeBlock = "var a = 11";
		定义函数, 例如 codeBlock = "var test4 = function (name) {return 'Hello World! ' + name;}";
		获取变量, 例如 codeBlock = "a";
		获取函数, 例如 codeBlock = "test4";
		调用函数, 例如 codeBlock = "test4('张三');";
	java
		定义变量, 通过 put(String, Object) 方法, 可以将java内容注入到javascript中, 例如 put("name", "张三"); 等同于javascript中 name = '张三';
		获取变量, 通过 get(String) 方法获取, 基础数据类型会自动转java类型, 其它类型得自己转
		获取函数, 通过 get(String) 方法获取, 例如 get("test4") 由于返回的是javascript函数, 所以得自己处理
		调用函数, 通过 run(String, Object[]) 方法调用, 例如 run("test4", "张三") 等同于javascript中 test4('张三');
	临时引擎
		通过 getTempleEngine(boolean) true获取带有初始数据的临时引擎, false不带有
	注意
		
		


JscriptInit(interface)	继承自Jscript, 是给 JscriptEngine 定义初始化使用的接口

JscriptRun(interface)	是
JscriptFunction(interface)	是
	

JscriptEngine 是JS脚本运行的基础, 一个引擎当中包含众多变量和函数(函数名也是一个变量)

JscriptEngineWapper 继承 JscriptEngine 并封装部分内容, 严格控制传入 JscriptEngine 的数据

JscriptEngineLoader 对 JscriptEngine 进行加载管理
	1.执行顺序控制, 比如: 定义"var temp = 1;", 而这时有脚本A -> "temp=2;", B -> "temp=3;", 此时脚本的运行顺序就非常重要, 需要有额外的一个属性说明执行顺序
		如果执行顺序调整, 则重新加载全部
	2.别名代码块, 可以给 JscriptInit 定义别名来调用。比如: "var temp = null;", 脚本A -> "temp = 14;" 取别名: "test", 如果想重置可通过"test"别名调用
	3.

JscriptEngineManage 对 JscriptEngine 进行整体管理
	
JscriptEngine
	JscriptEngine 对象构建, 
		注入variables
		运行init
		注册function
	
	注意: JS是弱引用类型变量值可以随意替换, 为了使JscriptEngine引擎使用简单透明且不出错, 定义了几个基本规则。
		1.常量不可修改
		2.函数需要使用注册方法后才能有函数名(变量名), 否则都是以匿名函数直接运行
		在原本的引擎当中不同的环境运行
		getTempEngine() 临时引擎, 临时引擎具有与原引擎一样的环境, 但是不可以修改原环境的任何内容, 
	 

Jscript 是最基础的JS脚本对象封装, 只含有JS脚本代码块, JscriptEngine运行也只运行这些代码块
JscriptInit 是对于JscriptEngine



JscriptEngine ->
	JscriptFolder ->
		JscriptFolder ->
			JscriptFolder ->
				Jscript
				Jscript
			Jscript
			Jscript
		JscriptFolder ->
			Jscript
			Jscript
			Jscript
		JscriptFolder ->
			Jscript

持久引擎 - 运行稳定内容
临时引擎 - 运行测试内容
	包含拥有持久引擎的运行环境(变量, 函数), 但是不能对其进行修改, 引擎中做的任何操作最后会还原





this[变量名] 可获取变量自身或函数自身
可通过 return typeof(this[变量名]); 来判别类型 'undefined' 为尚未定义, 'function' 为函数类型, 其它类型说明是变量, 由此可限制引擎变量的数据注册