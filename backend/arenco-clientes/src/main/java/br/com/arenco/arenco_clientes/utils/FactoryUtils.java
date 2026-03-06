package br.com.arenco.arenco_clientes.utils;

import java.text.NumberFormat;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum FactoryUtils {
  ;

  public static String formatarValorEmReais(final double valor) {
    final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    return formatoMoeda.format(valor);
  }
}
