package de.bitnoise.sonferenz.service.v2.services;


import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;

public interface AuthenticationService
{
  UserModel getCurrentUser() throws GeneralConferenceException;

  UserModel getCurrentUserOrFail();

  void queryUser(String usernameOrEmail);
}
