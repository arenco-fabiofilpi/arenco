package br.com.arenco.arenco_clientes.dtos.misc;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> {
  private final List<T> content;
  private final int pageNumber;
  private final int pageSize;
  private final long totalElements;
  private final int totalPages;

  public PageResponse(final Page<T> page) {
    this.content = page.getContent();
    this.pageNumber = page.getNumber() + 1;
    this.pageSize = page.getSize();
    this.totalElements = page.getTotalElements();
    this.totalPages = page.getTotalPages();
  }
}
