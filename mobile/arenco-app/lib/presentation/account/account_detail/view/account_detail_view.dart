import 'package:arenco_app/presentation/account/account_detail/view/widget/widget.dart';
import 'package:arenco_app/presentation/account/account_detail/view_model/view_model.dart';
import 'package:arenco_app/presentation/account/account_general/view/widget/widget.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class AccountDetailView extends StatelessWidget with Navigation {
  AccountDetailView({super.key});

  final AccountDetailViewModel _accountDetailViewModel =
      GetIt.I.get<AccountDetailViewModel>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBarComponent(
        name: "Perfil",
        iconLeading: const Icon(
          Icons.arrow_back_rounded,
          color: Colors.black,
        ),
        onTapLeading: () {
          navigateBack(context);
        },
      ),
      body: ListView(
        children: [
          const SizedBox(
            height: 32,
          ),
          CircleAvatarWidget(),
          const SizedBox(
            height: 48,
          ),
          Observer(
            builder: (context) {
              return FieldWidget(
                title: "ID",
                value: _accountDetailViewModel.id ?? "",
              );
            },
          ),
          const SizedBox(
            height: 16,
          ),
          Observer(
            builder: (context) {
              return FieldWidget(
                title: "Nome",
                value: _accountDetailViewModel.name ?? "",
              );
            },
          ),
          const SizedBox(
            height: 16,
          ),
          Observer(
            builder: (context) {
              return FieldWidget(
                title: "E-mail",
                value: _accountDetailViewModel.email ?? "",
              );
            },
          ),
          const SizedBox(
            height: 16,
          ),
          Observer(
            builder: (context) {
              return FieldWidget(
                title: "Telefone",
                value: _accountDetailViewModel.phoneNumber ?? "",
              );
            },
          ),
        ],
      ),
    );
  }
}
