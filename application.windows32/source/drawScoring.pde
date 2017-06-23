String addOn = "+100";
int addOff = 0;
int addFa = 0;
void drawScoring() {
  textAlign(CENTER, CENTER);

  fill(200);

  textSize(90);

  text(gameScore, width/2 - transX, 50 - transY);

  fill(random(255), random(255), random(255), addFa); 

  text(addOn, width/2 - transX, 50 - transY + addOff);

  addOff += 5;
  addFa -= 10;
}