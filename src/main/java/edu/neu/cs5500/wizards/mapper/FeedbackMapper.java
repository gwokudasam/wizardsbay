package edu.neu.cs5500.wizards.mapper;

import edu.neu.cs5500.wizards.core.Feedback;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by susannaedens on 6/20/16.
 */
public class FeedbackMapper implements ResultSetMapper<Feedback> {

    @Override
    public Feedback map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setId(resultSet.getInt("id"));
        feedback.setUserId(resultSet.getInt("user_id"));
        feedback.setRating(resultSet.getInt("rating"));
        feedback.setFeedbackDescription(resultSet.getString("feedback_description"));
        feedback.setTime(resultSet.getTimestamp("time"));
        return feedback;
    }

}
