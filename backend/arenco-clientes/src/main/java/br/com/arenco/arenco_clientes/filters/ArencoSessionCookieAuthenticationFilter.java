package br.com.arenco.arenco_clientes.filters;

import br.com.arenco.arenco_clientes.constants.ArencoAuthLibConstants;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedSessionContext;
import br.com.arenco.arenco_clientes.services.ArencoAuthLibGroupService;
import br.com.arenco.arenco_clientes.services.ArencoSessionService;
import br.com.arenco.arenco_clientes.services.CookieService;
import br.com.arenco.arenco_clientes.utils.ArencoOtpUtils;
import br.com.arenco.arenco_clientes.entities.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoSessionCookieAuthenticationFilter extends OncePerRequestFilter {
  private final CookieService cookieService;
  private final ArencoSessionService arencoSessionService;
  private final ArencoAuthLibGroupService arencoAuthLibGroupService;

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (ArencoAuthLibConstants.SESSION_ID_COOKIE_NAME.equals(cookie.getName())) {
          try {
            validateSessionCookie(cookie);
          } catch (final RuntimeException e) {
            log.error("Removendo Session Cookie", e);
            cookieService.removeSessionCookie(response);
          }
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private void validateSessionCookie(final Cookie cookie) {
    final String sessionId = cookie.getValue();
    final var userModel = arencoSessionService.getUserBySessionId(sessionId);
    final UserDetails user = convertIntoUserDetails(userModel);
    final UsernamePasswordAuthenticationToken auth =
        new ArencoAuthenticatedSessionContext(user, null, user.getAuthorities(), userModel);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private UserDetails convertIntoUserDetails(final UserModel userModel) {
    final var userGroups = arencoAuthLibGroupService.getGroupsByUser(userModel);
    final var authorities = arencoAuthLibGroupService.getGrantedAuthorities(userGroups);
    return new User(
        userModel.getUsername(),
        userModel.getPassword() != null
            ? userModel.getPassword()
            : "{noop}" + ArencoOtpUtils.generateOtp(),
        authorities);
  }
}
