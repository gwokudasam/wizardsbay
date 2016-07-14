package edu.neu.cs5500.wizards.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by susannaedens on 6/20/16.
 */
public class Feedback {

    @JsonProperty
    @NotEmpty
    private int id;

    @JsonProperty
    @NotEmpty
    private int userId;

    @JsonProperty
    @NotEmpty
    private int rating;

    @JsonProperty
    @NotEmpty
    private String feedbackDescription;

    @JsonProperty
    @NotEmpty
    private Timestamp time;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Feedback() {}

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, feedbackDescription, time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (id != feedback.id) return false;
        if (userId != feedback.userId) return false;
        if (!feedbackDescription.equals(feedback.feedbackDescription)) return false;
        return time.equals(feedback.time);

    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
