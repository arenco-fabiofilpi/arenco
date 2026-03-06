import 'package:arenco_app/presentation/finance/view_model/view_mode.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';
import 'package:flutter_json_view/flutter_json_view.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class FinanceView extends StatefulWidget {
  const FinanceView({super.key});

  @override
  State<FinanceView> createState() => _FinanceViewState();
}

class _FinanceViewState extends State<FinanceView> with Navigation {
  final financeViewModel = GetIt.I.get<FinanceViewModel>();

  Future<void> getFinanceList() async {
    financeViewModel.getSecuritiesReceivableFinance();
  }

  @override
  void initState() {
    getFinanceList();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBarComponent(
        name: "Financeiro",
        iconLeading: const Icon(
          Icons.arrow_back_rounded,
          color: Colors.black,
        ),
        onTapLeading: () {
          navigateBack(context);
        },
      ),
      body: RefreshIndicator(
        onRefresh: () async {
          await getFinanceList();
        },
        child: Observer(
          builder: (context) {
            return ListView(
              children: [
                if (financeViewModel.loading == true)
                  const Center(child: CircularProgressIndicator())
                else
                  financeViewModel.erroMessage != null &&
                          financeViewModel.erroMessage != ""
                      ? Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Center(
                              child: Text(
                                financeViewModel.erroMessage ?? "",
                                style: Theme.of(context)
                                    .textTheme
                                    .titleMedium
                                    ?.copyWith(
                                      color: Colors.black,
                                      fontSize: 18,
                                    ),
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Center(
                                child: SelectableText(
                                  "\n URL:  ${financeViewModel.baseUrl}",
                                  style: Theme.of(context)
                                      .textTheme
                                      .titleMedium
                                      ?.copyWith(
                                        color: Colors.black,
                                        fontSize: 18,
                                      ),
                                ),
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Center(
                                child: SelectableText(
                                  "\n token:  ${financeViewModel.token}",
                                  style: Theme.of(context)
                                      .textTheme
                                      .titleMedium
                                      ?.copyWith(
                                        color: Colors.black,
                                        fontSize: 18,
                                      ),
                                ),
                              ),
                            ),
                          ],
                        )
                      : financeViewModel.jsonView == null ||
                              financeViewModel.jsonView == ""
                          ? Container()
                          : JsonView.string(
                              financeViewModel.jsonView!,
                              theme: const JsonViewTheme(
                                backgroundColor: Colors.white,
                                keyStyle: TextStyle(
                                  color: Colors.black,
                                  fontSize: 16,
                                  fontWeight: FontWeight.w600,
                                ),
                                doubleStyle: TextStyle(
                                  color: Colors.green,
                                  fontSize: 16,
                                ),
                                intStyle: const TextStyle(
                                  color: Colors.green,
                                  fontSize: 16,
                                ),
                                stringStyle: TextStyle(
                                  color: Colors.green,
                                  fontSize: 16,
                                ),
                                boolStyle: TextStyle(
                                  color: Colors.green,
                                  fontSize: 16,
                                ),
                                closeIcon: Icon(
                                  Icons.close,
                                  color: Colors.green,
                                  size: 20,
                                ),
                                openIcon: Icon(
                                  Icons.add,
                                  color: Colors.green,
                                  size: 20,
                                ),
                                separator: Padding(
                                  padding:
                                      EdgeInsets.symmetric(horizontal: 8.0),
                                  child: Icon(
                                    Icons.arrow_right_alt_outlined,
                                    size: 20,
                                    color: Colors.green,
                                  ),
                                ),
                              ),
                            ),
              ],
            );
          },
        ),
      ),
    );
  }
}
