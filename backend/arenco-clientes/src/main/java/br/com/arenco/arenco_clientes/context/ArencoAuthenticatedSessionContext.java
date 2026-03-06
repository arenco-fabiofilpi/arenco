package br.com.arenco.arenco_clientes.context;

import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class ArencoAuthenticatedSessionContext extends UsernamePasswordAuthenticationToken {
  private final UserModel user;

  public ArencoAuthenticatedSessionContext(
      final Object principal,
      final Object credentials,
      final Collection<? extends GrantedAuthority> authorities,
      final UserModel user) {
    super(principal, credentials, authorities);
    this.user = user;
  }
}
