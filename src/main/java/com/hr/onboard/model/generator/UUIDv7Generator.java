package com.hr.onboard.model.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import com.fasterxml.uuid.Generators;

import java.io.Serializable;

public class UUIDv7Generator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        return Generators.timeBasedGenerator().generate();
    }
}
