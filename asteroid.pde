class asteroid {
  PVector position, velocity;
  float radius, rotation;
  FloatList radii;
  boolean dead;
  int deadCycle;
  asteroid(float x, float y, float r) {
    radii = generateJumps();
    position = new PVector(x, y);
    if (random(0, 1) > 0.5) {
      velocity = new PVector(random(5, 15), random(5, 15));
    } else {
      velocity = new PVector(random(-5, -15), random(-5, -15));
    }
    radius = r;
    dead = false;
    deadCycle = 255;
  }
  void exist() {
    if (!dead) {
      updatePosition();
      checkDeath();
    }
    drawShape();
  }
  void checkDeath() {
    //asteroid death
    if (dist(position.x, position.y, Bill.position.x, Bill.position.y) < radius) {
      dead = true; 
      gameScore += floor(radius*10);
      addOn = str(floor(radius*10));
      addOff = 0;
      addFa = 255;
    }
    //player death
    if(dist(Mike.position.x, Mike.position.y, position.x, position.y) < radius + 70){
     Mike.dead = true;
     backTint = 0;
    }
  }
  void updatePosition() {
    position.add(velocity);

    if (position.y >= mapHeight - radius) {
      velocity.y = random(-5, -15);
    }
    if (position.x >= mapWidth - radius) {
      velocity.x *= -1;
    }
    if (position.y <= radius) {
      velocity.y *= -1;
    }
    if (position.x <= radius) {
      velocity.x *= -1;
    }

    position.x = constrain(position.x, radius, mapWidth - radius);
    position.y = constrain(position.y, radius, mapHeight - radius);
  }
  void drawShape() {
    rotation += randomGaussian()*0.1;
    if (dead) {
      deadCycle -= 5; 
      radius += 5;
    }
    strokeWeight(10);
    stroke(random(deadCycle), random(deadCycle), random(deadCycle), deadCycle);
    fill(0);

    pushMatrix();

    translate(position.x, position.y);
    rotate(rotation);
    for (int i = 0; i < 361; i++) {
      rotate(PI/180);

      if (dead) {
        point(0, -1*(radii.get(i) + radius) + random(-20, 20));
      } else {
        point(0, -1*(radii.get(i) + radius));
      }
    }
    popMatrix();
  }
  FloatList generateJumps() {
    FloatList returnList = new FloatList();
    for (float i = 0; i < 361; i++) {
      returnList.append(noise(i*0.1)*random(20, 30));
    }
    return returnList;
  }
}