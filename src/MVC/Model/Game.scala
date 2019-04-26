package MVC.Model

import MVC.Model.fruits.{apple, banana, orange}
import MVC.Model.objects.Humans
import scalafx.scene.Group
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.collection.mutable.ListBuffer

class Game extends {
  val h1 = new Humans

  var anyRandom: scala.util.Random = scala.util.Random
  var allHumans: ListBuffer[Humans] = ListBuffer()
  val fruits: List[String] = List("apple", "banana", "orange")
  var allApple: ListBuffer[apple] = ListBuffer()
  var allBanana: ListBuffer[banana] = ListBuffer()
  var allOrange: ListBuffer[orange] = ListBuffer()

  val maximumWidth = 1920
  val maximumHeight = 1020

  var timeSpawn = 7.0
  var lastUpdateTime: Long = System.nanoTime()
  var objects: Group = new Group {}

  var leftKeyHeld = false
  var rightKeyHeld = false
  var upKeyHeld = false
  var downKeyHeld = false
  var spaceKeyHeld = false

  def createPlayers(player: Humans): Unit = {
    player.shape.centerX = maximumWidth / 2
    player.shape.centerY = maximumHeight / 2
    objects.children.add(player.shape)
    allHumans = allHumans :+ player
  }
  createPlayers(h1)

  def collide(circle1: Circle, circle2: Circle): Boolean = {
    val xdistance = circle1.centerX.value - circle2.centerX.value
    val ydistance = circle1.centerY.value - circle2.centerY.value
    val sumradius = Math.sqrt(xdistance*xdistance + ydistance*ydistance)
    sumradius < circle1.radius.value+circle2.radius.value
  }

  def stayput(circle1: Circle, circle2: Circle): Unit = {
    val sumradius = circle1.radius.toDouble + circle2.radius.toDouble
    val xdistance = circle1.centerX.toDouble - circle2.centerX.toDouble
    val ydistance = circle1.centerY.toDouble - circle2.centerY.toDouble
    val length = Math.sqrt(Math.pow(xdistance, 2)+ Math.pow(ydistance, 2))
    circle1.centerX = circle2.centerX.toDouble + sumradius * (xdistance/length)
    circle1.centerY = circle2.centerY.toDouble + sumradius * (ydistance/length)
  }

  def createFruits(x: String): Unit ={
    x match {
      case "apple" =>
        val apple = new apple()
        apple.shape.centerX = maximumWidth*Math.random
        apple.shape.centerY = maximumHeight*Math.random
        objects.children.add(apple.shape)
        allApple = allApple :+ apple
      case "orange" =>
        val orange = new orange()
        orange.shape.centerX = maximumWidth*Math.random
        orange.shape.centerY = maximumHeight*Math.random
        objects.children.add(orange.shape)
        allOrange = allOrange :+ orange
      case "banana" =>
        val banana = new banana()
        banana.shape.centerX = maximumWidth*Math.random
        banana.shape.centerY = maximumHeight*Math.random
        objects.children.add(banana.shape)
        allBanana = allBanana :+ banana
      case _ =>
    }
  }

  def createComputer(player: Humans): Unit ={
    player.shape.fill = Color.Red
    player.shape.centerX = 0.9*maximumWidth*Math.random()
    player.shape.centerY = 0.9*maximumHeight*Math.random()
    objects.children.add(player.shape)
    allHumans = allHumans :+ player
  }

  var h2 = new Humans
  createComputer(h2)

  val update: Long => Unit = (time: Long) => {
    val dt: Double = (time - lastUpdateTime) / 1000000000.0
    lastUpdateTime = time

    if(collide(h1.shape,h2.shape )){
      stayput(h1.shape, h2.shape)
    }

    if(collide(h1.shape,h2.shape ) && spaceKeyHeld){
      val c = List(Color.Red, Color.Green, Color.Purple)
      h1.shape.fill = c(anyRandom.nextInt(3))
      if(h1.health < 0){
        h1.shape.disable() = true
        h1.shape.visible() = false
      }
    }

    for(a<- allApple){
      if(collide(h1.shape,a.shape)){
        allApple.remove(allApple.indexOf(a))
        h1.consumeObject(a)
        a.shape.disable() = true
        a.shape.visible() = false
      }
    }

    for(b<- allBanana){
      if(collide(h1.shape,b.shape)){
        h1.consumeObject(b)
        allBanana.remove(allBanana.indexOf(b))
        b.shape.disable() = true
        b.shape.visible() = false
      }
    }

    for(o<- allOrange){
      if(collide(h1.shape,o.shape)){
        allOrange.remove(allOrange.indexOf(o))
        h1.consumeObject(o)
        o.shape.disable() = true
        o.shape.visible() = false
      }
    }

    if(leftKeyHeld) h1.shape.centerX.value -= h1.speed*0.25
    if(rightKeyHeld) h1.shape.centerX.value += h1.speed*0.25
    if(upKeyHeld) h1.shape.centerY.value -= h1.speed*0.25
    if(downKeyHeld) h1.shape.centerY.value += h1.speed*0.25

    timeSpawn -= dt
    if (timeSpawn < 0) {
      if (allApple.length + allBanana.length + allOrange.length >= 4) {
        timeSpawn = 5.0
      }
      else {
        createFruits(fruits(anyRandom.nextInt(3)))
        timeSpawn = 5.0
      }
    }
  }

}