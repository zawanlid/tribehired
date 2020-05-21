package com.tribehired.postsapi.model;

public class PostReq implements Comparable<PostReq>{

	private int userId;
	private int id;
	private Integer commentsCount;
	private String title;
	private String body;

	@Override
	public int compareTo(PostReq p) {
		return this.getCommentsCount().compareTo(p.getCommentsCount());
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
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