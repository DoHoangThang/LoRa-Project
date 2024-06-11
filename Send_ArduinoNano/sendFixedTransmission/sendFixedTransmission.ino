#define MAC      1  
 
#include "LoRa_E32.h"
LoRa_E32 e32ttl(3, 2);

#include "DHT.h"
#define DHTPIN 6     
#define DHTTYPE DHT11   
DHT dht(DHTPIN, DHTTYPE);

int measurePin = A0;
int ledPower = 13;
int gasPin = A1; 
int gasValue = 0;

float temptValue = 0, humiValue = 0;
float dustValue = 0;

String packet;
float voMeasured = 0;
float calcVoltage = 0;
float dustDensity = 0;

void setup()
{
  Serial.begin(9600);
  dht.begin();
  e32ttl.begin();
  pinMode(ledPower, OUTPUT);
}

void loop()
{
  readValueDHT11();
  Serial.println();
  readValueDust();
  Serial.println();
  readValueGas();
  Serial.println();

  if (runEvery(6000)) 
  {
    packet = packet + String(MAC) + "," + String(temptValue) + "," + String(humiValue) 
    + "," + String(dustDensity) + "," + String(gasValue);
    ResponseStatus rs = e32ttl.sendFixedMessage(0, 6, 0x09, packet);
    packet = "";
    //"1,34,79,23,40,2,34,79,23,40"
  }
  delay(500);
}

boolean runEvery(unsigned long interval)
{
  static unsigned long previousMillis = 0;
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval)
  {
    previousMillis = currentMillis;
    return true;
  }
  return false;
}

void readValueDHT11()
{
   humiValue = dht.readHumidity();
   temptValue = dht.readTemperature();
  //  Serial.print("humiValue:    ");
  //  Serial.print(humiValue);
  //  Serial.print("temptValue:    ");
  //  Serial.print(temptValue);
}
void readValueGas()
{
    gasValue = analogRead(gasPin);
    Serial.print("gasValue:    ");
    Serial.print(gasValue);
}
void readValueDust()
{
  digitalWrite(ledPower,LOW);
  delayMicroseconds(280);

  voMeasured = analogRead(measurePin);

  delayMicroseconds(40);
  digitalWrite(ledPower,HIGH);
  delayMicroseconds(9680);

  calcVoltage = voMeasured*(5.0/1024.0);
  dustDensity = 170/4*calcVoltage - 0.1;

  if ( dustDensity < 0)
  {
    dustDensity = 0.00;
  }

  Serial.println("Raw Signal Value (0-1023):");
  Serial.println(voMeasured);

  Serial.println("Voltage:");
  Serial.println(calcVoltage);

  Serial.println("Dust Density:");
  Serial.println(dustDensity);
}