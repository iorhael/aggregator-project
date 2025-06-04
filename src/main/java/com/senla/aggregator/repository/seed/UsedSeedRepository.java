package com.senla.aggregator.repository.seed;

public interface UsedSeedRepository {

   boolean wasSeedUsed(String name);

   void markSeedAsUsed(String name);
}
