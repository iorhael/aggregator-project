package com.senla.aggregator.repository.seed;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UsedSeedRepositoryImpl implements UsedSeedRepository {
    private static final String NAME_PARAMETER = "name";
    private static final String INSERT_USED_SEED_SQL = "INSERT INTO used_seeds(seed_name) VALUES (:name)";
    private static final String FIND_USED_SEED_SQL = "SELECT COUNT(*) FROM used_seeds WHERE seed_name = :name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean wasSeedUsed(String name) {
        Number count = (Number) entityManager.createNativeQuery(FIND_USED_SEED_SQL)
                .setParameter(NAME_PARAMETER, name)
                .getSingleResult();

        return count.intValue() > 0;
    }

    @Override
    public void markSeedAsUsed(String name) {
        entityManager.createNativeQuery(INSERT_USED_SEED_SQL)
                .setParameter(NAME_PARAMETER, name)
                .executeUpdate();
    }
}
