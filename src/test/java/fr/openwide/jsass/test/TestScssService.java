package fr.openwide.jsass.test;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.jsass.ScssServiceImpl;
import fr.openwide.jsass.test.resource.TestScssServiceResourceScope;

public class TestScssService {
	
	@Test
	public void testGetCompiledStylesheet() throws Exception {
		try {
			ScssServiceImpl scssService = new ScssServiceImpl();
			String compiledStylesheet = scssService.getCompiledStylesheet(
					TestScssServiceResourceScope.class,
					"style.scss",
					false
			);
			
			Assert.assertEquals(".test2 {\n\tcolor: #eeeeee;\n}\n\n.test {\n\tcolor: #cccccc;\n}", compiledStylesheet);
		} finally {
		}
	}

}