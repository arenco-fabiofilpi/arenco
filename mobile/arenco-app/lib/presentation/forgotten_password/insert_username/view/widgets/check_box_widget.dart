// ignore_for_file: must_be_immutable

import 'package:arenco_app/domain/domain.dart';
import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/insert_username_view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';

class CheckBoxWidget extends StatelessWidget {
  CheckBoxWidget({
    required this.text,
    required this.checkBoxError,
    required this.checkBoxSelect,
    required this.checkedBox,
    required this.contactsPasswordParams,
    super.key,
  });
  final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();
  final String text;
  final ContactsPasswordParams contactsPasswordParams;
  bool checkedBox;
  String? checkBoxError;
  CheckBoxSelect checkBoxSelect;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        insertUsernameViewModel.selectedBox(
          contactsPasswordParams: contactsPasswordParams,
        );
      },
      child: Row(
        children: [
          Align(
            alignment: Alignment.centerLeft,
            child: Container(
              width: 30.0,
              height: 30.0,
              decoration: BoxDecoration(
                color: Colors.transparent,
                border: Border.all(
                  color: checkBoxError != null
                      ? Colors.red
                      : ColorsTheme.buttonYellowColor,
                  width: 2.0,
                ),
                shape: BoxShape.circle,
              ),
              child: checkedBox
                  ? Center(
                      child: Container(
                        width: 20.0,
                        height: 20.0,
                        decoration: BoxDecoration(
                          color: checkedBox
                              ? ColorsTheme.buttonYellowColor
                              : Colors.transparent,
                          shape: BoxShape.circle,
                        ),
                      ),
                    )
                  : null,
            ),
          ),
          const SizedBox(
            width: 6,
          ),
          Expanded(
            child: AutoSizeText(
              text,
              maxFontSize: 14,
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                    color: checkBoxError != null
                        ? Colors.red
                        : ColorsTheme.textColorBlack,
                  ),
            ),
          ),
        ],
      ),
    );
  }
}
