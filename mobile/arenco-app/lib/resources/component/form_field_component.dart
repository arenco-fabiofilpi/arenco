// ignore_for_file: no_logic_in_create_state, parameter_assignments

import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FormFieldComponent extends StatefulWidget {
  final String label;
  final TextInputType? inputType;
  final TextStyle? textStyleWhenFocused;
  final TextStyle? textStyleWhenUnFocused;
  final TextStyle? floatingLabelTextStyle;
  final InputDecoration? inputDecoration;
  final bool? obscureText;
  final Widget? suffixIcon;
  final Widget? prefixIcon;
  final String? hintText;
  final TextStyle? hintTextStyle;
  final void Function()? onTap;
  final void Function(String)? onSubmitted;
  final void Function()? onEditingComplete;
  final void Function(String)? onChange;
  final TextInputAction textInputAction;
  final TextInputFormatter? inputFormatter;
  final bool? readOnly;
  final bool? autoFocus;
  final int? maxLength;
  final List<TextInputFormatter>? inputFormatterList;
  final String? errorText;

  const FormFieldComponent({
    super.key,
    required this.label,
    this.inputType = TextInputType.visiblePassword,
    this.textStyleWhenFocused,
    this.textStyleWhenUnFocused,
    this.floatingLabelTextStyle,
    this.inputDecoration,
    this.obscureText = false,
    this.suffixIcon,
    this.prefixIcon,
    this.hintText,
    this.hintTextStyle,
    this.onTap,
    this.onEditingComplete,
    this.onSubmitted,
    this.onChange,
    this.textInputAction = TextInputAction.done,
    this.inputFormatter,
    this.readOnly,
    this.autoFocus,
    this.maxLength,
    this.inputFormatterList,
    this.errorText,
  });

  @override
  State<FormFieldComponent> createState() => _FormFieldComponentState();
}

class _FormFieldComponentState extends State<FormFieldComponent> {
  FocusNode? focusNode;

  @override
  void initState() {
    focusNode = FocusNode();
    focusNode?.addListener(() {
      setState(() {});
    });
    super.initState();
  }

  @override
  void dispose() {
    focusNode?.removeListener(() {});
    focusNode?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return TextField(
      readOnly: widget.readOnly ?? false,
      obscureText: widget.obscureText ?? false,
      maxLength: widget.maxLength,
      inputFormatters: widget.inputFormatterList ??
          [
            widget.inputFormatter ?? FilteringTextInputFormatter.deny(RegExp('')),
          ],

      onEditingComplete: widget.onEditingComplete,
      onChanged: widget.onChange,
      onTap: widget.onTap,
      onSubmitted: widget.onSubmitted,
      autofocus: widget.autoFocus ?? false,
      focusNode: focusNode,
      keyboardType: widget.inputType,
      textAlignVertical: TextAlignVertical.center,
      textInputAction: widget.textInputAction,
      style: Theme.of(context).textTheme.headlineSmall?.copyWith(
            fontSize: context.isTablet() ? 14 : 16,
            color: Colors.black,
          ),
          
      decoration: widget.inputDecoration ??
          InputDecoration(
            contentPadding: const EdgeInsets.symmetric(vertical: 20, horizontal: 16),
            hintText: widget.hintText,
            hintStyle: widget.hintTextStyle,
            suffixIcon: widget.suffixIcon,
            prefixIcon: widget.prefixIcon,
            errorText: widget.errorText,
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(90),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(90),
              borderSide:  BorderSide(
                color: Theme.of(context).primaryColor,
                width: 3,
              ),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(90),
              borderSide:  BorderSide(
                color: Theme.of(context).primaryColor,
                width: 3,
              ),
            ),
            floatingLabelBehavior: FloatingLabelBehavior.auto,
            labelText: focusNode != null && focusNode!.hasFocus ? widget.label : null,
            floatingLabelStyle: widget.floatingLabelTextStyle ??
                const TextStyle(
                  // fontSize: 16,
                  color: Colors.black,
                  fontWeight: FontWeight.w400,
                ),
            suffixIconColor: Colors.red,
            focusedErrorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(90),
              borderSide:  const BorderSide(
                color: Colors.red,
                width: 2,
              ),
            ),
          ),
    );
  }
}

class CpfCnpjFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(
    TextEditingValue oldValue,
    TextEditingValue newValue,
  ) {
    final text = newValue.text;

    final digitsOnly = text.replaceAll(RegExp(r'\D'), '');

    if (digitsOnly.length <= 11) {
      final formattedText = _applyCpfMask(digitsOnly);
      return newValue.copyWith(
        text: formattedText,
        selection: TextSelection.collapsed(offset: formattedText.length),
      );
    } else if (digitsOnly.length <= 14) {
      final formattedText = _applyCnpjMask(digitsOnly);
      return newValue.copyWith(
        text: formattedText,
        selection: TextSelection.collapsed(offset: formattedText.length),
      );
    } else {
      final formattedText = _applyCnpjMask(digitsOnly.substring(0, 14));
      return newValue.copyWith(
        text: formattedText,
        selection: TextSelection.collapsed(offset: formattedText.length),
      );
    }
  }

  String _applyCpfMask(String digits) {
    if (digits.length > 11) digits = digits.substring(0, 11);
    return digits.replaceAllMapped(
      RegExp(r'(\d{3})(\d{3})(\d{3})(\d{2})'),
      (match) => '${match[1]}.${match[2]}.${match[3]}-${match[4]}',
    );
  }

  String _applyCnpjMask(String digits) {
    if (digits.length > 14) digits = digits.substring(0, 14);
    return digits.replaceAllMapped(
      RegExp(r'(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})'),
      (match) => '${match[1]}.${match[2]}.${match[3]}/${match[4]}-${match[5]}',
    );
  }
}
