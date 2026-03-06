package br.com.arenco.dataseeder.entities;

import br.com.arenco.dataseeder.enums.LoginMethod;
import br.com.arenco.dataseeder.enums.UserState;
import br.com.arenco.dataseeder.enums.UserType;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
public class UserModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private int idErp;
  private String grupoContabil;
  private String cnpj;
  private String cpf;

  private String name;
  private String username;
  private String password;
  private boolean enabled = true;
  private UserState state = UserState.ACTIVE;
  private LoginMethod loginMethod = LoginMethod.USERNAME_AND_PASSWORD;
  private UserType userType;
}
