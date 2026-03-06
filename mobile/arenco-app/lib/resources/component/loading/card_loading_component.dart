import 'package:flutter/material.dart';

class CardLoadingComponent extends StatelessWidget {
  final double? sizeWidth;
  final double? sizeHeight;
  const CardLoadingComponent({
    super.key,
    this.sizeWidth,
    this.sizeHeight,
  });

  @override
  Widget build(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    return Container(
      decoration: BoxDecoration(
        color: Colors.black,
        border: Border.all(),
        borderRadius: const BorderRadius.all(
          Radius.circular(8),
        ),
      ),
      width: sizeWidth! * width / 375,
      height: sizeHeight! * width / 375,
    );
  }
}
