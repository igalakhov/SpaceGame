void resetGame() {
  gameScore = 0;
  
  Mike = new ship(400, 400);

  Bill = new bullet(-width, -height, 0);
  
  asteroids = new ArrayList();
  
  curGameCycle = 0;
 
  for (int i = 0; i < waves[curGameCycle]; i++) {
    float radius = 50 + randomGaussian()*10;
    asteroid temp = new asteroid(random(0, mapWidth), random(0, mapHeight), radius);
    asteroids.add(temp);
  }
}