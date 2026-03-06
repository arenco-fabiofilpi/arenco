// ignore_for_file: must_be_immutable

import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FormFieldResetPassword extends StatefulWidget {
  FormFieldResetPassword({
    required this.label,
    this.prefixIcon,
    this.suffixIcon,
    required this.onChanged,
    this.onSubmitted,
    this.errorText,
    this.obscureText = false,
    this.textInputAction = TextInputAction.done,
    this.inputFormatter,
    this.inputFormatterList,
    this.inputType = TextInputType.emailAddress,
    super.key,
  });
  String label;
  IconData? prefixIcon;
  Widget? suffixIcon;
  Function(String) onChanged;
  Function(String)? onSubmitted;
  String? errorText;
  bool? obscureText;
  TextInputAction textInputAction;
  TextInputFormatter? inputFormatter;
  List<TextInputFormatter>? inputFormatterList;
  TextInputType? inputType;

  @override
  State<FormFieldResetPassword> createState() => _FormFieldResetPasswordState();
}

class _FormFieldResetPasswordState extends State<FormFieldResetPassword> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        AutoSizeText(
          widget.label,
          maxFontSize: 16,
          style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                fontSize: context.isTablet() ? 16 : 20,
                color: Colors.black,
              ),
        ),
        const SizedBox(
          height: 4,
        ),
        FormFieldComponent(
          label: "",
          inputType: widget.inputType, 
          prefixIcon: widget.prefixIcon == null
              ? null
              : Padding(
                  padding: const EdgeInsets.only(left: 20, right: 8),
                  child: Icon(
                    widget.prefixIcon,
                    color: Colors.grey[600],
                  ),
                ),
          suffixIcon: widget.suffixIcon,
          obscureText: widget.obscureText,
          onChange: (value) {
            widget.onChanged(value);
          },
          inputFormatter: widget.inputFormatter,
          onSubmitted: widget.onSubmitted,
          textInputAction: widget.textInputAction,
          errorText: widget.errorText,
          inputFormatterList: widget.inputFormatterList,
        ),
      ],
    );
  }
}
