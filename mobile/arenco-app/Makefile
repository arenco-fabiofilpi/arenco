clean_github:
	@echo "╠ Clean branchs github"
	@git fetch --all --prune
	@git branch | grep -v "master" | grep -v "develop" | xargs git branch -D 

flutter_config_native_splash:
	@echo "╠ Config native splash"
	@dart run flutter_native_splash:create --path=flutter_native_splash.yaml
	
flutter_config_icon:
	@echo "╠ Config icon app"
	@fvm dart run flutter_launcher_icons 

flutter_config:
	@echo "╠ Config fvm flutter"
	@fvm use
	@fvm flutter clean
	@fvm flutter pub get
	@fvm flutter pub run build_runner build --delete-conflicting-outputs

flutter_config_completed:
	@echo "╠ Config fvm flutter"
	@fvm use
	@fvm flutter clean
	@fvm flutter pub get
	@fvm flutter pub cache repair 
	@fvm flutter clean
	@fvm flutter pub get
	@fvm flutter pub run build_runner build --delete-conflicting-outputs

flutter_watch:
	@echo "╠ Build ruuner watch"
	@fvm dart run build_runner watch

flutter_config_ios:
	@echo "╠ Config ios cocoapods"
	@cd ios && pod deintegrate
	@cd ios && pod install --repo-update
	@cd ios && pod install

flutter_run_completed:
	@flutter clean
	@flutter pub get
	@flutter pub cache repair 
	@flutter clean
	@flutter pub get
	@cd ios && pod deintegrate
	@cd ios && pod install --repo-update
	@cd ios && pod install
	@flutter pub run build_runner build --delete-conflicting-outputs
	@flutter run --release --dart-define-from-file=".env.prod"

flutter_run_release:
	@echo "╠ Flutter run release"
	@fvm flutter run --release --dart-define-from-file=".env.prod"

flutter_run_debug:
	@echo "╠ Flutter run debug"
	@fvm flutter run --dart-define-from-file=".env.prod"

flutter_appbundle:
	@echo "╠ Flutter appbundle"
	@fvm flutter build appbundle --dart-define-from-file=".env.prod"

flutter_appbundle_staging:
	@echo "╠ Flutter appbundle"
	@fvm flutter build appbundle --dart-define-from-file=".env.staging"

flutter_apk:
	@echo "╠ Flutter APK"
	@fvm flutter pub get
	@fvm flutter build apk --dart-define-from-file=".env.prod"

flutter_apk_staging:
	@echo "╠ Flutter APK"
	@fvm flutter pub get
	@fvm flutter build apk --dart-define-from-file=".env.staging"

flutter_run_release_staging:
	@echo "╠ Flutter run release"
	@fvm flutter run --release --dart-define-from-file=".env.staging"

flutter_run_debug_staging:
	@echo "╠ Flutter run debug"
	@fvm flutter run --release --dart-define-from-file=".env.staging"

flutter_build_ios: 
	@echo "╠ Flutter build ios"
	@fvm flutter build ios --dart-define-from-file=".env.prod"
	@echo "╠ Open xcode and click in Product->Archive"