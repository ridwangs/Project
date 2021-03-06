package MVC.Model.objects

import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

class Humans extends Animate_Objects (100, 10, 5){
  var xcoord: Double = 0
  var ycoord: Double = 0

  // keys
  var leftKeyHeld = false
  var rightKeyHeld = false
  var upKeyHeld = false
  var downKeyHeld = false
  var spaceKeyHeld = false


  var shape: Circle = new Circle {
    radius = 24.0
    fill = Color.Blue
  }

  def collide(other: Humans): Boolean = {

    val xdistance = this.shape.centerX.value - other.shape.centerX.value
    val ydistance = this.shape.centerY.value - other.shape.centerY.value
    val sumradius = Math.sqrt(xdistance*xdistance + ydistance*ydistance)
    sumradius < this.shape.radius.value+other.shape.radius.value
  }

  def collide(other: Inanimate_Objects): Boolean = {

    val xdistance = this.shape.centerX.value - other.shape.centerX.value
    val ydistance = this.shape.centerY.value - other.shape.centerY.value
    val sumradius = Math.sqrt(xdistance*xdistance + ydistance*ydistance)
    sumradius < this.shape.radius.value+other.shape.radius.value
  }

  def stayput(other: Humans): Unit = {
    val sumradius = this.shape.radius.toDouble + other.shape.radius.toDouble
    val xdistance = this.shape.centerX.value - other.shape.centerX.value
    val ydistance = this.shape.centerY.value - other.shape.centerY.value
    val length = Math.sqrt(Math.pow(xdistance, 2)+ Math.pow(ydistance, 2))
    val ox = other.shape.centerX
    val oy = other.shape.centerY
    val tx = this.shape.centerX
    val ty = this.shape.centerY
    this.shape.centerX = ox.toDouble + sumradius * (xdistance/length)
    this.shape.centerY = oy.toDouble + sumradius * (ydistance/length)
//    other.shape.centerX = tx.toDouble + sumradius * (xdistance/length)
//    other.shape.centerY = ty.toDouble + sumradius * (ydistance/length)
  }
  
  
  override def consumeObject(consumedObject: Inanimate_Objects): Unit = {
    consumedObject.effect(this)
  }

  override def attack(opp: Animate_Objects): Unit = {
    opp.health -= 150//health - attacker.strength
  }

}
