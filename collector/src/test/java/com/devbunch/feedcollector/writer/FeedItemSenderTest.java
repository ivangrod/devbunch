package com.devbunch.feedcollector.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.devbunch.model.FeedItem;

@RunWith(MockitoJUnitRunner.class)
public class FeedItemSenderTest {
	
	@Mock
	private KafkaTemplate<String, FeedItem> kafkaTemplate;
	
	
	@InjectMocks
	private FeedItemSender feedItemSender;

	@Test
	public void testWrite() throws Exception {
		// given
		FeedItem feed = new FeedItem();
		feed.setUri("wwww.baeldung.com");
		ProducerRecord<String, FeedItem> producerRecord = new ProducerRecord<String, FeedItem>("topic", feed);
		RecordMetadata recordMetadata = null;
		SendResult<String, FeedItem> result = new SendResult<>(producerRecord, recordMetadata);
		ListenableFuture<SendResult<String, FeedItem>> future = new AsyncResult<>(result);
		BDDMockito.given(kafkaTemplate.send(BDDMockito.eq(null), BDDMockito.eq(feed))).willReturn(future);
		List<FeedItem> feedItems = new ArrayList<>();
		feedItems.add(feed);
		
		// when
		feedItemSender.write(feedItems);
		
	}

}
