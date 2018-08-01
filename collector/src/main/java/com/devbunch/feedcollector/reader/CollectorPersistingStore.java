package com.devbunch.feedcollector.reader;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import com.devbunch.feedcollector.reader.dto.FeedLastInfo;
import com.devbunch.feedcollector.reader.dto.FeedLastInfoCollection;

public class CollectorPersistingStore implements DisposableBean, Closeable, Flushable {

	private static final Logger logger = LoggerFactory.getLogger(CollectorPersistingStore.class);

	private static volatile CollectorPersistingStore INSTANCE;

	private final Yaml yaml = new Yaml();

	private FeedLastInfoCollection feedInfoCollectorStore;

	private Map<String, Date> feedInfoCollectorCache = new ConcurrentHashMap<>();

	private String baseDirectory = System.getProperty("java.io.tmpdir") + "/collect-store/";

	private String fileName = "feed-last-info.yml";

	private File file;

	private volatile boolean dirty;

	public static CollectorPersistingStore getInstance() {
		if (INSTANCE == null) {
			synchronized (CollectorPersistingStore.class) {
				if (INSTANCE == null) {
					INSTANCE = new CollectorPersistingStore();
				}
			}
		}
		return INSTANCE;
	}

	private CollectorPersistingStore() {
		File baseDir = new File(this.baseDirectory);
		baseDir.mkdirs();
		this.file = new File(baseDir, this.fileName);
		try {
			if (!this.file.exists()) {
				this.file.createNewFile();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Failed to create collect-store file '" + this.file.getAbsolutePath() + "'", e);
		}
		this.loadMetadata();
	}

	public void storeEntryInfo(String origin, Date publishedDate) {
		Assert.notNull(origin, "'origin' cannot be null");
		Assert.notNull(publishedDate, "'publishedDate' cannot be null");

		try {
			this.feedInfoCollectorCache.put(origin, publishedDate);
		} finally {
			this.dirty = true;
		}

	}

	public boolean checkEntryInfoWasStored(String origin, Date publishedDate) {

		Assert.notNull(origin, "'origin' cannot be null");
		Assert.notNull(publishedDate, "'publishedDate' cannot be null");

		boolean entryWasStored = false;
		Date publishedDateStored = this.feedInfoCollectorCache.get(origin);

		if (publishedDateStored != null) {
			entryWasStored = publishedDateStored.equals(publishedDate) || publishedDateStored.after(publishedDate);
		}

		return entryWasStored;
	}

	@Override
	public void flush() throws IOException {
		saveFeedInfoCollector();
	}

	@Override
	public void close() throws IOException {
		flush();
	}

	@Override
	public void destroy() throws Exception {
		flush();
	}

	private void loadMetadata() {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(this.file));
			feedInfoCollectorStore = yaml.loadAs(inputStream, FeedLastInfoCollection.class);

			if (feedInfoCollectorStore != null) {
				feedInfoCollectorStore.getFeedLastInfoIt().forEach(feedLastInfo -> feedInfoCollectorCache
						.putIfAbsent(feedLastInfo.getOrigin(), feedLastInfo.getPublicationTimestamp()));
			} else {
				feedInfoCollectorStore = new FeedLastInfoCollection();
			}

		} catch (Exception e) {
			logger.warn("Failed to load entry from the persistent store. This may result in a duplicate "
					+ "entry after this component is restarted", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				logger.warn("Failed to close InputStream for: " + this.file.getAbsolutePath());
			}
		}
	}

	private void saveFeedInfoCollector() {
		if (this.file == null || !this.dirty) {
			return;
		}
		this.dirty = false;
		FileWriter fileWriter = null;
		try {

			List<FeedLastInfo> feedLastInfoList = feedInfoCollectorCache.entrySet().stream()
					.map(feedOriginWithPublicationTimestamp -> new FeedLastInfo(
							feedOriginWithPublicationTimestamp.getKey(), feedOriginWithPublicationTimestamp.getValue()))
					.collect(Collectors.toList());
			this.feedInfoCollectorStore.setFeedLastInfoIt(feedLastInfoList);

			fileWriter = new FileWriter(this.file);
			yaml.dump(this.feedInfoCollectorStore, fileWriter);

		} catch (IOException e) {
			logger.warn("Failed to persist entry. This may result in a duplicate "
					+ "entry after this component is restarted.", e);
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				logger.warn("Failed to close OutputStream to " + this.file.getAbsolutePath(), e);
			}
		}
	}
}
