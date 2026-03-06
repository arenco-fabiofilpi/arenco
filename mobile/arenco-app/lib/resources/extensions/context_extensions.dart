
import 'package:arenco_app/resources/resources.dart';
import 'package:flutter/material.dart';
import 'package:responsive_builder/responsive_builder.dart';

extension ScreenConfig on BuildContext {
  double screenWidth() {
    final width = MediaQuery.of(this).size.width;
    return width;
  }

  double screenHeight() {
    final height = MediaQuery.of(this).size.height;
    return height;
  }

  double screenHeightWithoutTop() {
    final height = MediaQuery.of(this).size.height - paddingTopStatusBar();
    return height;
  }

  double screenHeightWithoutBottom() {
    final height =
        MediaQuery.of(this).size.height - paddingBottomNavigationBar();
    return height;
  }

  double screenHeighWithoutTopAndBottom() {
    final height = MediaQuery.of(this).size.height -
        paddingTopStatusBar() -
        paddingBottomNavigationBar();
    return height;
  }

  double scaleFactor() {
    final factor = MediaQuery.textScalerOf(this).scale(1);
    return factor;
  }

  double paddingTopStatusBar() {
    final statusBar = MediaQuery.of(this).padding.top;
    return statusBar;
  }

  double paddingBottomNavigationBar() {
    final statusBar = MediaQuery.of(this).padding.bottom;
    return statusBar;
  }

  double screenHeightAppLayout() {
    final height = MediaQuery.of(this).size.height / appWidthLayout;
    return height;
  }

  double screenWidthAppLayout() {
    final width = MediaQuery.of(this).size.width / appWidthLayout;
    return width;
  }

  double paddingPercentHorizontal({required double width}) {
    return ((2 * width) / screenWidth()) * screenWidth();
  }

  double paddingPercentHeight({required double heightPadding}) {
    final height =
        heightPadding * MediaQuery.of(this).size.height / appWidthLayout;
    return height;
  }

  double paddingPercentWidth({required double widthPadding}) {
    final width =
        widthPadding * MediaQuery.of(this).size.width / appHeightLayout;
    return width;
  }

  bool isTablet() {
    return getDeviceType(MediaQuery.of(this).size) == DeviceScreenType.tablet;
  }

  bool isWeb() {
    return getDeviceType(MediaQuery.of(this).size) == DeviceScreenType.desktop;
  }

  bool isMobile() {
    return getDeviceType(MediaQuery.of(this).size) == DeviceScreenType.mobile;
  }
}
