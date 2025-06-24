#include <U8g2lib.h>

// Create an object for your display
// Default ESP32 hardware I2C pins:
//   - SDA: GPIO 21 (D21)
//   - SCL: GPIO 22 (D22)
//
// If you're using different pins, use the software I2C constructor instead.
// Example for Software I2C:
// U8G2_SSD1306_128X64_NONAME_F_SW_I2C u8g2(U8G2_R0, /* clock=*/ 18, /* data=*/ 15, /* reset=*/ U8X8_PIN_NONE);

// Hardware I2C display constructor (uses default SDA/SCL)
U8G2_SSD1306_128X64_NONAME_F_HW_I2C u8g2(U8G2_R0, /* reset=*/ U8X8_PIN_NONE);

// Fill in your WiFi network credentials
const char* ssid = "your-wifi-name";
const char* pass = "your-wifi-password";

// Replace with the actual IP address of your PC/server running the Java app
const char* serverURL = "http://your-ip-address:8080/OSHI-status";

// The delay time (in milliseconds) between each data pull from the server.
// It's recommended not to go below 1000 ms (1 second) to avoid spamming the server.
const int delay_time = 1000;


//fonts used for the display 
const uint8_t* TitleFont = u8g2_font_ncenB10_tr;
const uint8_t* DataFont = u8g2_font_6x10_tr;

//debuging / testing. change to true to get debug statments
bool verbose = false;