package byz.easy.jscript.core.graal;

import java.util.function.Consumer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

/**
 * @author 
 * @since 2020年7月27日
 */
public class GraalEngine {
	
	private Object renderedObject = null;
	 
    private Consumer<Object> fnResolve = object -> {
        renderedObject = object;
        System.out.println("fnResolve=>promiseResolved");
    };
 
    private Consumer<Object> fnRejected = object -> {
        renderedObject = object;
        System.out.println("fnRejected=>promiseRejected");
    };
    
    private void execute() {
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {
            Value eval = context.eval("js",
                    "(async function() { return 'Hello World' });"
            );
            eval.execute().invokeMember("then", fnResolve, fnRejected);
 
            System.out.println("renderedObject = " + renderedObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	public static void main(String[] args) {
//		GraalEngine engine = new GraalEngine();
//		engine.execute();
//	}

}
