package com.onemoreuse.dto;

import java.util.ArrayList;
import java.util.List;

public class ReuseSuggestionDto {

    private String detectedObject;
    private String category;
    private String material;
    private String condition;
    private int confidenceScore;
    private String emoji;
    private List<IdeaDto> ideas = new ArrayList<>();

    public ReuseSuggestionDto() {
    }

    public ReuseSuggestionDto(String detectedObject, String category, String material,
                              String condition, int confidenceScore, String emoji, List<IdeaDto> ideas) {
        this.detectedObject = detectedObject;
        this.category = category;
        this.material = material;
        this.condition = condition;
        this.confidenceScore = confidenceScore;
        this.emoji = emoji;
        this.ideas = ideas;
    }

    public String getDetectedObject() {
        return detectedObject;
    }

    public void setDetectedObject(String detectedObject) {
        this.detectedObject = detectedObject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<IdeaDto> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<IdeaDto> ideas) {
        this.ideas = ideas;
    }
}
