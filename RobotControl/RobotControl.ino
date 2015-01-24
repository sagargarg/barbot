#include <MeetAndroid.h>
#include <Servo.h>
Servo myservo;

#define LEFTSIDE_NEG   2
#define LEFTSIDE_POS   3
#define RIGHTSIDE_NEG  6
#define RIGHTSIDE_POS  7
#define TRIGGER_PIN    8
#define ECHO_PIN       12

MeetAndroid meetAndroid;

void setup(){
 /* myservo.attach(11);
  int pos = myservo.read();
  //set servo at to 180 degrees to begin with
//  while (pos != 180) {
//    myservo.write(pos);
//    delay(15);
//    pos++;
}*/
  
  
 
 Serial.begin(9600); 
 
 meetAndroid.registerFunction(forward, 'w');
 meetAndroid.registerFunction(backward, 's');
 meetAndroid.registerFunction(leftTurn, 'a');
 meetAndroid.registerFunction(rightTurn, 'd');
 meetAndroid.registerFunction(stopIt, 'x');

 
  pinMode(LEFTSIDE_NEG, OUTPUT);
  pinMode(LEFTSIDE_POS, OUTPUT);
  pinMode(RIGHTSIDE_NEG, OUTPUT);
  pinMode(RIGHTSIDE_POS, OUTPUT);
  
}


void loop(){
  //meetAndroid.receive();
  
  //while (getSonar() > 30) {
    forward2();
  //}
  
  //while (getSonar() < 30) {
    //rightTurn2();
    //delay(50);
  //}
  
  
  delay(100);
  
}

void forward(byte flag, byte numOfValues)
{
  analogWrite(LEFTSIDE_POS, meetAndroid.getInt());
  analogWrite(RIGHTSIDE_POS,meetAndroid.getInt());
  analogWrite(LEFTSIDE_NEG,0);
  analogWrite(LEFTSIDE_NEG,0);
}

void forward2()
{
  analogWrite(LEFTSIDE_POS, 255);
  analogWrite(RIGHTSIDE_POS, 255);
  analogWrite(LEFTSIDE_NEG,255);
  analogWrite(LEFTSIDE_NEG,255);
}

void backward(byte flag, byte numOfValues)
{
  analogWrite(LEFTSIDE_POS,0);
  analogWrite(RIGHTSIDE_POS,0);
  analogWrite(LEFTSIDE_NEG,meetAndroid.getInt());
  analogWrite(LEFTSIDE_NEG,meetAndroid.getInt()); 
}


void stopIt(byte flag, byte numOfValues)
{
  analogWrite(LEFTSIDE_POS,meetAndroid.getInt());
  analogWrite(RIGHTSIDE_POS,meetAndroid.getInt());
  analogWrite(LEFTSIDE_NEG,meetAndroid.getInt());
  analogWrite(RIGHTSIDE_NEG,meetAndroid.getInt()); 
}

void leftTurn(byte flag, byte numOfValues){
  analogWrite(LEFTSIDE_POS,0);
  analogWrite(LEFTSIDE_NEG,meetAndroid.getInt());
  analogWrite(RIGHTSIDE_POS,meetAndroid.getInt());
  analogWrite(RIGHTSIDE_NEG,0);
  Serial.print(meetAndroid.getInt());
}

void rightTurn(byte flag, byte numOfValues){
   analogWrite(LEFTSIDE_POS,meetAndroid.getInt());
   analogWrite(LEFTSIDE_NEG,0);
   analogWrite(RIGHTSIDE_POS,0);
   analogWrite(RIGHTSIDE_NEG,meetAndroid.getInt());  
}

void rightTurn2(){
   analogWrite(LEFTSIDE_POS, 255);
   analogWrite(LEFTSIDE_NEG,0);
   analogWrite(RIGHTSIDE_POS,0);
   analogWrite(RIGHTSIDE_NEG, 255);  
}

int getSonar(){
  int duration,distance;
  
  duration = pulseIn(ECHO_PIN,HIGH);
  distance = (duration/2)/29.1;
  
  return distance;
}




