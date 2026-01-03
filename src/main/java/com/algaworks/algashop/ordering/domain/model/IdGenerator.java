package com.algaworks.algashop.ordering.domain.model;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import io.hypersistence.tsid.TSID;
import java.util.UUID;


public class IdGenerator {

    private static final TimeBasedEpochGenerator timeBasedEpochGenerator
        = Generators.timeBasedEpochGenerator();

    private static final TSID.Factory tsidFactory = TSID.Factory.INSTANCE;

    public IdGenerator() {
    }

    public static UUID generateTimeBasedUUID() {
        return timeBasedEpochGenerator.generate();
    }

    public static TSID generateTSID() {
        return tsidFactory.generate();
    }
}
