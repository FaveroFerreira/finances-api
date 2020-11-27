package com.deliver.finances.config;

import com.deliver.finances.config.flags.FeatureFlags;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.repository.util.DefaultMapSerializer;
import org.togglz.core.spi.FeatureProvider;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;

@Configuration
public class FeatureFlagsConfig {

    private static final String FEATURE_FLAG_TABLE = "FEATURE_FLAG_CONFIGURATION";

    @Bean
    public UserProvider userProvider() {
        return new UserProvider() {
            @Override
            public FeatureUser getCurrentUser() {
                return null;
            }
        };
    }

    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider().addFeatureEnum(FeatureFlags.class);
    }

    @Bean
    public StateRepository stateRepository(DataSource dataSource) {
        return JDBCStateRepository.newBuilder(dataSource)
                .tableName(FEATURE_FLAG_TABLE)
                .createTable(false)
                .serializer(DefaultMapSerializer.singleline())
                .noCommit(true)
                .build();
    }

}
