// package br.com.arenco.arenco_clientes.aop;
//
// import br.com.arenco.arenco_clientes.aop.interfaces.FeatureFlag;
// import br.com.arenco.arenco_clientes.constants.ArencoFeatureFlags;
// import java.util.HashMap;
// import java.util.Map;
// import lombok.extern.slf4j.Slf4j;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Pointcut;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
//
// @Component
//// @Aspect
// @Slf4j
// public class FeatureFlagAspect {
//
//  // Aqui é onde as Feature Flags podem ser configuradas
//  private final Map<String, Boolean> featureFlags = new HashMap<>();
//
//  public FeatureFlagAspect(
//      @Value("${arenco-clientes.feature-flags.update-customer-allowed:true}")
//          final boolean updateCustomerAllowed,
//      @Value("${arenco-clientes.feature-flags.other-feature-flag:false}")
//          final boolean otherFeatureFlag) {
//    // A lista de Feature Flags pode ser configurada aqui ou ser carregada de um banco de
//    // dados/configuração externa
//    featureFlags.put(ArencoFeatureFlags.UPDATE_CUSTOMER_ALLOWED, updateCustomerAllowed);
//    featureFlags.put(ArencoFeatureFlags.OTHER_FEATURE_FLAG, otherFeatureFlag);
//  }
//
//  // Pointcut para interceptar métodos anotados com @FeatureFlag
//  @Pointcut("@annotation(br.com.arenco.arenco_clientes.aop.interfaces.FeatureFlag)")
//  public void featureFlagMethod() {}
//
//  // Advice genérico para verificar se a Feature Flag está ativa
//  @Around("featureFlagMethod()")
//  public Object checkFeatureFlag(final ProceedingJoinPoint pjp) throws Throwable {
//    // Recupera a anotação do método que está sendo executado
//    final FeatureFlag featureFlagAnnotation = getFeatureFlagAnnotation(pjp);
//    if (featureFlagAnnotation == null) {
//      // Se o método não tiver a anotação, permite a execução normalmente
//      return pjp.proceed();
//    }
//
//    // Verifica se a Feature Flag associada ao método está ativa
//    final String flagName = featureFlagAnnotation.value();
//    if (!isFeatureFlagActive(flagName)) {
//      log.warn("Feature Flag '{}' not active - {}", flagName, pjp.getSignature().getName());
//      // Se a flag não estiver ativa, retorna uma resposta HTTP 501 (Not Implemented)
//      return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
//    }
//
//    // Se a flag estiver ativa, permite que o método continue a execução normalmente
//    return pjp.proceed();
//  }
//
//  // Método para obter a anotação do método em execução
//  private FeatureFlag getFeatureFlagAnnotation(final ProceedingJoinPoint pjp)
//      throws NoSuchMethodException {
//    // A anotação @FeatureFlag está associada ao método executado
//    return pjp.getSignature()
//        .getDeclaringType()
//        .getMethod(pjp.getSignature().getName(), getParameterTypes(pjp))
//        .getAnnotation(FeatureFlag.class);
//  }
//
//  // Método para verificar se a flag está ativa (pode ser dinâmico de acordo com a configuração)
//  private boolean isFeatureFlagActive(final String flagName) {
//    Boolean isActive = featureFlags.get(flagName);
//    return isActive != null && isActive;
//  }
//
//  // Método auxiliar para pegar os tipos dos parâmetros de um método (necessário para refletir
// sobre
//  // os métodos)
//  private Class<?>[] getParameterTypes(final ProceedingJoinPoint pjp) {
//    return pjp.getArgs() != null ? new Class[] {pjp.getArgs().getClass()} : new Class[0];
//  }
// }
