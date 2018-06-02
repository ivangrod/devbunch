package com.devbunch.feedcollector.reader.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedLastInfo {

	private String origin;

	private Date publicationTimestamp;
}
