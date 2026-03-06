import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';

class AppBarWidget extends StatelessWidget {
  const AppBarWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: context.screenWidth() / 6),
      child: Image.asset(
        logoApp,
        fit: BoxFit.fitWidth,
      ),
    );
  }
}
