package edu.neu.cs5500.wizards;

import edu.neu.cs5500.wizards.auth.ServiceAuthenticator;
import edu.neu.cs5500.wizards.core.User;
import edu.neu.cs5500.wizards.db.BidDAO;
import edu.neu.cs5500.wizards.db.FeedbackDAO;
import edu.neu.cs5500.wizards.db.ItemDAO;
import edu.neu.cs5500.wizards.db.UserDAO;
import edu.neu.cs5500.wizards.resources.BidResource;
import edu.neu.cs5500.wizards.resources.FeedbackResource;
import edu.neu.cs5500.wizards.resources.ItemResource;
import edu.neu.cs5500.wizards.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.skife.jdbi.v2.DBI;

public class EbayCloneApplication extends Application<ServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new EbayCloneApplication().run(args);
    }

    @Override
    public String getName() {
        return "ebay-clone";
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        // Swagger
        bootstrap.addBundle(new SwaggerBundle<ServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ServiceConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        // Migrations
        bootstrap.addBundle(new MigrationsBundle<ServiceConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws ClassNotFoundException {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        // User endpoints
        final UserDAO userDao = jdbi.onDemand(UserDAO.class);
        final ItemDAO itemDaoForUser = jdbi.onDemand(ItemDAO.class);
        final FeedbackDAO feedbackDaoForUser = jdbi.onDemand(FeedbackDAO.class);
        environment.jersey().register(new UserResource(userDao, itemDaoForUser, feedbackDaoForUser));

        // Item endpoints
        final ItemDAO itemDao = jdbi.onDemand(ItemDAO.class);
        environment.jersey().register(new ItemResource(itemDao, userDao));

        // Bids endpoints
        final BidDAO bidDao = jdbi.onDemand(BidDAO.class);
        final UserDAO userDaoForBids = jdbi.onDemand(UserDAO.class);
        final ItemDAO itemDaoForBids = jdbi.onDemand(ItemDAO.class);
        environment.jersey().register(new BidResource(bidDao, userDaoForBids, itemDaoForBids));

        // Feedback endpoints
        final FeedbackDAO feedbackDao = jdbi.onDemand(FeedbackDAO.class);
        final UserDAO userDaoForFeedback = jdbi.onDemand(UserDAO.class);
        environment.jersey().register(new FeedbackResource(feedbackDao, userDaoForFeedback));

        // authentication
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new ServiceAuthenticator(userDao))
                        .setRealm("Needs Authentication")
                        .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

    }
}
