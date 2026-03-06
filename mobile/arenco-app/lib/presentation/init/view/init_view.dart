import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';

class InitView extends StatefulWidget {
  const InitView({super.key});

  @override
  State<InitView> createState() => _InitViewState();
}

class _InitViewState extends State<InitView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBarComponent(
        iconAction: const Icon(
          Icons.search,
          color: Colors.black,
        ),
        iconLeading: Image.asset(
          "assets/button_icon/align-left.png",
          color: Colors.black,
        ),
        onTapAction: () {},
        onTapLeading: () {},
      ),
    );
  }
}
