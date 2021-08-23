package com.juanchonal.prueba.mutant;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MutantStatsRepository extends ReactiveCrudRepository<Stats, Long> {
}
