package com.tribehired.postsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "post_id", "post_title", "post_body", "total_number_of_comments" })
public class PostRes {

	@JsonProperty("post_id")
	private int id;
	@JsonProperty("total_number_of_comments")
	private int commentsCount;
	@JsonProperty("post_title")
	private String title;
	@JsonProperty("post_body")
	private String body;

	public PostRes() {}

	public PostRes(int id, String title, String body, int commentsCount) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.commentsCount = commentsCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	
}