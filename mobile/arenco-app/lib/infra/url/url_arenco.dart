mixin URLLaunch {
  static String baseURLArenco(){
    return const String.fromEnvironment('API_URL');
  }

  static String baseURLArencoFinance(){
    return const String.fromEnvironment('API_URL_FINANCE');
  }
}
