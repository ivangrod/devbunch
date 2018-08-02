package com.devbunch.feedcollector.reader;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import com.google.common.collect.Maps;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class FeedItemReader extends ItemReaderAdapter<Entry<String, SyndEntry>> {

	private static final Logger logger = LoggerFactory.getLogger(FeedItemReader.class);

	@Value("classpath:opml/engineering_blogs.opml")
	private Resource resourceOpml;

	private List<Outline> outlinesToRead;

	private String currentOriginToRead;

	private List<SyndEntry> currentEntriesToRead;

	private CollectorPersistingStore collectorPersistingStore;

	@PostConstruct
	public void init() {

		super.setTargetObject(this);
		super.setTargetMethod("read");

		collectorPersistingStore = CollectorPersistingStore.getInstance();

		WireFeedInput input = new WireFeedInput();
		List<Outline> outlines = null;
		try {
			Opml feed = (Opml) input.build(new InputSource(this.resourceOpml.getInputStream()));
			outlines = feed.getOutlines();
		} catch (IllegalArgumentException | IOException | FeedException exc) {
			logger.error("An error has been produced while opml was processed", exc);
		}

		this.outlinesToRead = !CollectionUtils.isEmpty(outlines) ? outlines.get(0).getChildren()
				: Collections.emptyList();
	}

	@PreDestroy
	public void saveCurrentStateOfCollectorPersistingStore() {

		try {
			collectorPersistingStore.close();
		} catch (IOException e) {
			logger.error("Failed to persist the current state of CollectorPersistingStore.", e);
		}
	}

	@Override
	public Entry<String, SyndEntry> read() throws Exception {

		if (!CollectionUtils.isEmpty(this.currentEntriesToRead)) {
			return this.analyzeEntriesUpdatedToDate();

		} else if (!CollectionUtils.isEmpty(this.outlinesToRead)) {
			collectorPersistingStore.flush();
			initLoadOfCurrentEntriesToRead();
			return this.read();
		}

		collectorPersistingStore.flush();
		return null;
	}

	private void initLoadOfCurrentEntriesToRead() {

		Outline outlineForFeed = this.outlinesToRead.remove(0);
		Entry<String, SyndFeed> feedWithOrigin = this.buildOriginAndFeedPair(outlineForFeed.getText(),
				outlineForFeed.getXmlUrl());

		this.currentOriginToRead = feedWithOrigin.getKey();

		Optional.ofNullable(feedWithOrigin.getValue()).ifPresent(feed -> this.currentEntriesToRead = feed.getEntries()
				.stream()
				.sorted(Comparator.comparing(SyndEntry::getPublishedDate, nullsLast(naturalOrder()))
						.thenComparing(Comparator.comparing(SyndEntry::getUpdatedDate, nullsLast(naturalOrder()))))
				.collect(Collectors.toList()));
	}

	private Entry<String, SyndFeed> buildOriginAndFeedPair(final String origin, final String feedUrl) {

		SyndFeed feed = null;

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpUriRequest request = new HttpGet(feedUrl);
			try (CloseableHttpResponse response = client.execute(request);
					InputStream stream = response.getEntity().getContent()) {
				SyndFeedInput input = new SyndFeedInput();
				feed = input.build(new XmlReader(stream));
			}
		} catch (IllegalArgumentException | FeedException | IOException exc) {
			logger.error("Url of feed [{}]", feedUrl);
			logger.error("An error has been produced while opml was processed.", exc);
		}

		return Maps.immutableEntry(origin.toUpperCase(), feed);
	}

	private Entry<String, SyndEntry> analyzeEntriesUpdatedToDate() {

		SyndEntry entryToRead = this.currentEntriesToRead.remove(0);

		try {

			Date entryDate = entryToRead.getPublishedDate() != null ? entryToRead.getPublishedDate()
					: entryToRead.getUpdatedDate();

			if (collectorPersistingStore.checkEntryInfoWasStored(this.currentOriginToRead, entryDate)) {
				// Don't clear entries to read list, because it's possible to find several feed
				// equals in the same iteration
				// this.currentEntriesToRead.clear();
				return this.read();
			} else {
				collectorPersistingStore.storeEntryInfo(this.currentOriginToRead, entryDate);
			}

		} catch (Exception e) {
			logger.error("An error has been produced checking if entry was stored previously .", e);
		}

		return new AbstractMap.SimpleImmutableEntry<>(this.currentOriginToRead, entryToRead);
	}
}
