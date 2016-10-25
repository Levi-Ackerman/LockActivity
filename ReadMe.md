# 用Activity来实现的Android锁屏工具

## 功能

目前市面上大部分锁屏应用都是用悬浮窗实现，而不用Activity。因为用Activity实现的锁屏应用，很大的问题就是Activity能被各种办法关闭或者绕过，所以本项目参考了一些前人的经验，也反编了一些现有锁屏应用的包，最后终于基本解决了所有能绕过Activity锁屏的场景，让Activity实现的锁屏也能安安全全的挡在屏幕前。

1. 亮屏自动启动锁屏Activity

2. 锁屏界面屏蔽Home键,back键,recent键，防止将Activity退到后台

3. 锁屏界面禁用通知栏下拉，防止点击通知跳到第三方应用，锁屏被绕过

4. 最近列表中排除锁屏Activity，防止锁屏Activity在不正常的场景出现

## 设置说明

1. 请先设置"我的锁屏"为默认的Launcher程序(桌面应用),才可以正常使用所有功能
2. 第三方应用无权限禁用系统的锁屏,所以如果设置了密码锁,会出现双重锁屏情况,测试时请先禁用系统锁屏
3. 来电和闹铃等场景会自动解除锁屏,但是来电和闹铃亮屏后,过程中按电源键关闭屏幕,再打开,锁屏界面会出现在来电或者闹铃界面之上,造成覆盖,需要另做特殊处理

## 测试包

debug测试包[点击下载](http://www.lizhengxian.top/LockActivity/debug_apk/app-debug.apk)

## License

    Copyright (C) 2016 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
