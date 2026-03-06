// ignore_for_file: must_be_immutable

import 'package:arenco_app/resources/component/password_strength/widget/widget.dart';
import 'package:flutter/material.dart';

class PasswordStrengthComponent extends StatelessWidget {
  final String password;
  Map<String, RegExp> checkPasswordList;

  PasswordStrengthComponent({
    super.key,
    required this.password,
    required this.checkPasswordList,
  });

  double _calculateStrength(String password) {
    double strength = 0;

    if (password.isEmpty) return 0;

    for (final checkPassword in checkPasswordList.entries) {
      if (checkPassword.value.hasMatch(password)) strength += 0.25;
    }

    return strength;
  }


  @override
  Widget build(BuildContext context) {
    final double strength = _calculateStrength(password);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(90),
          child: LinearProgressIndicator(
            value: strength,
            backgroundColor: Colors.grey[300],
            color: strength <= 0.25
                ? Colors.red
                : strength <= 0.5
                    ? Colors.orange
                    : strength <= 0.75
                        ? Colors.yellow
                        : Colors.green,
            minHeight: 10,
          ),
        ),
        const SizedBox(
          height: 12,
        ),
        for (final checkPassword in checkPasswordList.entries)
          Column(
            children: [
              StringStrength(
                text: checkPassword.key,
                isPassed: checkPassword.value.hasMatch(password),
              ),
              const SizedBox(
                height: 8,
              ),
            ],
          ),
      ],
    );
  }
}
