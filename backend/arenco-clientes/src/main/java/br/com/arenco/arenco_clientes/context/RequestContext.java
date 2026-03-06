package br.com.arenco.arenco_clientes.context;

public class RequestContext {
  private static final ThreadLocal<RequestData> requestDataHolder = new ThreadLocal<>();

  public static void set(final RequestData data) {
    requestDataHolder.set(data);
  }

  public static RequestData get() {
    return requestDataHolder.get();
  }

  public static void clear() {
    requestDataHolder.remove();
  }
}
