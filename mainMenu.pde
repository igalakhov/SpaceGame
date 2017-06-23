void mainMenu() {
  background(0);

  textSize(100);
  stroke(255);
  strokeWeight(5);
  fill(255, 255, 255, 200);

  text("Asteroids", width/2, 200);

  textSize(50);

  text("(remastered)", width/2, 300);

  if (checkBox(mouseX, mouseY, 200, 450, 400, 150) && mousePressed && canClick) {
    indexThere = false;
    hueChanger = 0;
    streak = 255;
    whiteFlash = 0;
    gameState = 3;
  } else if (checkBox(mouseX, mouseY, 200, 450, 400, 150)) {
    fill(255, 255, 255, 100); 
    rect(200, 450, 400, 150);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 450, 400, 150);
  }
  
  fill(255, 255, 255, 100);
  
  text("PLAY", width/2, 525);
}