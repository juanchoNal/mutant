package com.juanchonal.prueba.mutant;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class MutantService {


  private final MutantStatsRepository mutantStatsRepository;

  private static final List<String> posiblesSecuencias = List.of(
    "AAAA",
    "TTTT",
    "CCCC",
    "GGGG"
  );

  public MutantService(MutantStatsRepository mutantStatsRepository) {
    this.mutantStatsRepository = mutantStatsRepository;
  }

  public Mono<Boolean> isMutant(MutantDTO mutantDTO) {

    var dna = Arrays.stream(mutantDTO.getDna())
      // convertimos a mayúsculas
      .map(String::toUpperCase)
      ///convertimos de nuevo a String[]
      .toArray(String[]::new);

    int N = dna[0].length();

    var espejo = mirror(dna, N);
    var trans = transpuesta(dna, N);

    var chainsList = List.of(
      dna,
      // transpuesta -> filas en columnas
      trans,
      // diagonales normales
      diags(dna, N),
      // diagonales del espejo = diagonales inversas de dna
      diags(espejo, N)
    );

    var count = 0;

    var isMutant = false;

    for (var chains : chainsList) {

      count += checkMutant(chains, N);

      if (count > 1) {
        isMutant = true;
        break;
      }

    }

    return mutantStatsRepository.save(new Stats(isMutant))
      .map(Stats::isMutant);
  }

  /**
   * Dado un listado de cadenas busca si por lo menos 2 tienen una secuencia mutante
   *
   * @param chains Listado de cadenas de ADN
   * @param N      tamaño de la matriz NxN
   * @return cantidad de ocurrencias (max 2)
   */
  private int checkMutant(String[] chains, int N) {

    var isMutant = 0;

    // Por cada una de las cadenas
    for (var chain : chains) {

      // Por cada una de las secuencias a buscar
      for (var sequencia : posiblesSecuencias) {

        // Si el texto es inferior a la secuancia entonces no se evalua porque nunca cumple
        if (chain.length() < sequencia.length()) {
          continue;
        }

        // Revisamos si la cadena contiene la secuencia
        if (chain.contains(sequencia)) {
          isMutant++;
        }

        // En el momento que tengamos más de una paramos pues ya es mutante
        if (isMutant > 1) {
          return isMutant;
        }

      }

    }
    return isMutant;
  }

  /**
   * Rota una matriz como espejo
   *
   * @param matrix matriz a rotar
   * @param N      tamaño de la matriz NxN
   * @return la metriz rotada tipo espejo
   */
  private static String[] mirror(String[] matrix, int N) {

    var tmp = new LinkedList<String>();

    for (var row : matrix) {
      tmp.add(new StringBuilder(row).reverse().toString());
    }

    //
    return tmp.toArray(new String[0]);

  }

  /***
   * Encuentra las diagonales de nuna matriz
   * @param matrix la matriz base
   * @param N tamaño de la matriz NxN
   * @return listado de las diagonales de la matriz
   */
  private static String[] diags(String[] matrix, int N) {

    assert matrix.length == N;

    HashMap<String, String> diags = new HashMap<>();


    // parto cada string en un array de strings
    IntStream.range(0, N).forEach(idx -> {

      IntStream.range(idx, N).forEach(idy -> {

        var xy = "" + (idx - idy);
        var yx = "" + (idy - idx);

        var diag = diags.getOrDefault(xy, "");
        var diagInv = diags.getOrDefault(yx, "");

        diag += matrix[idx].charAt(idy);
        diagInv += matrix[idy].charAt(idx);

        diags.put(xy, diag);
        diags.put(yx, diagInv);


      });

    });

    return diags.values().stream().filter(st -> st.length()<4).toArray(String[]::new);
  }

  /**
   * Dada una matriz, regresa la transpuesta y el listado de diagonales de la misma
   *
   * @param matrix la mtriz a afectar
   * @param N      tamaño de la matriz NxN
   * @return la matriz transpuesta y el listado de las diagonales
   */
  private static String[] transpuesta(String[] matrix, int N) {

    assert matrix.length == N;

    String[] rotated = {"", "", "", "", "", ""};

    // parto cada string en un array de strings
    IntStream.range(0, N).forEach(idx -> {
      IntStream.range(0, N).forEach(idy -> {


        var row = matrix[idy];
        var col = row.split("(?!^)");

        rotated[idx] = rotated[idx] + col[idx];

      });

    });

    return rotated;
  }



    public Mono<Map<String,Object>> getStats(){

     return mutantStatsRepository.findAll().collectList().map(mutantStats -> {

       // contamos cuántos son mutantes
       var mutant = (int) mutantStats.stream().filter(Stats::isMutant).count();
       var human= mutantStats.size() - mutant;

       return Map.of(
         "count_mutant_dna",mutant,
         "count_human_dna", human,
         "ratio", (mutant==0? 1.0 : (human/mutant))
       );

     });

    }

}
