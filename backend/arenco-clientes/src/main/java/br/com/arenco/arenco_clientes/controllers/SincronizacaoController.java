package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.facades.JobInfoFacade;
import br.com.arenco.arenco_clientes.dtos.misc.PageResponse;
import br.com.arenco.arenco_clientes.dtos.sync.JobInfoDto;
import br.com.arenco.arenco_clientes.utils.PageableUtils;
import br.com.arenco.arenco_clientes.enums.JobType;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sync")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class SincronizacaoController {
  private final JobInfoFacade jobInfoFacade;

  @PostMapping("/{jobType}")
  public ResponseEntity<HttpStatus> triggerManualSync(@PathVariable final JobType jobType) {
    jobInfoFacade.createManualTrigger(jobType);
    return ResponseEntity.created(URI.create("/admin/sync")).build();
  }

  @GetMapping("/{jobType}")
  public ResponseEntity<PageResponse<JobInfoDto>> getAllJobs(
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "size", defaultValue = "20") final int size,
      final Pageable pageable,
      @PathVariable final JobType jobType) {
    final var result =
        jobInfoFacade.getAll(PageableUtils.adjustPageable(pageable, page, size), jobType);
    final PageResponse<JobInfoDto> pageResponse = new PageResponse<>(result);
    return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
  }
}
