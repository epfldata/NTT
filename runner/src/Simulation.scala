package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor

object Simulation extends App {
  val mainClass                                         = MainInit.liftedMain
  val observer: ClassWithObject[Observer]               = Observer.reflect(IR)
  val WORKER_observer: ClassWithObject[WORKER_Observer] = WORKER_Observer.reflect(IR)
  val link: ClassWithObject[Link]                       = Link.reflect(IR)
  val WORKER_link: ClassWithObject[WORKER_Link]         = WORKER_Link.reflect(IR)
  val wolf: ClassWithObject[Wolf]                       = Wolf.reflect(IR)
  val WORKER_wolf: ClassWithObject[WORKER_Wolf]         = WORKER_Wolf.reflect(IR)
  val agent: ClassWithObject[Agent]                     = Agent.reflect(IR)
  val turtle: ClassWithObject[Turtle]                   = Turtle.reflect(IR)
  val WORKER_turtle: ClassWithObject[WORKER_Turtle]     = WORKER_Turtle.reflect(IR)
  val patch: ClassWithObject[Patch]                     = Patch.reflect(IR)
  val WORKER_patch: ClassWithObject[WORKER_Patch]       = WORKER_Patch.reflect(IR)
  compileSims(
    List(
      WORKER_link,
      WORKER_turtle,
      turtle,
      WORKER_patch,
      observer,
      agent,
      wolf,
      link,
      patch,
      WORKER_wolf,
      WORKER_observer
    ),
    Some(mainClass)
  )
}
