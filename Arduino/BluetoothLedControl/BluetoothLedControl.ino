String data = "";    
char received;
void setup() 
{
  Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  pinMode(13, OUTPUT);        //Sets digital pin 13 as output pin
  pinMode(12, OUTPUT);
  pinMode(11, OUTPUT);
}
void loop()
{
  if(Serial.available() > 0)  
  {
    received = Serial.read();
    if(received != '\n'){
      data.concat(received);      
      Serial.print(received);        
      Serial.print("\n");        
      Serial.print("Current data: ");
      Serial.println(data);

    }else{
      
      Serial.print("Final data:");
      Serial.print(data);

    
      if((data.equals("13"))){
         ToggleLed(13);
         //Serial.println("Toggling done");
      }
      if((data.equals("12"))){
        ToggleLed(12);
      }
      if((data.equals("11"))){
        ToggleLed(11);
      }
    }
    
  }else{
   // Serial.println("Not available");                            
  }

}

void ToggleLed(int led_nr){
  if (digitalRead(led_nr) == HIGH ){
    digitalWrite(led_nr, LOW);
  }else{
    digitalWrite(led_nr, HIGH);
  }
  Serial.print("Led nr ");
  Serial.print(led_nr);
  Serial.println(" up!");
  data = "";
}
