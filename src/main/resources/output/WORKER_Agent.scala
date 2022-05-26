package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class WORKER_Agent(
    val DEFAULT_observer: Observer,
    val DEFAULT_Parent: Agent,
    val DEFAULT_logs: mutable.Map[String, Any],
    val DEFAULT_ASK: Int
) extends Agent(DEFAULT_observer, 0, 0, -1) {
  def main(): Unit = {
    DEFAULT_UpdateFromParent(DEFAULT_logs)
    while (true) {
      handleMessages()
      default_is_done = true
      waitLabel(Turn, 1)
    }
  }
}
