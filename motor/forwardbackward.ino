int motorPin = 9;
void setup() {
  pinMode(motorPin, OUTPUT);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
}
  
void loop() {
  openCage();
  closeCage();
}

//opens cage to deliver drink
void openCage() {
  digitalWrite(motorPin, LOW);
  digitalWrite(2, LOW);   // set leg 1 of the H-bridge low
  digitalWrite(3, HIGH);
  delay(1500);
}

//closes cage after delivery
void closeCage() {
  digitalWrite(motorPin, LOW);
  digitalWrite(3, LOW);   // set leg 1 of the H-bridge low
  digitalWrite(2, HIGH);
  delay(1500);
}
