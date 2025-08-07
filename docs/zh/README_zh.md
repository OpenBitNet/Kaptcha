
# Kaptcha - 一个灵活、易用且可高度扩展的Java验证码库

[English](../../README.md) |
[日本語](../ja/README_ja.md) |
[中文](README_zh.md)

**Kaptcha** 是一个轻量级的Java验证码生成库，它从零开始构建，旨在提供极致的灵活性和简单的API。
无论您是需要一个开箱即用的验证码解决方案，还是希望深度定制每一个干扰元素和文字样式，Kaptcha 都能满足您的需求。

![数学验证码示例](https://file.mazepeng.com/math_captcha_1.png)
![数学验证码示例](https://file.mazepeng.com/math_captcha_2.png)
![数学验证码示例](https://file.mazepeng.com/math_captcha_3.png)
![数学验证码示例](https://file.mazepeng.com/arithmetic_captcha.png)


---

## ✨ 特性

- **简单易用**: 通过链式调用的 `Builder` 模式，只需几行代码即可生成验证码。
- **高度可扩展**: 所有核心功能（如内容生成、干扰、字体、颜色）都通过接口定义，您可以轻松实现自己的逻辑并替换默认组件。
- **丰富的内置组件**:
    - **内容生成器**: 支持随机字符和四则运算。
    - **干扰器**: 内置直线、贝塞尔曲线、噪点和波浪扭曲等多种干扰效果，并支持将它们组合使用。
    - **字体支持**: 自动从项目资源中加载并随机使用漂亮的艺术字体。
- **线程安全**: 核心的“生产者”模式设计，确保在多线程环境（如Web应用）中安全、高效地生成验证码。
- **零依赖**: 无需任何第三方库，仅依赖标准Java JDK。
- **多种输出格式**: 支持直接输出为 `BufferedImage`、写入文件流，或转换为 `Base64` 字符串以便在Web中使用。

---

## 🚀 快速上手

### 1. 添加依赖 
```xml
<dependency>
    <groupId>com.mazepeng</groupId>
    <artifactId>Kaptcha</artifactId>
    <version>0.0.2</version>
</dependency>
```

### 2. 准备字体文件 (可选，但推荐)
1. 在您的项目 `src/main/resources` 目录下创建一个 `fonts` 文件夹。
2. 将您喜欢的 `.ttf` 或 `.otf` 字体文件放入该文件夹。
   *(推荐一些免费商用字体如 Google Fonts 的 Kalam, Caveat, Press Start 2P 等)*

### 3. 生成您的第一个验证码

```java
import com.mazepng.captcha.Captcha;
import com.mazpeng.captcha.CaptchaProducer;
import java.io.FileOutputStream;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        // 1. 创建一个验证码生产者，它持有所有配置，可复用
        CaptchaProducer producer = new CaptchaProducer.Builder()
                .width(150)
                .height(50)
                .build();

        // 2. 使用生产者生成一个具体的验证码实例
        Captcha captcha = producer.nextCaptcha();

        // 3. 获取答案和图像
        String answer = captcha.getText();
        String base64Image = captcha.toBase64();
        
        System.out.println("验证码答案: " + answer);
        System.out.println("Base64 图像: " + base64Image);

        // 4. (可选) 保存到文件以便查看
        try (FileOutputStream fos = new FileOutputStream("my_first_captcha.png")) {
            captcha.writeTo(fos);
        }
    }
}
```

---

## 🎨 高级用法

Kaptcha 的强大之处在于其灵活的配置和组件替换能力。

### 使用算术验证码和贝塞尔曲线干扰

```java
import com.mazepeng.captcha.generator.ArithmeticGenerator;
import com.mazpeng.captcha.interferer.BesselCurveInterferer;

// 创建一个算术题 + 贝塞尔曲线干扰的生产者
CaptchaProducer mathProducer = new CaptchaProducer.Builder()
        .width(160)
        .height(60)
        .generator(new ArithmeticGenerator())      // 使用算术生成器
        .interferer(new BesselCurveInterferer(3)) // 使用3条贝塞尔曲线干扰
        .fontSize(38)                           // 设置字体大小
        .backgroundColor(new Color(245, 245, 245)) // 设置背景色
        .build();

// 反复生成新的算术验证码
Captcha mathCaptcha1 = mathProducer.nextCaptcha();
Captcha mathCaptcha2 = mathProducer.nextCaptcha();
```

### 组合多种干扰效果

您可以将多个干扰器组合在一起，创造出更复杂的验证码。

```java
import com.mazepeng.captcha.interferer.*;

// 创建一个复合干扰器
IInterferer compositeInterferer = new CompositeInterferer(
    new LineInterferer(5),          // 5条直线
    new WarpInterferer(),           // 默认的波浪扭曲
    new NoiseInterferer(0.6f)       // 密度为0.6的噪点
);

CaptchaProducer complexProducer = new CaptchaProducer.Builder()
        .width(200)
        .height(70)
        .interferer(compositeInterferer) // 使用复合干扰器
        .build();

Captcha complexCaptcha = complexProducer.nextCaptcha();
```

---

## 🛠️ 扩展您自己的组件

想实现自己的干扰效果或文字生成逻辑？非常简单！只需要实现对应的接口即可。

### 示例：创建一个画圆圈的干扰器

1.  **实现 `IInterferer` 接口**
    ```java
    public class CircleInterferer implements IInterferer {
        private int count;
        public CircleInterferer(int count) { this.count = count; }

        @Override
        public void draw(Graphics2D g, int width, int height) {
            // ... 在这里实现画N个随机圆圈的逻辑 ...
        }
    }
    ```
2.  **在构建时使用它**
    ```java
    CaptchaProducer circleProducer = new CaptchaProducer.Builder()
        .interferer(new CircleInterferer(15)) // 使用我们自己的干扰器
        .build();
    ```

---

## 📦 API 概览

### 核心类
- **`CaptchaProducer`**: 验证码生产者，线程安全，可复用。通过 `Builder` 创建。
    - `nextCaptcha()`: 生成一个新的 `Captcha` 实例。
- **`Captcha`**: 代表一个具体的验证码实例。
    - `getText()`: 获取验证码答案。
    - `getImage()`: 获取 `BufferedImage` 图像。
    - `toBase64()`: 获取 Base64 格式的图像字符串。
    - `writeTo(OutputStream)`: 将图像写入输出流。

### 核心接口 (`com.mazepeng.captcha.api`)
- **`IGenerator`**: 内容生成器（如字符、算术）。
- **`IInterferer`**: 干扰器（如线条、噪点）。
- **`IFontProvider`**: 字体提供器。
- **`IColorizer`**: 颜色选择器。

### 内置组件

#### 生成器
- **`ArithmeticGenerator`**: 算术题生成器。
- **`CharGenerator`**: 字符生成器。

#### 干扰器
- **`LineInterferer`**: 直线干扰器。
- **`WarpInterferer`**: 波浪干扰器。
- **`BesselCurveInterferer`**: 贝塞尔曲线干扰器。
- **`NoiseInterferer`**: 噪点干扰器。
- **`CompositeInterferer`**: 复合干扰器，将多个干扰器组合在一起。

#### 字体提供器
- **`DefaultFontProvider`**: 默认字体提供器。
- **`RandomFontProvider`**: 随机字体提供器。

#### 颜色选择器
- **`RandomColorizer`**: 随机颜色选择器。

---

## 📜 许可

本项目采用 [MIT License](LICENSE.txt) 许可。