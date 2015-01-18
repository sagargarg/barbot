#include <Servo.h>
Servo myservo;

int motorPin1 = 2;
int motorPin2 = 4;

void setup() {
  pinMode(motorPin1, OUTPUT);
  pinMode(motorPin2, OUTPUT);
  
  myservo.attach(11);
  int pos = myservo.read();
  
  //set servo at 45 degrees to begin with
  while (pos != 180) {
    myservo.write(pos);
    delay(15);
    pos++;
  }
  myservo.write(130);
  delay(2000);
  
}
  
void loop() {
 digitalWrite(motorPin1, HIGH);
 digitalWrite(motorPin2, HIGH);
 delay(100);
  
 digitalWrite(motorPin1, LOW);
 digitalWrite(motorPin2, LOW);
 delay(1000);
 
 digitalWrite(motorPin1, HIGH);
 digitalWrite(motorPin2, HIGH);
 delay(100);
 
 digitalWrite(motorPin1, LOW);
 digitalWrite(motorPin2, LOW);
 delay(200);
}

//opens cage to deliver drink
void openCage() {
  myservo.write(85);
  delay(1500);
}

//closes cage after delivery
void closeCage() {
  myservo.write(-85);
  delay(1500);
}

//
