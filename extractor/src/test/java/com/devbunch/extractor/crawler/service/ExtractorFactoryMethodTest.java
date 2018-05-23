package com.devbunch.extractor.crawler.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.devbunch.extractor.crawler.ICrawler;


public class ExtractorFactoryMethodTest {

	@Test
	public void testGetExtractor() throws Exception {
		// given
		String origin = "baeldung";
		String link = "www.baeldung.com";
		Optional<ICrawler> crawler = Optional.of(Mockito.mock(ICrawler.class));
		
		
		// when
		ExtractorService service = ExtractorFactoryMethod.getExtractor(origin, link, crawler);
		
		// then
		Assert.assertNotNull(service);
	}

}
