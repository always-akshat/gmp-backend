package com.getMyParking.service.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

/**
 * Created by rahulgupta.s on 14/03/15.
 */
public class GetMyParkingConfiguration extends Configuration {

    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory;

    @JsonProperty("flyway")
    private FlywayFactory flywayFactory;

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public FlywayFactory getFlywayFactory() {
        return flywayFactory;
    }

    public void setFlywayFactory(FlywayFactory flywayFactory) {
        this.flywayFactory = flywayFactory;
    }
}
