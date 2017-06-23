class bullet {

  PVector position, velocity;

  float rotation, speed;

  boolean offScreen = false;

  bullet(float x, float y, float angle) {
    speed = 100;
    position = new PVector(x, y);
    velocity = new PVector(0, 1);
    velocity.rotate(angle*(PI/180));
    velocity.mult(-speed);
  }

  void exist() {
    drawBullet();   
    position.x += velocity.x;
    position.y += velocity.y;

    if (checkBox(position.x, position.y, -transX, -transY, width, height)) {
      offScreen = true;
    } else {
      offScreen = false;
    }
  }  
  void drawBullet() {
    stroke(255, 0, 0);


    strokeWeight(10);

    line(position.x, position.y, position.x + velocity.x, position.y + velocity.y);
  }
}