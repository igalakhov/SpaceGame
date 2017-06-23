float hueChanger = 0;
float streak = 255;

float whiteFlash = 0;

void waitMenu(){
 colorMode(HSB);
 
 background(hueChanger, 255, streak);
 hueChanger += 0.1;
 
 if(indexThere){
  streak--;
 } else {
  streak += 5;
 }
 streak = constrain(streak, -10, 255);
  
 colorMode(RGB);
 
 fill(255);
 
 textAlign(CENTER, CENTER);
 
 text("You are almost ready, hold your hand over the leap motion controller to begin.", 0, 0, 800, 800);
 
 if(streak == -10){
  whiteFlash = 255;
  resetGame();
  gameState = 0;  
 }   
}