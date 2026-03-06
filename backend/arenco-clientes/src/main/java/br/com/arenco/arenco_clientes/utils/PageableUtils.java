package br.com.arenco.arenco_clientes.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public enum PageableUtils {
  ;

  public static Pageable adjustPageable(final Pageable pageable, final int page, final int size) {
    final int pageIndex = Math.max(0, page - 1);
    return PageRequest.of(pageIndex, size, pageable.getSort().and(Sort.by("_id")));
  }
}
