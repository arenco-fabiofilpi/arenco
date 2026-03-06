// ignore_for_file: must_be_immutable

import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';

class TextButtonComponent extends StatelessWidget {
  TextButtonComponent({
    required this.onPressed,
    required this.title,
    this.colorText = Colors.black,
    this.textBold = false,
    this.padding = 0,
    this.loading = false,
    super.key,
  });
  Function() onPressed;
  final String title;
  Color colorText;
  bool textBold;
  double padding;
  bool loading;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: () => onPressed(),
      child: Padding(
        padding: EdgeInsets.symmetric(vertical: padding),
        child: loading
            ? SizedBox(
                height: 20,
                width: 20,
                child: CircularProgressIndicator(
                  valueColor:
                      AlwaysStoppedAnimation<Color>(ColorsTheme.textColorGrey!),
                  strokeWidth: 2.5,
                ),
              )
            : AutoSizeText(
                title,
                maxFontSize: 14,
                style: textBold
                    ? Theme.of(context).textTheme.headlineLarge?.copyWith(
                          fontSize: context.isTablet() ? 14 : 16,
                          color: colorText,
                        )
                    : Theme.of(context).textTheme.headlineMedium?.copyWith(
                          fontSize: context.isTablet() ? 14 : 16,
                          color: colorText,
                        ),
              ),
      ),
    );
  }
}
