import 'package:arenco_app/data/data.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view/widgets/widget.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class ChoseContactWidget extends StatelessWidget {
  ChoseContactWidget({super.key});

  final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Padding(
          padding: EdgeInsets.symmetric(
            horizontal: context.paddingPercentHorizontal(width: 22),
          ),
          child: AutoSizeText(
            "2. Como você gostaria de receber seu código de verificação",
            maxFontSize: 14,
            style: Theme.of(context)
                .textTheme
                .headlineSmall
                ?.copyWith(color: ColorsTheme.textColorBlack),
          ),
        ),
        Padding(
          padding: EdgeInsets.symmetric(
            horizontal: context.paddingPercentHorizontal(width: 22),
            vertical: context.paddingPercentHeight(heightPadding: 12),
          ),
          child: Column(
            children: [
              Observer(
                builder: (context) {
                  return ListView.builder(
                    shrinkWrap: true, // Constrains the height to the items
                    physics:
                        const NeverScrollableScrollPhysics(), // Prevents nested scrolling
                    itemCount: insertUsernameViewModel
                            .contactsPasswordListParams?.contacts?.length ??
                        0,
                    itemBuilder: (context, index) {
                      return Observer(
                        builder: (context) {
                          return Padding(
                            padding: const EdgeInsets.only(bottom: 8.0),
                            child: CheckBoxWidget(
                              text: insertUsernameViewModel
                                          .contactsPasswordListParams
                                          ?.contacts?[index]
                                          .contactMethod ==
                                      ContactMethod.cellphone
                                  ? "📱 ${formatPhoneNumber(insertUsernameViewModel.contactsPasswordListParams?.contacts?[index].contactValue ?? "")}"
                                  : "📧 ${insertUsernameViewModel.contactsPasswordListParams?.contacts?[index].contactValue ?? ""}",
                              checkBoxSelect: insertUsernameViewModel
                                          .contactsPasswordListParams
                                          ?.contacts?[index]
                                          .contactMethod ==
                                      ContactMethod.cellphone
                                  ? CheckBoxSelect.sms
                                  : CheckBoxSelect.email,
                              checkBoxError:
                                  insertUsernameViewModel.checkBoxError,
                              checkedBox: insertUsernameViewModel
                                      .contactsPasswordListParams!
                                      .contacts![index] ==
                                  insertUsernameViewModel
                                      .contactsPasswordSelected,
                              contactsPasswordParams: insertUsernameViewModel
                                  .contactsPasswordListParams!.contacts![index],
                            ),
                          );
                        },
                      );
                    },
                  );
                },
              ),
              const SizedBox(
                height: 6,
              ),
              Observer(
                builder: (context) => Align(
                  alignment: Alignment.centerLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(left: 16.0),
                    child: AutoSizeText(
                      insertUsernameViewModel.checkBoxError ?? "",
                      maxFontSize: 14,
                      style:
                          Theme.of(context).textTheme.headlineSmall?.copyWith(
                                color: Colors.red,
                              ),
                    ),
                  ),
                ),
              ),
              ButtonsWidget(
                showButtons: MediaQuery.of(context).viewInsets.bottom > 0,
                paddingButtons: false,
              ),
            ],
          ),
        ),
      ],
    );
  }
}
