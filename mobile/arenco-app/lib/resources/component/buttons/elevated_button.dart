import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';

// ignore: must_be_immutable
class ElevatedButtonComponent extends StatelessWidget with ColorsTheme {
  ElevatedButtonComponent({
    required this.loading,
    required this.onPressed,
    required this.textButton,
    this.colorButton,
    super.key,
  });
  bool loading;
  Function() onPressed;
  String textButton;
  Color? colorButton;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: () async {
          await onPressed();
        },
        style: ElevatedButton.styleFrom(
          backgroundColor: colorButton ?? Theme.of(context).primaryColor,
          elevation: 0,
          padding: EdgeInsets.zero,
          maximumSize: const Size(double.infinity, 56),
          minimumSize: const Size(double.infinity, 56),
        ),
        child: Container(
          margin: const EdgeInsets.symmetric(vertical: 8.0),
          child: loading
              ? SizedBox(
                height: 24,
                width: 24,
                child: CircularProgressIndicator(
                    valueColor:
                        AlwaysStoppedAnimation<Color>(ColorsTheme.textColorGrey!),
                    strokeWidth: 2.5,
                  ),
              )
              : AutoSizeText(
                  textButton,
                  maxFontSize: 16,
                  style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                        color: Colors.black,
                      ),
                ),
        ),
      ),
    );
  }
}
