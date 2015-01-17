int motorPin = 9;
void setup() {
  pinMode(motorPin, OUTPUT);
}
  
void loop() {
 digitalWrite(motorPin, HIGH);
 delay(100);
  
 digitalWrite(motorPin, LOW);
 delay(1000);
 
 digitalWrite(motorPin, HIGH);
 delay(100);
 
 digitalWrite(motorPin, LOW);
 delay(200);
}
