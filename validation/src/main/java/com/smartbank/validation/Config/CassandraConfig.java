package com.smartbank.validation.Config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.ExtraTypeCodecs;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.*;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(
        basePackages = "com.smartbank.validation.Repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "smartbank";
    }

    @Override
    public String getContactPoints() {
        return "localhost";
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Bean
    @Primary
    public CqlSessionFactoryBean session() {

        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(getContactPoints());
        session.setKeyspaceName(getKeyspaceName());
        session.setLocalDatacenter(getLocalDataCenter());
        return session;
    }


    @Bean
    @Primary
    public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {
        SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();

        CodecRegistry codecRegistry = session.getContext().getCodecRegistry();

        //setting up codec for transaction UDT in smartbank keyspace
        UserDefinedType transactionUdt = session.getMetadata().getKeyspace("smartbank")
                .flatMap(ks -> ks.getUserDefinedType("transaction"))
                .orElseThrow(IllegalStateException::new);

        TypeCodec<UdtValue> innerCodec = session.getContext()
                .getCodecRegistry()
                .codecFor(transactionUdt);
        TransactionCodec transactionCodec = new TransactionCodec(innerCodec);
        ((MutableCodecRegistry) codecRegistry).register(transactionCodec);

//        //setting up codec for transaction UDT in smartbank2 keyspace
//        UserDefinedType transactionUdt2 = session.getMetadata().getKeyspace("smartbank2")
//                .flatMap(ks -> ks.getUserDefinedType("transaction"))
//                .orElseThrow(IllegalStateException::new);
//
//        TypeCodec<UdtValue> innerCodec2 = session.getContext()
//                .getCodecRegistry()
//                .codecFor(transactionUdt2);
//
//        TransactionCodec transactionCodec2 = new TransactionCodec(innerCodec2);
//        ((MutableCodecRegistry) codecRegistry).register(transactionCodec2);

        sessionFactory.setSession(session);
        sessionFactory.setConverter(converter);
        sessionFactory.setSchemaAction(SchemaAction.NONE);
        return sessionFactory;
    }

    @Bean
    @Primary
    public CassandraConverter converter(CassandraMappingContext mappingContext) {
        return new MappingCassandraConverter(mappingContext);
    }

    @Bean
    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
        return new CassandraTemplate(sessionFactory, converter);
    }

}
