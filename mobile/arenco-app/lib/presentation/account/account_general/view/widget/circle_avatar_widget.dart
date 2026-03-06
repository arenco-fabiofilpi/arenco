import 'package:arenco_app/presentation/account/account_general/view_model/view_model.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class CircleAvatarWidget extends StatelessWidget {
  CircleAvatarWidget({super.key});

  final AccountGeneralViewModel _accountGeneralViewModel =
      GetIt.I.get<AccountGeneralViewModel>();

  @override
  Widget build(BuildContext context) {
    return Observer(
      builder: (context) {
        return CircleAvatar(
          radius: 50,
          backgroundColor: Colors.transparent,
          child: Image.asset(
            _accountGeneralViewModel.imageUrl ??
                "assets/button_icon/account.png",
          ),
        );
      },
    );
  }
}
