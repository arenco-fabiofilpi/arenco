import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';

mixin ThemeMain {
  static ThemeData themeDataMain() {
    return ThemeData(
      colorScheme: ColorScheme.fromSeed(
        seedColor: const Color(0xFF00b06f),
        primary: ColorsTheme.primaryColor,
        onPrimary: const Color(0xFF00b06f),
        surface: const Color(0XFFf3f3f3),
        onSurface: const Color(0XFFf3f3f3),
        onSecondary: const Color(0XFFf3f3f3),
      ),
      useMaterial3: true,
      fontFamily: fontFamily,
      platform: TargetPlatform.iOS,
      visualDensity: VisualDensity.adaptivePlatformDensity,
      pageTransitionsTheme: const PageTransitionsTheme(
        builders: {
          TargetPlatform.android: CupertinoPageTransitionsBuilder(),
          TargetPlatform.iOS: CupertinoPageTransitionsBuilder(),
        },
      ),
      textTheme: const TextTheme(
        displaySmall: TextStyle(
          fontWeight: FontWeight.w100,
        ),
        displayMedium: TextStyle(
          fontWeight: FontWeight.w200,
        ),
        displayLarge: TextStyle(
          fontWeight: FontWeight.w300,
        ),
        headlineSmall: TextStyle(
          fontWeight: FontWeight.w400,
        ),
        headlineMedium: TextStyle(
          fontWeight: FontWeight.w500,
        ),
        headlineLarge: TextStyle(
          fontWeight: FontWeight.w600,
        ),
        titleSmall: TextStyle(
          fontWeight: FontWeight.w700,
        ),
        titleMedium: TextStyle(
          fontWeight: FontWeight.w800,
        ),
        titleLarge: TextStyle(
          fontWeight: FontWeight.w900,
        ),
      ),
    );
  }
}
