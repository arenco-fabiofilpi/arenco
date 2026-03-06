package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.PasswordHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordHistoryServiceImpl implements PasswordHistoryService {

  //  private final PasswordHistoryRepository passwordHistoryRepository;
  //
  //  @Autowired
  //  protected PasswordHistoryServiceImpl(final PasswordHistoryRepository
  // passwordHistoryRepository) {
  //    this.passwordHistoryRepository = passwordHistoryRepository;
  //  }
  //
  //  @Override
  //  public void save(final User user, final String encodedPassword) {
  //    log.debug("Saving encodedPassword history for user: {}", user.getUsername());
  //
  //    final PasswordHistory passwordHistory = new PasswordHistory();
  //    passwordHistory.setUser(user);
  //    passwordHistory.setPassword(encodedPassword);
  //    passwordHistoryRepository.save(passwordHistory);
  //
  //    final List<PasswordHistory> passwordHistories = passwordHistoryRepository.findByUser(user);
  //
  //    if (passwordHistories.size() > 5) {
  //      final List<PasswordHistory> oldPasswords =
  //          passwordHistories.stream()
  //              .sorted(Comparator.comparing(ArencoItem::getCreatedAt))
  //              .limit((long) passwordHistories.size() - 5)
  //              .toList();
  //
  //      passwordHistoryRepository.deleteAll(oldPasswords);
  //      log.info("Deleted old password history for user: {}", user.getUsername());
  //    }
  //
  //    log.info("EncodedPassword history saved for user: {}", user.getUsername());
  //  }
}
