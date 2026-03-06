package br.com.arenco.arenco_clientes.context;

import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Getter
public class ArencoAuthenticatedPreSessionContext extends PreAuthenticatedAuthenticationToken {
  private final AuthenticatedPreSessionModel authenticatedPreSessionModel;
  private final AtomicReference<PreSessionOtpModel> oneTimePasswordModel;

  public ArencoAuthenticatedPreSessionContext(
      final Object aPrincipal,
      final Object aCredentials,
      final Collection<? extends GrantedAuthority> anAuthorities,
      final AuthenticatedPreSessionModel authenticatedPreSessionModel,
      final AtomicReference<PreSessionOtpModel> oneTimePasswordModel) {
    super(aPrincipal, aCredentials, anAuthorities);
    this.authenticatedPreSessionModel = authenticatedPreSessionModel;
    this.oneTimePasswordModel = oneTimePasswordModel;
  }
}
