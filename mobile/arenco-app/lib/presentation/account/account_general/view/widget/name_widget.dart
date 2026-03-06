import 'package:arenco_app/presentation/account/account_general/view_model/view_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class NameWidget extends StatelessWidget {
  NameWidget({super.key});

  final AccountGeneralViewModel _accountGeneralViewModel =
      GetIt.I.get<AccountGeneralViewModel>();

  @override
  Widget build(BuildContext context) {
    return Observer(
      builder: (context) {
        return Align(
          child: Text(
            _accountGeneralViewModel.name ?? "",
            style: Theme.of(context)
                .textTheme
                .headlineLarge
                ?.copyWith(color: Colors.black, fontSize: 20),
          ),
        );
      },
    );
  }
}
