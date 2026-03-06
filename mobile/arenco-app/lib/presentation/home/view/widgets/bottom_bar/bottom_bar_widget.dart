import 'package:arenco_app/presentation/home/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class BottomBarWidget extends StatelessWidget with ColorsTheme {
  BottomBarWidget({super.key});

  final _homeViewModel = GetIt.I.get<HomeViewModel>();

  @override
  Widget build(BuildContext context) {
    return NavigationBarTheme(
      data: NavigationBarThemeData(
        labelTextStyle: WidgetStateProperty.all(
          Theme.of(context).textTheme.headlineMedium?.copyWith(
                color: Colors.grey[850],
                fontSize: 14,
              ),
        ),
      ),
      child: Observer(
        builder: (context) {
          return NavigationBar(
            selectedIndex: _homeViewModel.currentIndexBar,
            onDestinationSelected: _homeViewModel.setCurrentIndexBar,
            backgroundColor: ColorsTheme.backgroundBottomNavigationBar,
            indicatorColor: ColorsTheme.buttonYellowColor,
            destinations: [
              NavigationDestination(
                icon: Icon(
                  Icons.home_filled,
                  color: Colors.grey[850],
                ),
                label: 'Início',
              ),
              NavigationDestination(
                icon: Icon(
                  Icons.account_balance,
                  color: Colors.grey[850],
                ),
                label: 'Financeiro',
              ),
              NavigationDestination(
                icon: Icon(
                  Icons.account_circle,
                  color: Colors.grey[850],
                ),
                label: 'Conta',
              ),
            ],
          );
        },
      ),
    );
  }
}
