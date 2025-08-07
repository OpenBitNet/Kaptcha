# Kaptcha - A Flexible, Easy-to-Use, and Highly Extensible Java CAPTCHA Library


<!-- Keep these links. Translations will automatically update with the README. -->
[English](README.md) |
[日本語](docs/ja/) |
[中文](docs/zh/)

**Kaptcha** is a lightweight Java CAPTCHA generation library built from scratch, designed to provide ultimate flexibility and a simple API. 
Whether you need an out-of-the-box CAPTCHA solution or want to deeply customize every distortion element and text style, Kaptcha can meet your requirements.

![Math CAPTCHA Example](https://file.mazepeng.com/math_captcha_1.png)
![Math CAPTCHA Example](https://file.mazepeng.com/math_captcha_2.png)
![Math CAPTCHA Example](https://file.mazepeng.com/math_captcha_3.png)
![Math CAPTCHA Example](https://file.mazepeng.com/arithmetic_captcha.png)

---

## ✨ Features

- **Easy to Use**: Generate CAPTCHAs with just a few lines of code through the chain-calling `Builder` pattern.
- **Highly Extensible**: All core functionalities (such as content generation, distortion, fonts, colors) are defined via interfaces, allowing you to easily implement your own logic and replace default components.
- **Rich Built-in Components**:
    - **Content Generators**: Support random characters and arithmetic operations.
    - **Distortions**: Built-in effects like straight lines, Bézier curves, noise, and wave distortions, with support for combining them.
    - **Font Support**: Automatically loads and randomly uses beautiful artistic fonts from project resources.
- **Thread-Safe**: Core "producer" pattern design ensures safe and efficient CAPTCHA generation in multi-threaded environments (e.g., web applications).
- **Zero Dependencies**: No third-party libraries required, only relies on the standard Java JDK.
- **Multiple Output Formats**: Supports direct output as `BufferedImage`, writing to file streams, or converting to `Base64` strings for web use.

---

## 🚀 Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.mazepeng</groupId>
    <artifactId>Kaptcha</artifactId>
    <version>0.0.2</version>
</dependency>
```

### 2. Prepare Font Files (Optional but Recommended)

1. Create a `fonts` folder in your project's `src/main/resources` directory.
2. Place your preferred `.ttf` or `.otf` font files in this folder.
   *(Recommended free commercial fonts include Google Fonts' Kalam, Caveat, Press Start 2P, etc.)*

### 3. Generate Your First CAPTCHA

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

## 🎨 Advanced Usage

The power of Kaptcha lies in its flexible configuration and component replacement capabilities.

### Using Arithmetic CAPTCHA with Bézier Curve Distortion

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

### Combining Multiple Distortion Effects

You can combine multiple distorters to create more complex CAPTCHAs.

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

## 🛠️ Extending Your Own Components

Want to implement your own distortion effects or text generation logic? It's simple! Just implement the corresponding interface.

### Example: Creating a Circle-Drawing Distorter

1. **Implement the `IInterferer` Interface**
    ```java
    public class CircleInterferer implements IInterferer {
        private int count;
        public CircleInterferer(int count) { this.count = count; }

        @Override
        public void draw(Graphics2D g, int width, int height) {
            // ... Implement the logic to draw N random circles here ...
        }
    }
    ```
2. **Use It During Construction**
    ```java
    CaptchaProducer circleProducer = new CaptchaProducer.Builder()
        .interferer(new CircleInterferer(15)) // Use our custom interferer
        .build();
    ```

---

## 📦 API Overview

### Core Classes

- **`CaptchaProducer`**: A thread-safe, reusable captcha producer. Created via `Builder`.
    - `nextCaptcha()`: Generates a new `Captcha` instance.
- **`Captcha`**: Represents a specific captcha instance.
    - `getText()`: Retrieves the captcha answer.
    - `getImage()`: Retrieves the `BufferedImage` of the captcha.
    - `toBase64()`: Retrieves the Base64-encoded image string.
    - `writeTo(OutputStream)`: Writes the image to an output stream.

### Core Interfaces (`com.mazepeng.captcha.api`)

- **`IGenerator`**: Content generator (e.g., characters, arithmetic).
- **`IInterferer`**: Interferer (e.g., lines, noise).
- **`IFontProvider`**: Font provider.
- **`IColorizer`**: Color selector.

### Built-in Components

#### Generators

- **`ArithmeticGenerator`**: Arithmetic problem generator.
- **`CharGenerator`**: Character generator.

#### Interferers

- **`LineInterferer`**: Line interferer.
- **`WarpInterferer`**: Wave interferer.
- **`BesselCurveInterferer`**: Bézier curve interferer.
- **`NoiseInterferer`**: Noise interferer.
- **`CompositeInterferer`**: Composite interferer that combines multiple interferers.

#### Font Providers

- **`DefaultFontProvider`**: Default font provider.
- **`RandomFontProvider`**: Random font provider.

#### Color Selectors

- **`RandomColorizer`**: Random color selector.

---

## 📜 License

This project is licensed under the [MIT License](LICENSE.txt).