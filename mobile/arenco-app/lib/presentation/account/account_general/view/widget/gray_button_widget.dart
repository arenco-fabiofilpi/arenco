import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:flutter/material.dart';

class GrayButtonWidget extends StatelessWidget with Navigation {
  GrayButtonWidget({
    required this.navigateToPage,
    required this.title,
    required this.iconData,
    super.key,
  });

  final String navigateToPage;
  final String title;
  final IconData iconData;

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(
        horizontal: 32,
      ),
      child: ElevatedButton(
        onPressed: () {
          navigateTo(context, page: navigateToPage);
        },
        style: ElevatedButton.styleFrom(
          elevation: 0,
          backgroundColor: const Color.fromRGBO(248, 247, 247, 1),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          padding: const EdgeInsets.symmetric(vertical: 12),
        ),
        child: Row(
          children: [
            const SizedBox(
              width: 16,
            ),
            Icon(
              iconData,
              color: Colors.black,
              size: 32,
            ),
            const SizedBox(
              width: 16,
            ),
            Text(
              title,
              style: Theme.of(context)
                  .textTheme
                  .headlineLarge
                  ?.copyWith(color: Colors.black, fontSize: 14),
            ),
            const Expanded(
              child: Align(
                alignment: Alignment.centerRight,
                child: Icon(
                  Icons.arrow_forward_ios_rounded,
                  color: Colors.black,
                ),
              ),
            ),
            const SizedBox(
              width: 16,
            ),
          ],
        ),
      ),
    );
  }
}
