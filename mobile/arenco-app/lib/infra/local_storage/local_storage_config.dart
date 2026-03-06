import 'package:arenco_app/domain/domain.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class LocalStorageService implements LocalStorageServiceInterface {
  @override
  Future<void> create({required String key, required String value}) async {
    const storage = FlutterSecureStorage();
    await storage.write(key: key, value: value);
  }

  @override
  Future<void> delete({required String key}) async {
    const storage = FlutterSecureStorage();
    await storage.delete(key: key);
  }

  @override
  Future<String> read({required String key}) async {
    const storage = FlutterSecureStorage();
    final String value = (await storage.read(key: key)) ?? '';
    return value;
  }

  @override
  Future<void> cleanAll() async {
    await const FlutterSecureStorage().deleteAll();
  }

  @override
  Future<void> update({required String key, required String newValue}) async {
    delete(key: key);
    create(key: key, value: newValue);
  }
}
