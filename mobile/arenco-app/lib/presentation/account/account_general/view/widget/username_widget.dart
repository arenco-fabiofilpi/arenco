import 'package:arenco_app/presentation/account/account_general/view_model/view_model.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class UsernameWidget extends StatelessWidget {
  UsernameWidget({super.key});

  final _accountGeneralViewModel = GetIt.I.get<AccountGeneralViewModel>();

  @override
  Widget build(BuildContext context) {
    return Align(
      child: Text(
        _accountGeneralViewModel.username ?? "",
        style: Theme.of(context).textTheme.headlineSmall?.copyWith(
              color: const Color.fromRGBO(158, 158, 158, 1),
              fontSize: 14,
            ),
      ),
    );
  }
}
