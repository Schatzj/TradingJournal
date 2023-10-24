package models;

import java.util.ArrayList;
import java.util.List;

public class FileTags {
	private String fileName;
	private List<String> tags = new ArrayList<>();
	
	public FileTags() {}
	
	public FileTags(String fileName, List<String> tags) {
		super();
		this.fileName = fileName;
		this.tags = tags;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}

}
