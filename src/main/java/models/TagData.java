package models;

import java.util.ArrayList;
import java.util.List;

public class TagData {
	private List<FileTags> tags = new ArrayList<>();

	public TagData() {}
	
	public TagData(List<FileTags> tags) {
		super();
		this.tags = tags;
	}

	public List<FileTags> getTags() {
		return tags;
	}

	public void setTags(List<FileTags> tags) {
		this.tags = tags;
	}
	
	public void addFileTag(FileTags tagData) {
		tags.add(tagData);
	}
}
