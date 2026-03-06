// ignore_for_file: prefer_const_constructors_in_immutables, must_be_immutable

import 'package:arenco_app/resources/resources.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';

class StringStrength extends StatelessWidget {
  StringStrength({
    super.key,
    required this.text,
    required this.isPassed,
  });
  final String text;
  bool isPassed;

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          decoration: BoxDecoration(
            color: isPassed ? ColorsTheme.greenCheckBox : Colors.white,
            border: Border.all(
              color: isPassed ? ColorsTheme.greenCheckBox : ColorsTheme.textColorGrey!,
            ),
            borderRadius: BorderRadius.circular(90),
          ),
          height: 20,
          width: 20,
          child: isPassed
              ? const Center(
                child: Icon(
                    Icons.check_rounded,
                    size: 15,
                    color: Colors.white,
                  ),
              )
              : null,
        ),
        const SizedBox(
          width: 12,
        ),
        Expanded(
          child: AutoSizeText(
            text,
            maxFontSize: 16,
            minFontSize: 14,
            style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                  color: Colors.black,
                ),
          ),
        ),
      ],
    );
  }
}
