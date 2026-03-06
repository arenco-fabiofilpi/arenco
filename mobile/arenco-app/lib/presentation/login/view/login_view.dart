// ignore_for_file: prefer_const_constructors, parameter_assignments

import 'package:arenco_app/presentation/forgotten_password/insert_username/view_model/view_model.dart';
import 'package:arenco_app/presentation/login/view/widget/widget.dart';
import 'package:arenco_app/presentation/login/view_model/view_model.dart';
import 'package:arenco_app/resources/resources.dart';
import 'package:arenco_app/shared_library/routes/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:get_it/get_it.dart';

class LoginView extends StatefulWidget {
  const LoginView({super.key});

  @override
  State<LoginView> createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> with Navigation {
  final _loginViewModel = GetIt.I.get<LoginViewModel>();
  final insertUsernameViewModel = GetIt.I.get<InsertUsernameViewModel>();

  Future<void> _sendForm(BuildContext context) async {
    if (!_loginViewModel.loadingSendButton) {
      await _loginViewModel.sendForm(context: context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: ListView(
        children: [
          SizedBox(
            height: context.paddingPercentHeight(
              heightPadding: 32,
            ),
          ),
          const AppBarWidget(),
          SizedBox(
            height: context.paddingPercentHeight(heightPadding: 40),
          ),
          Padding(
            padding: EdgeInsets.symmetric(
              horizontal: context.paddingPercentHorizontal(width: 24.0),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Observer(
                  builder: (context) {
                    return FormFieldWidget(
                      label: user,
                      prefixIcon: Icons.person,
                      onChanged: _loginViewModel.onChangedUsername,
                      errorText: _loginViewModel.usernameError,
                      textInputAction: TextInputAction.next,
                      inputFormatterList: [CpfCnpjFormatter()],
                      inputType: TextInputType.numberWithOptions(
                        signed: true,
                        decimal: true,
                      ),
                    );
                  },
                ),
                SizedBox(
                  height: context.paddingPercentHeight(heightPadding: 12),
                ),
                Observer(
                  builder: (context) {
                    return FormFieldWidget(
                      label: password,
                      onChanged: _loginViewModel.onChangedPassword,
                      errorText: _loginViewModel.passwordError,
                      obscureText: _loginViewModel.obscureText,
                      onSubmitted: (value) async {
                        await _sendForm(context);
                      },
                      prefixIcon: Icons.lock,
                      suffixIcon: IconButton(
                        highlightColor: const Color.fromARGB(0, 73, 71, 71),
                        onPressed: () async {
                          _loginViewModel.changeObscure();
                        },
                        icon: Padding(
                          padding: const EdgeInsets.only(right: 8, left: 8),
                          child: Icon(
                            _loginViewModel.obscureText
                                ? Icons.remove_red_eye_outlined
                                : Icons.visibility_off_outlined,
                            color: Colors.grey[600],
                          ),
                        ),
                      ),
                    );
                  },
                ),
                Align(
                  alignment: Alignment.centerRight,
                  child: TextButtonComponent(
                    title: forgetPassword,
                    onPressed: () {
                      insertUsernameViewModel.initData(
                        usernameByLogin: _loginViewModel.username ?? "",
                      );
                      navigateTo(
                        context,
                        page: forgottenPassword,
                      );
                    },
                  ),
                ),
                SizedBox(
                  height: context.paddingPercentHeight(heightPadding: 20),
                ),
                Observer(
                  builder: (context) {
                    return ElevatedButtonComponent(
                      loading: _loginViewModel.loadingSendButton,
                      onPressed: () async {
                        await _sendForm(context);
                      },
                      textButton: enter,
                    );
                  },
                ),
                SizedBox(
                  height: context.paddingPercentHeight(heightPadding: 8),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
