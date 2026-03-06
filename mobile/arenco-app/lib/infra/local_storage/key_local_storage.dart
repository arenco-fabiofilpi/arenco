mixin KeyLocalStorage {
  static String keyUsername() {
    return const String.fromEnvironment('username');
  }

  static String loginData() {
    return const String.fromEnvironment('Login_data');
  }
}
