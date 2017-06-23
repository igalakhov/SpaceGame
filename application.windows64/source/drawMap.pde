float cursorSpeed = 0.1;
float cursorRotation = 0;
int gridSize = 100;
void drawMap() {
  //translate!!!

  translate(transX, transY);

  //background is black
  background(0);

  //map border

  strokeWeight(10);

  stroke(255);

  noFill();

  rect(0, 0, mapWidth, mapHeight, 50);

  float deltaX = cursorPosition.x + transX;
  float deltaY = cursorPosition.y + transY;

  cursorPosition.x = -transX + deltaX;
  cursorPosition.y = -transY + deltaY;
  drawCursor(); //draw the cursor

  fill(255);

  strokeWeight(1);

  stroke(255, 255, 255, 50);

  for (int i = 0; i < mapWidth; i += gridSize) {
    line(i, 0, i, mapHeight);
  }
  for (int i = 0; i < mapHeight; i += gridSize) {
    line(0, i, mapWidth, i);
  }
  
  stroke(255);
  
  for(PVector star : stars){
   point(star.x, star.y); 
  }
}