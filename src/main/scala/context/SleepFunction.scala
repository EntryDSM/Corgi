package context

trait SleepFunction[A] extends (Integral[A] => Unit)
