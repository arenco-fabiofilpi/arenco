import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';

class LoadingContactsWidget extends StatelessWidget {
  const LoadingContactsWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(
        horizontal: context.paddingPercentHorizontal(width: 22),
      ),
      child: LoadingComponent(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            CardLoadingComponent(
              sizeWidth: context.screenWidth(),
              sizeHeight: 18,
            ),
            const SizedBox(
              height: 6,
            ),
            CardLoadingComponent(
              sizeWidth: context.screenWidth() - 150,
              sizeHeight: 18,
            ),
            SizedBox(
              height: context.paddingPercentHeight(heightPadding: 12),
            ),
            CardLoadingComponent(
              sizeWidth: context.screenWidth() / 2.2,
              sizeHeight: 18,
            ),
             const SizedBox(
              height: 6,
            ),
            CardLoadingComponent(
              sizeWidth: context.screenWidth() / 2.1,
              sizeHeight: 18,
            ),

          ],
        ),
      ),
    );
  }
}
