package com.devbunch.feedcollector.reader.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedLastInfoCollection {

	private List<FeedLastInfo> feedLastInfoIt = new ArrayList<>();
}
