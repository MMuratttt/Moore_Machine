# 🧠 Moore Machine

Bu proje, **Moore Durum Makinesi** (Moore State Machine) mantığına dayanan, Java Swing ile geliştirilmiş grafiksel bir simülasyon uygulamasıdır.  
Moore makinelerinde her durumda sabit bir çıktı üretilir ve çıktı yalnızca mevcut duruma bağlıdır.

## 🚀 Özellikler

- Moore makinesi ile durum geçişlerini simüle etme
- Girdi dizisine göre çıktı üretimi
- Grafiksel arayüz ile kullanıcı dostu kullanım
- Geçiş ve çıktı tablolarını elle düzenleme
- Durumlar arası geçişleri görsel olarak çizme

---

## 📥 Uygulamayı İndir

En güncel `.jar` dosyasını indirmek için aşağıdaki bağlantıyı kullanabilirsiniz:

🔗 [**Releases sayfasına git**](https://github.com/MMuratttt/Moore_Machine/releases)

### 💻 Çalıştırmak için:

```bash
java -jar Moore_Machine.jar
```
**Not: Bilgisayarınızda Java 8 veya üzeri bir sürüm yüklü olmalıdır.**


## 🛠️ Kurulum (Geliştiriciler İçin)
Projeyi kendi bilgisayarınızda derlemek ve çalıştırmak için:
```bash
git clone https://github.com/MMuratttt/Moore_Machine.git
cd Moore_Machine
```
Ardından IntelliJ IDEA veya benzeri bir Java IDE ile projeyi açarak çalıştırabilirsiniz. 

Main class:
```bash
org.training.MooreSimulatorGUI
```
## 🧪 Örnek Kullanım
Uygulamada aşağıdaki bilgileri girerek bir Moore makinesi tanımlayabilirsiniz:

**Durumlar:**
```bash
q0,q1,q2,q3
```
**Girdi alfabesi:**
```bash
a,b
```
**Çıktı alfabesi:**
```bash
0,1
```
**Geçiş Tablosu:**

```bash
q0    q1    q0
q1    q2    q0
q2    q2    q3
q3    q1    q0
```

**Çıktı Tablosu:**
```bash
q0    1
q1    0
q2    0
q3    1
```
**Girdi Dizisi:**
```bash
abab
```
## 📸 Ekran Görüntüsü

![Moore Machine Simülatörü](screenshots/gui_preview.png)

