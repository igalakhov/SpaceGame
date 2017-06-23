void drawCursor() {

  pushMatrix();

  translate(cursorPosition.x, cursorPosition.y);

  rotate(cursorRotation);

  scale(sin(cursorRotation*2)/5 + 1.5);

  strokeWeight(2);

  line(0, -20, 0, -5);
  line(0, 20, 0, 5);
  line(-20, 0, -5, 0);
  line(20, 0, 5, 0);

  cursorRotation += cursorSpeed;

  popMatrix();

  strokeWeight(1);
}