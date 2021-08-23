package com.juanchonal.prueba.mutant;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/")
public class MutantDetectorController {

  private final MutantService mutantService;

  public MutantDetectorController(MutantService mutantService) {
    this.mutantService = mutantService;
  }

  @PostMapping("mutant/")
  private Mono<Void> isMutant(@RequestBody MutantDTO mutant, ServerHttpResponse response) {

    return mutantService.isMutant(mutant).flatMap(isMutantResult -> {
      response.setStatusCode(isMutantResult ? HttpStatus.OK : HttpStatus.FORBIDDEN);
      return Mono.empty();

    });
  }

  @GetMapping("stats")
  private Mono<Map<String, Object>> updateStats() {
    return mutantService.getStats();
  }

}