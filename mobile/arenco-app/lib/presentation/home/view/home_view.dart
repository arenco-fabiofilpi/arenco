import 'package:arenco_app/presentation/home/view/widgets/widget.dart';
import 'package:arenco_app/presentation/home/view_model/view_model.dart';
import 'package:arenco_app/presentation/presentation.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class HomeView extends StatefulWidget with ColorsTheme {
  const HomeView({super.key});

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  final _homeViewModel = GetIt.I.get<HomeViewModel>();

  static final List<Widget> _pagesHomeView = <Widget>[
    const InitView(),
    FinanceView(),
    AccountView(),
  ];

  @override
  void initState() {
    _homeViewModel.setCurrentIndexBar(0);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Observer(
        builder: (context) {
          return Center(
            child: _pagesHomeView.elementAt(_homeViewModel.currentIndexBar),
          );
        },
      ),
      backgroundColor: Colors.white,
      bottomNavigationBar: BottomBarWidget(),
    );
  }
}
