/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vlatko
 */
class Grad {
private int xpos;
private int ypos;
public Grad(int x, int y) {
xpos = x;
ypos = y;
}
public int getx() {
return xpos;
}
public int gety() {
return ypos;
}
public int rastojanie(Grad gradSosed) {
return rastojanie(gradSosed.getx(),gradSosed.gety());
}
public int rastojanie(int x, int y) {
int razlikaodX = xpos - x;
int razlikaodY = ypos - y;
return(int)Math.sqrt( razlikaodX*razlikaodX + razlikaodY*razlikaodY );
}
}