#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <config.h>
#include <U8g2lib.h>


//edit the config.h header which is in the same folder as the code and fill the values there







//function declarations / function prototypes
void wifiConnect();
void screenDraw(float, float, float);

void setup() {

  Serial.begin(115200);
  delay(1000);

  u8g2.begin();  //the library used to control the display

  wifiConnect(); //connecting to wifi

  if(verbose){
    Serial.print("esp32 ip is : ");
    Serial.println(WiFi.localIP());
  }


}

void loop() {

  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(serverURL);
    http.setTimeout(10000);

    //getting the response of the webServer
    int response = http.GET();

    if(response > 0){

        Serial.println("Data pulled successfully");


      //getting the content / payload
      String content = http.getString(); 

      if(verbose){
      Serial.print("data : ");
      Serial.println(content);
      }

      //deserialization starts here
      StaticJsonDocument<256> doc; 

      DeserializationError err = deserializeJson(doc, content);
      if(err){
        Serial.print("deserialization failed : ");
        Serial.println(err.c_str());
        return;
      }

      float memory = doc["memoryUsage"];
      float cUsage = doc["cpuUsage"];
      float cTemp = doc["cpuTemp"];

      screenDraw(memory, cUsage, cTemp); //drawing values on screen
      
      if(verbose){
      Serial.println("\nFinal values : ");

      Serial.print("Memory Usage: ");
      Serial.print(memory);
      Serial.println("%");

      Serial.print("CPU Usage: ");
      Serial.print(cUsage);
      Serial.println("%");

      Serial.print("CPU Temp: ");
      Serial.print(cTemp);
      Serial.println(" C");
      }

    } else { //response < 0
      Serial.print("Http error code : ");
      Serial.println(response);
    }
    http.end();

  } else { //if not connected basically wifi disconncted
    Serial.println("WIFI DISCONNECTED!!");
    wifiConnect();

  }

  delay(delay_time);
}

void wifiConnect(){

  //prints on the display "connecting"
  u8g2.clearBuffer();
  u8g2.setFont(TitleFont);
  u8g2.drawStr(0, 35, "Connecting..."); 
  u8g2.sendBuffer();

  WiFi.begin(ssid, pass);
  Serial.print("Connecting to Wifi");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnected!!!");

}

void screenDraw(float mem, float CU, float CT){

  u8g2.clearBuffer();

  u8g2.setFont(TitleFont);
  u8g2.drawStr(0, 12, "PC STATUS");
  u8g2.drawHLine(0 , 16, 128);

  u8g2.setFont(DataFont);
  
  char memstr[16], cpuU[16], cpuT[16];

  snprintf(memstr, 16, "Mem: %.1f%%", mem);
  snprintf(cpuU, 16, "Cpu: %.1f%%", CU);
  snprintf(cpuT, 16, "Temp: %.1f C", CT);

  u8g2.drawStr(10, 30, memstr);
  u8g2.drawStr(10, 45, cpuU);
  u8g2.drawStr(10, 60, cpuT);
  


  u8g2.sendBuffer();
}
