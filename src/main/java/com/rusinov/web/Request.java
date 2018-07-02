package com.rusinov.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

	String taskName;
	String torrentURL;
	String subtitleURL;

	public String getTaskName() {
		return taskName;
	}

	@JsonProperty(required = true)
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTorrentURL() {
		return torrentURL;
	}

	@JsonProperty(required = true)
	public void setTorrentURL(String torrentURL) {
		this.torrentURL = torrentURL;
	}

	public String getSubtitleURL() {
		return subtitleURL;
	}

	@JsonProperty(required = false)
	public void setSubtitleURL(String subtitleURL) {
		this.subtitleURL = subtitleURL;
	}

}
