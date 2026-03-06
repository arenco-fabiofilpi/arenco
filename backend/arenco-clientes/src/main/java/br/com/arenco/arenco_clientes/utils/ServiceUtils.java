package br.com.arenco.arenco_clientes.utils;


public enum ServiceUtils {
  ;

  public static RuntimeException createIdNotFoundException(
      final Class<? extends Exception> exceptionClass, final String exceptionMessage) {
    try {
      final Exception exception =
          exceptionClass.getDeclaredConstructor(String.class).newInstance(exceptionMessage);
      if (exception instanceof final RuntimeException runtimeException) {
        return runtimeException;
      } else {
        return new IllegalArgumentException(
            "A exceção fornecida não é uma RuntimeException", exception);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Erro ao instanciar a exceção fornecida", e);
    }
  }
}
