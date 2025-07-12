package com.svalero.memazo.domain;

public class PublicationInDto {

    private Long userId;
    private String content;
    private String imageUrl;
    private String typeContent;
    private String privacy;
    private Double latitude;
    private Double longitude;

    public PublicationInDto(Long userId, String content, String imageUrl, String typeContent, String privacy, Double latitude, Double longitude) {
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.typeContent = typeContent;
        this.privacy = privacy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}