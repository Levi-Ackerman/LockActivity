# 用Activity来实现的Android锁屏工具

## 功能

1. 亮屏自动启动锁屏

2. 锁屏界面屏蔽Home键,back键,recent键

3. 锁屏界面禁用通知栏下拉

4. 最近列表中排除锁屏Activity

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