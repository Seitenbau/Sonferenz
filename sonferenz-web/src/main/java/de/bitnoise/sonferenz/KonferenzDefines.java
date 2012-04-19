package de.bitnoise.sonferenz;

public interface KonferenzDefines
{

  String REGEX_USERNAME = "[A-Za-z0-9_\\.\\-@]{3,42}";
  
  String REGEX_USER_DISPLAY = "[A-Za-z0-9_\\-\\.\\s@]{3,23}";

  String PASSWORD_REGEX = ".{8,23}";
}
