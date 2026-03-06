import 'package:arenco_app/shared_library/routes/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AppBarComponent extends StatelessWidget
    with Navigation
    implements PreferredSizeWidget {
  const AppBarComponent({
    this.name = "",
    this.onTapAction,
    this.iconAction,
    this.iconLeading,
    this.onTapLeading,
    this.setIconLeadingBackPage = false,
    super.key,
  });

  final String? name;
  final Function()? onTapAction;
  final Widget? iconAction;
  final Function()? onTapLeading;
  final Widget? iconLeading;
  final bool setIconLeadingBackPage;

  @override
  Widget build(BuildContext context) {
    return AppBar(
      surfaceTintColor: Colors.transparent,
      title: name == ""
          ? null
          : Text(
              name!,
              style: Theme.of(context).textTheme.titleSmall?.copyWith(
                    color: Colors.black,
                    fontSize: 18,
                  ),
            ),
      systemOverlayStyle: SystemUiOverlayStyle.dark,
      backgroundColor: Colors.transparent,
      elevation: 0,
      actions: iconAction == null
          ? null
          : [
              GestureDetector(
                onTap: () {
                  onTapAction?.call();
                },
                child: Container(
                  padding: const EdgeInsets.all(8),
                  margin: const EdgeInsets.only(right: 24),
                  decoration: const BoxDecoration(
                    color: Color.fromRGBO(245, 245, 245, 1),
                    shape: BoxShape.circle,
                  ),
                  child: iconAction,
                ),
              ),
            ],
      leadingWidth: 62,
      leading: GestureDetector(
        onTap: () {
          if (setIconLeadingBackPage) {
            navigateBack(context);
          } else {
            onTapLeading?.call();
          }
        },
        child: Container(
          padding: const EdgeInsets.all(8),
          margin: const EdgeInsets.only(left: 24),
          decoration: const BoxDecoration(
            color: Color.fromRGBO(245, 245, 245, 1),
            shape: BoxShape.circle,
          ),
          child: setIconLeadingBackPage
              ? const Icon(
                  Icons.arrow_back_rounded,
                  color: Colors.black,
                )
              : iconLeading,
        ),
      ),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
