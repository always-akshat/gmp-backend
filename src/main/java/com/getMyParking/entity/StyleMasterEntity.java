package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 20/08/15.
 */
@Entity
@Table(name = "style_master", schema = "", catalog = "get_my_parking_v2")
public class StyleMasterEntity {
    private String title;
    private String font;
    private String doubleHeight;
    private String doubleWidth;
    private String negative;
    private String underline;
    private String feed;

    @Id
    @Column(name = "title", nullable = false, insertable = true, updatable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "font", nullable = true, insertable = true, updatable = true, length = 255)
    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    @Basic
    @Column(name = "double_height", nullable = true, insertable = true, updatable = true, length = 255)
    public String getDoubleHeight() {
        return doubleHeight;
    }

    public void setDoubleHeight(String doubleHeight) {
        this.doubleHeight = doubleHeight;
    }

    @Basic
    @Column(name = "double_width", nullable = true, insertable = true, updatable = true, length = 255)
    public String getDoubleWidth() {
        return doubleWidth;
    }

    public void setDoubleWidth(String doubleWidth) {
        this.doubleWidth = doubleWidth;
    }

    @Basic
    @Column(name = "negative", nullable = true, insertable = true, updatable = true, length = 255)
    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    @Basic
    @Column(name = "underline", nullable = true, insertable = true, updatable = true, length = 255)
    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    @Basic
    @Column(name = "feed", nullable = true, insertable = true, updatable = true, length = 255)
    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StyleMasterEntity that = (StyleMasterEntity) o;

        if (doubleHeight != null ? !doubleHeight.equals(that.doubleHeight) : that.doubleHeight != null) return false;
        if (doubleWidth != null ? !doubleWidth.equals(that.doubleWidth) : that.doubleWidth != null) return false;
        if (feed != null ? !feed.equals(that.feed) : that.feed != null) return false;
        if (font != null ? !font.equals(that.font) : that.font != null) return false;
        if (negative != null ? !negative.equals(that.negative) : that.negative != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (underline != null ? !underline.equals(that.underline) : that.underline != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (font != null ? font.hashCode() : 0);
        result = 31 * result + (doubleHeight != null ? doubleHeight.hashCode() : 0);
        result = 31 * result + (doubleWidth != null ? doubleWidth.hashCode() : 0);
        result = 31 * result + (negative != null ? negative.hashCode() : 0);
        result = 31 * result + (underline != null ? underline.hashCode() : 0);
        result = 31 * result + (feed != null ? feed.hashCode() : 0);
        return result;
    }
}
