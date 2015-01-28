#include <MeetAndroid.h>
#include <Servo.h>

MeetAndroid meetAndroid;
Servo myservo;

#define TRIGGER_PIN    8
#define ECHO_PIN       12

void setup(){
  myservo.attach(11);
  
 
  int pos = myservo.read();
  
  //set servo at to 180 degrees to begin with
  while (pos != 180) {
    myservo.write(pos);
    delay(15);
    pos++;
  }


Serial.begin(9600); 

meetAndroid.registerFunction(openTop, 'w');
meetAndroid.registerFunction(closeTop, 'x');  
}

void loop(){
  meetAndroid.receive();
  Serial.print(getSonar());
  
}

void openTop() {
  myservo.write(100);
  delay(1000);
}

void closeTop() {
  myservo.write(180);
  delay(1000);
}

void openTop(byte flag, byte numOfValues) {
  //130
  myservo.write(meetAndroid.getInt());
}

void closeTop(byte flag, byte numOfValues) {
  //180
  myservo.write(meetAndroid.getInt());
}

int getSonar(){
  int duration,distance;
  digitalWrite(TRIGGER_PIN,HIGH);
  duration = pulseIn(ECHO_PIN,HIGH);
  distance = (duration/2)/29.1;
  
  return distance;
}


