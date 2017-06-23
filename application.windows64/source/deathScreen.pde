int hueThing = 0;

boolean canClick = true;

void deathScreen() {
  background(0);

  textAlign(CENTER, CENTER);

  textSize(110);

  fill(200);

  text("YOU DIED", width/2, 200);

  textSize(50);

  text("Your score is", width/2, 300);

  textSize(75);

  colorMode(HSB);

  fill(hueThing, 255, 255);

  hueThing++;

  if (hueThing > 255) {
    hueThing = 0;
  }

  text(gameScore, width/2, 400);

  colorMode(RGB);

  textSize(50);

  stroke(255);

  strokeWeight(5);

  if (checkBox(mouseX, mouseY, 200, 450, 400, 100) && mousePressed && canClick) {
    indexThere = false;
    hueChanger = 0;
    streak = 255;
    whiteFlash = 0;
    gameState = 3;
  } else if (checkBox(mouseX, mouseY, 200, 450, 400, 100)) {
    fill(255, 255, 255, 100); 
    rect(200, 450, 400, 100);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 450, 400, 100);
  }

  if (checkBox(mouseX, mouseY, 200, 600, 400, 100) && mousePressed && canClick) {
    gameState = 2;
    canClick = false;
  } else if (checkBox(mouseX, mouseY, 200, 600, 400, 100)) {
    fill(255, 255, 255, 100); 
    rect(200, 600, 400, 100);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 600, 400, 100);
  }


  fill(255);

  text("REPLAY", width/2, 500);

  text("MAIN MENU", width/2, 650);
}
void mouseReleased(){
 canClick = true;   
}