# **Wumpus World - Android Oyunu**

Bu proje, klasik Wumpus World oyununun Android Jetpack Compose kullanılarak oluşturulmuş bir versiyonudur. Amacınız, altını bulup oyunu kazanmaktır. Ancak, Wumpus ve çukurlar gibi tehlikelerden kaçınmanız gerekmektedir.

---

## **APK Dosyasını İndirin ve Kurun**
Eğer projeyi derlemekle uğraşmak istemiyorsanız, oyunun hazır APK dosyasını indirip cihazınıza kurabilirsiniz:

1. [GitHub Releases](https://github.com/ismailfatihbirak/WumpusWorld/releases) sayfasına gidin.
2. En son sürümün altında yer alan **Assets** kısmında, APK dosyasını bulun.
3. APK dosyasını indirip cihazınıza kurun. Kurulum sırasında bilinmeyen kaynaklardan uygulama yükleme izni vermeniz gerekebilir.

---

## **Özellikler**
- 4x4 ızgara tabanlı oyun alanı
- Tehlike uyarıları: Esinti (çukur) ve koku (Wumpus)
- Altın toplama ve oyunu tamamlama
- Basit ve sezgisel bir arayüz

---

## **Gereksinimler**
Bu projeyi çalıştırmak için aşağıdaki araçlara ihtiyacınız var:
- **Android Studio**: LadyBug 2024.2.1 patch 3 veya daha üzeri bir versiyon
- **Java 11 veya üstü** kurulu
- **Bir Android emülatörü** veya **gerçek bir Android cihaz**

---

## **Kurulum ve Çalıştırma**

### 1. **Android Studio'yu İndir ve Kur**
Eğer daha önce Android Studio kullanmadıysanız:
1. [Android Studio'yu buradan indirin](https://developer.android.com/studio).
2. İndirme işlemi tamamlandıktan sonra talimatları izleyerek kurulum işlemini gerçekleştirin.

### 2. **Projeyi GitHub'dan İndir**
1. [Projenin GitHub sayfasına gidin](https://github.com/ismailfatihbirak/WumpusWorld).
2. Sağ üstteki **Code** butonuna tıklayın.
3. **Download ZIP** seçeneğiyle projeyi indirin ve bir klasöre çıkarın.
   - Alternatif olarak, Git kullanıyorsanız terminalde şu komutu çalıştırabilirsiniz:
     ```bash
     git clone https://github.com/ismailfatihbirak/WumpusWorld.git
     ```

### 3. **Projeyi Android Studio'da Aç**
1. Android Studio'yu açın.
2. **"Open"** seçeneğine tıklayın ve projeyi indirdiğiniz klasöre giderek seçin.
3. Proje yüklendikten sonra birkaç dakika içinde Android Studio gerekli dosyaları indirecektir.

### 4. **Bir Cihaz Ayarla**
1. **Emülatör Kullanmak İsterseniz**:
   - Android Studio’da üst menüden **Device Manager**’a gidin.
   - Yeni bir sanal cihaz (emülatör) oluşturun. (Örneğin, Pixel 5, Android 12)
2. **Gerçek Cihaz Kullanmak İsterseniz**:
   - Android cihazınızı USB ile bilgisayarınıza bağlayın.
   - Telefonunuzda **USB Hata Ayıklama Modu**nu etkinleştirin.

### 5. **Projeyi Derle ve Çalıştır**
1. Android Studio’da üstteki yeşil **"Run"** butonuna tıklayın.
2. Uygulama, seçtiğiniz cihazda (emülatör veya gerçek cihaz) çalıştırılacaktır.

---

## **Nasıl Oynanır?**
1. Uygulama açıldığında 4x4 bir oyun ızgarası göreceksiniz.
2. Amacınız:
   - Altını bulup toplamak.
   - Tehlikelerden (Wumpus ve çukurlar) kaçmak.
   - Başlangıç pozisyonuna dönmek.
3. Hücrelere dokunarak hareket edin.
4. Eğer bir çukur veya Wumpus yakınındaysanız uyarılar alırsınız:
   - **Esinti (Breeze)**: Yakında bir çukur var.
   - **Koku (Stench)**: Yakında bir Wumpus var.

---

## **Sorun Giderme**
- **Proje derlenmiyor**: Android Studio'nun güncel olduğundan emin olun. Gerekirse **File > Invalidate Caches / Restart** seçeneğini kullanın.
- **Emülatör çalışmıyor**: Bilgisayarınızın sanallaştırma ayarlarını kontrol edin ve etkinleştirin.
- **Gerçek cihaz algılanmıyor**: USB hata ayıklama modunu etkinleştirdiğinizden emin olun.
