package de.bitnoise.sonferenz;

public interface KonferenzDefines
{

  String REGEX_USERNAME = "[A-Za-z0-9_]{8,15}";

  String REGEX_USER_DISPLAY = "[A-Za-z0-9_ ]{3,23}";

  String PASSWORD_REGEX = ".{8,15}";
}
