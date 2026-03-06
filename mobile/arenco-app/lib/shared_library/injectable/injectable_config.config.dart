// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// InjectableConfigGenerator
// **************************************************************************

// ignore_for_file: type=lint
// coverage:ignore-file

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:get_it/get_it.dart' as _i174;
import 'package:injectable/injectable.dart' as _i526;

import '../../presentation/account/account_detail/view_model/account_detail_view_model.dart'
    as _i828;
import '../../presentation/account/account_general/view_model/account_view_model.dart'
    as _i95;
import '../../presentation/finance/view_model/finance_view_model.dart' as _i330;
import '../../presentation/forgotten_password/insert_token/view_model/insert_token_view_model.dart'
    as _i333;
import '../../presentation/forgotten_password/insert_username/view_model/insert_username_view_model.dart'
    as _i598;
import '../../presentation/forgotten_password/reset_password/view_model/reset_password_view_model.dart'
    as _i687;
import '../../presentation/home/view_model/home_view_model.dart' as _i304;
import '../../presentation/login/view_model/login_view_model.dart' as _i296;

extension GetItInjectableX on _i174.GetIt {
// initializes the registration of main-scope dependencies inside of GetIt
  _i174.GetIt init({
    String? environment,
    _i526.EnvironmentFilter? environmentFilter,
  }) {
    final gh = _i526.GetItHelper(
      this,
      environment,
      environmentFilter,
    );
    gh.lazySingleton<_i304.HomeViewModel>(() => _i304.HomeViewModel());
    gh.lazySingleton<_i687.ResetPasswordViewModel>(
        () => _i687.ResetPasswordViewModel());
    gh.lazySingleton<_i598.InsertUsernameViewModel>(
        () => _i598.InsertUsernameViewModel());
    gh.lazySingleton<_i333.InsertTokenViewModel>(
        () => _i333.InsertTokenViewModel());
    gh.lazySingleton<_i330.FinanceViewModel>(() => _i330.FinanceViewModel());
    gh.lazySingleton<_i828.AccountDetailViewModel>(
        () => _i828.AccountDetailViewModel());
    gh.lazySingleton<_i95.AccountGeneralViewModel>(
        () => _i95.AccountGeneralViewModel());
    gh.lazySingleton<_i296.LoginViewModel>(() => _i296.LoginViewModel());
    return this;
  }
}
