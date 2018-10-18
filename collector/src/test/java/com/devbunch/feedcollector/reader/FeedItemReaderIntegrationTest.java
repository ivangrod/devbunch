package com.devbunch.feedcollector.reader;

import static org.junit.Assert.assertNotNull;

import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devbunch.feedcollector.FeedCollectorApplication;
import com.rometools.rome.feed.synd.SyndEntry;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FeedCollectorApplication.class)
public class FeedItemReaderIntegrationTest {

	@Autowired
	private FeedItemReader feedItemReader;

	@Test
	@Ignore
	public void readAirPairSource() throws Exception {

		// uri: https://www.airpair.com/rss
		Entry<String, SyndEntry> entriesByOrigin = feedItemReader.read();
		assertNotNull(entriesByOrigin);
	}
}
