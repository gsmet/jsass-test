package fr.openwide.jsass;

import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.StringContext;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class ScssServiceImpl {
	
	public String getCompiledStylesheet(Class<?> scope, String path, boolean checkCacheEntryUpToDate) {
		String scssPath = getFullPath(scope, path);
		
		try {
			Compiler compiler = new Compiler();
			Options options = new Options();
			options.getImporters().add(new JSassScopeAwareImporter());
			
			ClassPathResource cpr = new ClassPathResource(scssPath);
			
			Context fileContext = new StringContext(IOUtils.toString(cpr.getInputStream()), new URI("classpath", "//" + scssPath, null), null, options);
			Output output = compiler.compile(fileContext);

			return output.getCss();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error compiling %1$s", scssPath), e);
		}
	}
	
	public static String getFullPath(Class<?> scope, String path) {
		StringBuilder fullPath = new StringBuilder();
		if (scope != null) {
			fullPath.append(scope.getPackage().getName().replace(".", "/")).append("/");
		}
		fullPath.append(path);
		return fullPath.toString();
	}

}
