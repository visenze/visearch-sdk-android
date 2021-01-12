package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// PS-827 for IR demo
public class TagGroup {
    @SerializedName("tag_group")
    private String name;

    @SerializedName("tags")
    private List<Tag> tags = new ArrayList<>();

    public TagGroup(String tagGroup) {
        this.name = tagGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            return;
        }

        this.tags.add(tag);
    }
}
