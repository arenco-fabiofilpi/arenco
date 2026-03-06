import 'package:arenco_app/presentation/account/account_general/view/widget/widget.dart';
import 'package:arenco_app/presentation/home/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class AccountView extends StatelessWidget with Navigation {
  AccountView({super.key});
  final HomeViewModel _homeViewModel = GetIt.I.get<HomeViewModel>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBarComponent(
        name: "Conta",
        iconLeading: const Icon(
          Icons.arrow_back_rounded,
          color: Colors.black,
        ),
        onTapLeading: () {
          _homeViewModel.setCurrentIndexBar(0);
        },
      ),
      body: ListView(
        children: [
          const SizedBox(
            height: 32,
          ),
          CircleAvatarWidget(),
          const SizedBox(
            height: 24,
          ),
          NameWidget(),
          const SizedBox(
            height: 4,
          ),
          UsernameWidget(),
          const SizedBox(
            height: 24,
          ),
          GrayButtonWidget(
            navigateToPage: accountDetail,
            iconData: Icons.person,
            title: "Perfil",
          ),
          const SizedBox(
            height: 16,
          ),
          GrayButtonWidget(
            navigateToPage: accountDetail,
            iconData: Icons.settings,
            title: "Configurações",
          ),
          const SizedBox(
            height: 16,
          ),
          GrayButtonWidget(
            navigateToPage: accountDetail,
            iconData: Icons.email,
            title: "Contato",
          ),
          const SizedBox(
            height: 16,
          ),
          GrayButtonWidget(
            navigateToPage: accountDetail,
            iconData: Icons.share,
            title: "Compartilhar App",
          ),
          const SizedBox(
            height: 16,
          ),
          GrayButtonWidget(
            navigateToPage: accountDetail,
            iconData: Icons.help,
            title: "Ajuda",
          ),
        ],
      ),
    );
  }
}
