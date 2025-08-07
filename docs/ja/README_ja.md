# Kaptcha - 柔軟で使いやすく高度に拡張可能なJava CAPTCHAライブラリ

[English](../../README.md) |
[日本語](README_ja.md) |
[中文](../zh/README_zh.md)

**Kaptcha** は軽量なJava CAPTCHA生成ライブラリで、ゼロから構築され、究極の柔軟性とシンプルなAPIを提供します。
すぐに使えるCAPTCHAソリューションが必要な場合でも、各ノイズ要素やテキストスタイルを深くカスタマイズしたい場合でも、Kaptchaはあらゆるニーズに対応できます。

![数学CAPTCHA例](https://file.mazepeng.com/math_captcha_1.png)
![数学CAPTCHA例](https://file.mazepeng.com/math_captcha_2.png)
![数学CAPTCHA例](https://file.mazepeng.com/math_captcha_3.png)
![算術CAPTCHA例](https://file.mazepeng.com/arithmetic_captcha.png)

---

## ✨ 特徴

- **簡単な使用**: チェーンメソッドの`Builder`パターンにより、数行のコードでCAPTCHAを生成可能。
- **高度な拡張性**: コンテンツ生成、ノイズ、フォント、色などすべてのコア機能はインターフェースで定義されており、独自ロジックを簡単に実装してデフォルトコンポーネントを置換可能。
- **豊富な組み込みコンポーネント**:
    - **コンテンツジェネレータ**: ランダム文字と四則演算をサポート。
    - **ノイズジェネレータ**: 直線、ベジェ曲線、ノイズ、波状歪みなど多様な効果を内蔵し、それらを組み合わせて使用可能。
    - **フォントサポート**: プロジェクトリソースから自動的に読み込み、美しいアートフォントをランダムに使用。
- **スレッドセーフ**: コアの「プロデューサー」パターン設計により、Webアプリケーションなどのマルチスレッド環境でも安全かつ効率的にCAPTCHAを生成。
- **ゼロ依存**: サードパーティライブラリ不要、標準Java JDKのみで動作。
- **多彩な出力形式**: `BufferedImage`への直接出力、ファイルストリームへの書き込み、Web使用向けの`Base64`文字列変換をサポート。

---

## 🚀 クイックスタート

### 1. 依存関係の追加

```xml
<dependency>
    <groupId>com.mazepeng</groupId>
    <artifactId>Kaptcha</artifactId>
    <version>0.0.2</version>
</dependency>
```

### 2. フォントファイルの準備 (オプション、但し推奨)

1. プロジェクトの`src/main/resources`ディレクトリに`fonts`フォルダを作成。
2. お好みの`.ttf`または`.otf`フォントファイルをこのフォルダに配置。
   *(Google FontsのKalam、Caveat、Press Start 2Pなどの無料商用フォントがおすすめ)*

### 3. 最初のCAPTCHAを生成

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

## 🎨 高度な使用方法

Kaptchaの真の強みは、その柔軟な設定とコンポーネント置換能力にあります。

### 算術CAPTCHAとベジェ曲線ノイズの使用

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

### 複数ノイズ効果の組み合わせ

複数のノイズジェネレータを組み合わせることで、より複雑なCAPTCHAを作成できます。

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

## 🛠️ 独自コンポーネントの拡張

独自のノイズ効果やテキスト生成ロジックを実装したいですか？非常に簡単です！対応するインターフェースを実装するだけです。

### 例: 円を描画するノイズジェネレータの作成

1. **`IInterferer` インターフェースの実装**
    ```java
    public class CircleInterferer implements IInterferer {
        private int count;
        public CircleInterferer(int count) { this.count = count; }

        @Override
        public void draw(Graphics2D g, int width, int height) {
            // ... ここにN個のランダムな円を描画するロジックを実装 ...
        }
    }
    ```
2. **ビルド時に使用**
    ```java
    CaptchaProducer circleProducer = new CaptchaProducer.Builder()
        .interferer(new CircleInterferer(15)) // カスタム干渉器を使用
        .build();
    ```

---

## 📦 API 概要

### コアクラス

- **`CaptchaProducer`**: スレッドセーフで再利用可能なCAPTCHAプロデューサ。`Builder`で作成。
    - `nextCaptcha()`: 新しい`Captcha`インスタンスを生成。
- **`Captcha`**: 個々のCAPTCHAインスタンスを表す。
    - `getText()`: CAPTCHAの解答を取得。
    - `getImage()`: `BufferedImage`画像を取得。
    - `toBase64()`: Base64形式の画像文字列を取得。
    - `writeTo(OutputStream)`: 画像を出力ストリームに書き込み。

### コアインターフェース (`com.mazepeng.captcha.api`)

- **`IGenerator`**: コンテンツジェネレータ（文字、算術式など）。
- **`IInterferer`**: 干渉器（線、ノイズなど）。
- **`IFontProvider`**: フォントプロバイダ。
- **`IColorizer`**: カラーセレクタ。

### 組み込みコンポーネント

#### ジェネレータ

- **`ArithmeticGenerator`**: 算術式ジェネレータ。
- **`CharGenerator`**: 文字ジェネレータ。

#### 干渉器

- **`LineInterferer`**: 直線干渉器。
- **`WarpInterferer`**: 波状干渉器。
- **`BesselCurveInterferer`**: ベジェ曲線干渉器。
- **`NoiseInterferer`**: ノイズ干渉器。
- **`CompositeInterferer`**: 複合干渉器。複数の干渉器を組み合わせる。

#### フォントプロバイダ

- **`DefaultFontProvider`**: デフォルトフォントプロバイダ。
- **`RandomFontProvider`**: ランダムフォントプロバイダ。

#### カラーセレクタ

- **`RandomColorizer`**: ランダムカラー選択ツール。

---

## 📜 ライセンス

本プロジェクトは [MIT License](LICENSE.txt) ライセンスの下で公開されています。