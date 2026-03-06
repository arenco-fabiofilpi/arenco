// ignore_for_file: avoid_redundant_argument_values

import 'package:flutter/material.dart';

class LoadingComponent extends StatefulWidget {
  const LoadingComponent({required this.child, super.key});

  final Widget child;
  @override
  _LoadingComponentState createState() => _LoadingComponentState();
}

class _LoadingComponentState extends State<LoadingComponent> with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController.unbounded(vsync: this)
      ..repeat(min: -0.5, max: 1.5, period: const Duration(milliseconds: 1500))
      ..addListener(() {
        setState(() {});
      });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final shimmerBox = context.findRenderObject() as RenderBox?;
    if (shimmerBox == null) {
      return const SizedBox();
    }

    final offsetWithinShimmer = shimmerBox.localToGlobal(Offset.zero, ancestor: shimmerBox);

    final shimmerRect = Rect.fromLTWH(
      offsetWithinShimmer.dx,
      offsetWithinShimmer.dy,
      shimmerBox.size.width,
      shimmerBox.size.height,
    );

    return ShaderMask(
      blendMode: BlendMode.srcATop,
      shaderCallback: (bounds) {
        final gradient = LinearGradient(
          colors: const [
            Color(0xFFEBEBF4),
            Color(0xFFF4F4F4),
            Color(0xFFEBEBF4),
          ],
          stops: const [
            0.1,
            0.3,
            0.4,
          ],
          transform: _SlidingGradientTransform(_controller.value),
          begin: const Alignment(-1, -0.3),
          end: const Alignment(1, 0.3),
          tileMode: TileMode.clamp,
        );
        return gradient.createShader(shimmerRect);
      },
      child: widget.child,
    );
  }
}

class _SlidingGradientTransform extends GradientTransform {
  const _SlidingGradientTransform(this.slidePercent);

  final double slidePercent;

  @override
  Matrix4? transform(Rect bounds, {TextDirection? textDirection}) => Matrix4.translationValues(bounds.width * slidePercent, 0, 0);
}
