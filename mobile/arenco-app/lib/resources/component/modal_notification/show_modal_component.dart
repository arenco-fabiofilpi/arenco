import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/shared_library.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';

class ShowModalComponent with Navigation, ColorsTheme {
  final String? title;
  final String? subtitle;
  final String? titleButton;
  final Function()? onPressed;
  final Widget? editingSubtitle;

  ShowModalComponent({
    this.title,
    this.subtitle,
    this.titleButton,
    this.onPressed,
    this.editingSubtitle,
  });
  void modalBottomSheet(BuildContext context) {
    final double width = MediaQuery.of(context).size.width;
    showModalBottomSheet<void>(
      backgroundColor: Colors.white,
      context: context,
      isScrollControlled: true,
      elevation: 0,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(8),
          topRight: Radius.circular(8),
        ),
      ),
      builder: (BuildContext context) {
        return ListView(
          shrinkWrap: true,
          children: [
            Padding(
              padding: const EdgeInsets.only(
                top: 10,
                left: 24,
                right: 24,
                bottom: 16,
              ),
              child: Wrap(
                children: [
                  Center(
                    child: Container(
                      width: 53 * width / 375,
                      height: 4,
                      decoration: BoxDecoration(
                        color: ColorsTheme.primaryColor,
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                  ),
                  const SizedBox(height: 27),
                  AutoSizeText(
                    title ?? '',
                    style: TextStyle(
                      fontSize: 22 * width / 375,
                      fontWeight: FontWeight.w400,
                      color: Colors.grey[700],
                    ),
                    maxFontSize: 22,
                    minFontSize: 16,
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 16, bottom: 36),
                    child: editingSubtitle ??
                        AutoSizeText(
                          subtitle ?? '',
                          style: TextStyle(
                            fontSize: 16 * width / 375,
                            color: Colors.grey[600],
                            fontWeight: FontWeight.w400,
                          ),
                          maxFontSize: 16,
                          minFontSize: 10,
                        ),
                  ),
                  ElevatedButtonComponent(
                    loading: false,
                    onPressed: () {
                      navigateBack(context);
                    },
                    textButton: titleButton ?? "",
                  ),
                  const SizedBox(
                    height: 8,
                  ),
                ],
              ),
            ),
          ],
        );
      },
    );
  }
}
