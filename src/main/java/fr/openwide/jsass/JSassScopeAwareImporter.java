package fr.openwide.jsass;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class JSassScopeAwareImporter implements Importer {
	
	public JSassScopeAwareImporter() {
	}

	@Override
	public Collection<Import> apply(String url, Import previous) {
		return Collections.singletonList(getInputSource(url, previous.getBase()));
	}
	
	public Import getInputSource(String url, URI base) {
		try {
			url = url.replaceFirst(".scss$", "");
			if (url.contains("/")) {
				base = base.resolve(url.substring(0, url.lastIndexOf("/") + 1));
				url = url.substring(url.lastIndexOf("/") + 1);
			}
		
			List<URI> potentialIdentifiers = new ArrayList<URI>();
			potentialIdentifiers.add(new URI("_" + url + ".scss"));
			potentialIdentifiers.add(new URI(url + ".scss"));
			
			for (URI potentialIdentifier : potentialIdentifiers) {
				URI potentialUri = base.resolve(potentialIdentifier);
				ClassPathResource cpr = new ClassPathResource(potentialUri.toString().replaceFirst("^classpath:/", ""));
				try {
					return new Import(potentialIdentifier, potentialUri, IOUtils.toString(cpr.getInputStream()));
				} catch (IOException e) {
				}
			}
			
			throw new IllegalArgumentException(String.format("File \"%1$s\" not imported because it could not be found", base.resolve(url)));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(String.format("Invalid URI syntax for \"%1$s\" from path \"%2$s\"", url, base));
		}
	}

}