#include <WiFi.h>
#include <FirebaseESP32.h>

#define WIFI_SSID "duende"
#define WIFI_PASSWORD "12345678"
#define FIREBASE_HOST "https://login-firebase-79de7-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "AIzaSyCzPcnEQYFJY90gERRQQ91f4L30pHvAApE"


FirebaseData firebaseData;
FirebaseAuth firebaseAuth;

const int input1 = 4;
const int input2 = 15;



void setup() {
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  pinMode(input1, OUTPUT);
  pinMode(input2, OUTPUT);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

bool previousValue = false;

void loop() {
  if (Firebase.getBool(firebaseData, "/acionamento")) {
    if (firebaseData.dataType() == "boolean") {
      bool valor = firebaseData.boolData();
      Serial.print("Valor lido do Firebase: ");
      Serial.println(valor);

      if (valor == false && previousValue == true) {
        digitalWrite(input1, HIGH);
        digitalWrite(input2, LOW);
        delay(1000);
      } else if (valor == true && previousValue == false) {
        digitalWrite(input1, LOW);
        digitalWrite(input2, HIGH);
        delay(1000);
      } else if (valor == true && previousValue == true) {
        digitalWrite(input1, LOW);
        digitalWrite(input2, LOW);
        delay(1000);
      } else if (valor == false && previousValue == false) {
        digitalWrite(input1, LOW);
        digitalWrite(input2, LOW);
        delay(1000);
      }
      previousValue = valor;
    }
  } else {
    Serial.println("Falha ao ler o dado do Firebase.");
    Serial.println(firebaseData.errorReason());
  }
  delay(1000);
}