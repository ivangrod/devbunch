package com.devbunch.feedcollector.reader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CollectorPersistingStoreTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void validateCollectorPersistingStore() throws Exception {

		CollectorPersistingStore collectorStore = CollectorPersistingStore.getInstance();
		collectorStore.storeEntryInfo("foo", Date.from(Instant.now()));
		collectorStore.close();

		File file = new File(new File(System.getProperty("java.io.tmpdir") + "/collect-store/"), "feed-last-info.yml");
		assertTrue(file.exists());
	}

	@Test
	public void validateCollectorPersistingCheckWasStoredWhenPublishedDateIsAfter() throws Exception {

		final String origin = "foo";

		CollectorPersistingStore collectorStore = CollectorPersistingStore.getInstance();
		collectorStore.storeEntryInfo(origin, Date.from(Instant.now()));
		collectorStore.flush();

		assertTrue(
				collectorStore.checkEntryInfoWasStored(origin, Date.from(Instant.now().minus(10, ChronoUnit.MINUTES))));
	}

	@Test
	public void validateCollectorPersistingCheckWasStoredWhenPublishedDateIsBeforeOrEqual() throws Exception {

		final String origin = "foo";

		CollectorPersistingStore collectorStore = CollectorPersistingStore.getInstance();
		collectorStore.storeEntryInfo(origin, Date.from(Instant.now()));
		collectorStore.flush();

		assertFalse(
				collectorStore.checkEntryInfoWasStored(origin, Date.from(Instant.now().plus(10, ChronoUnit.MINUTES))));
	}
}
