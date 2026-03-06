abstract class LocalStorageServiceInterface {
  Future<void> create({required String key, required String value});

  Future<void> delete({required String key});

  Future<String> read({required String key});

  Future<void> cleanAll();

  Future<void> update({required String key, required String newValue});
}
