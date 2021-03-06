package com.tp.bsserver.model;


import com.tp.bsserver.util.GsonUtil;

//图文消息
public class ImgTextMessage extends Message {

	private String content;
	private String title;
	private String imageUri;
	private String extra;
	private String url;

	public ImgTextMessage(String content, String title, String imageUri,
			String extra, String url) {
		this(content, title, imageUri, extra);
		this.url = url;
	}

	public ImgTextMessage(String content, String title, String imageUri) {
		this.type = "RC:ImgTextMsg";
		this.content = content;
		this.title = title;
		this.imageUri = imageUri;
	}

	public ImgTextMessage(String content, String title, String imageUri,
			String extra) {
		this(content, title, imageUri);
		this.extra = extra;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return GsonUtil.toJson(this, ImgTextMessage.class);
	}
}
