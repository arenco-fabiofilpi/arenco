package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.LoginOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public interface LoginOtpModelService {
  LoginOtpModel createOneTimePassword(final ContactModel contactModel, final UserModel userModel);

  void validateOneTimePassword(final UserModel userModel, final String otp);

  void authenticateOneTimePassword(final UserModel userModel, final String otp);
}
