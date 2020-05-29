String data = "";    
char received;
void setup() 
{
  Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  pinMode(13, OUTPUT);        //Sets digital pin 13 as output pin
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
      Serial.print("asd");
    
      if((data.equals("13")) || (data == '13') || (data == 13)){
         ToggleLed(13);
         Serial.println("Toggling done");
         data = "";
  
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
}
