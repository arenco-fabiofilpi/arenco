package br.com.arenco.arenco_clientes.constants;

public final class ArencoFeatureFlags {
  public static final String UPDATE_CUSTOMER_ALLOWED = "update-customer-allowed";
  public static final String OTHER_FEATURE_FLAG = "other-feature-flag";

  private ArencoFeatureFlags() {
    throw new IllegalStateException("Utility class");
  }
}
