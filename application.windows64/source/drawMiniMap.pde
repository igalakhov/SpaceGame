void drawMiniMap() {
  stroke(255);
  strokeWeight(5);

  rect(-transX + 0.75*width, -transY + 0.75*height, 0.2*width, 0.2*height);

  float playerX = map(Mike.position.x, 0, mapWidth, 0, 0.2*width);
  float playerY = map(Mike.position.y, 0, mapHeight, 0, 0.2*height);

  point(-transX + 0.75*width + playerX, -transY + 0.75*height + playerY);

  for (asteroid curAsteroid : asteroids) {
    float xPos = map(curAsteroid.position.x, 0, mapWidth, 0, 0.2*width);
    float yPos = map(curAsteroid.position.y, 0, mapHeight, 0, 0.2*height);

    stroke(random(255), random(255), random(255));
    point(-transX + 0.75*width + xPos, -transY + 0.75*height + yPos);
  }
}           