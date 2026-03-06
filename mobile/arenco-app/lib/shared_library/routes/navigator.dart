import 'package:flutter/material.dart';

mixin class Navigation {
  void navigateBack(BuildContext context) {
    Navigator.of(context).pop();
  }

  void navigateTo(
    BuildContext context, {
    required String page,
    dynamic arguments,
    VoidCallback? thenFunction,
  }) {
    Navigator.of(context).pushNamed(page, arguments: arguments).then((value) {
      if(thenFunction != null){
        thenFunction();
      }
    });
  }

  void popAndNavigate(BuildContext context, {required String page, VoidCallback? thenFunction}) {
    Navigator.of(context).popAndPushNamed(page).then((value) {
      if(thenFunction != null){
         thenFunction();
      }
    });
  }

  void pushReplacement(
    BuildContext context, {
    required String page,
    dynamic arguments,
    VoidCallback? thenFunction,
  }) {
    Navigator.of(context).pushReplacementNamed(page, arguments: arguments).then((value){
      if(thenFunction != null){
        thenFunction();
      }
    });
  }

  void popUntilFirstPage(BuildContext context) {
    Navigator.of(context).popUntil((route) => route.isFirst);
  }

  void popUntilPage(BuildContext context, {required String page}) {
    Navigator.of(context).popUntil((routes) {
      if (routes.settings.name != page) return false;
      return true;
    });
  }

  void popUntil({
    required BuildContext context,
    required int backPageUntil,
  }) {
    var backToPage = 0;
    Navigator.of(context).popUntil(
      (route) => backToPage++ == backPageUntil,
    );
  }
}
