import 'package:flutter/material.dart';

class FieldWidget extends StatelessWidget {
  const FieldWidget({
    required this.title,
    required this.value,
    super.key,
  });

  final String title;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
      margin: const EdgeInsets.symmetric(
        horizontal: 32,
      ),
      decoration: BoxDecoration(
        color: const Color.fromRGBO(248, 247, 247, 1),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: Theme.of(context)
                .textTheme
                .headlineLarge
                ?.copyWith(color: Colors.black, fontSize: 14),
          ),
          const SizedBox(
            height: 4,
          ),
          Text(
            value,
            style: Theme.of(context).textTheme.headlineLarge?.copyWith(
                  color: const Color.fromRGBO(155, 153, 153, 1),
                  fontSize: 14,
                ),
          ),
        ],
      ),
    );
  }
}
