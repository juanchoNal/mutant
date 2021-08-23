package com.juanchonal.prueba.mutant;


import org.springframework.data.annotation.Id;

public class Stats {

  public Stats() {
  }

  public Stats(Long id, boolean mutant) {
    this.id = id;
    this.mutant = mutant;
  }

  public Stats(boolean mutant) {
    this.mutant = mutant;
  }

  @Id
  private Long id;


  private boolean mutant;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isMutant() {
    return mutant;
  }

  public void setMutant(boolean mutant) {
    this.mutant = mutant;
  }
}
